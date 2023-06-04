package org.server;

import org.example.Message;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Server {
    // 默认起始端口
    private int port = 9000;
    // 保存可用端口的数组
    private final int[] ports = new int[3];

    private final MessageQueue mq = MessageQueue.getInstance();

    Server() {
        setAvailablePorts();
        createQueueProduceServer();
        createQueueConsumeServer();
        createQueueSubscribeServer();
    }

    public void createQueueProduceServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(ports[0])) {
                System.out.println("生产服务开启成功");
                showServerAddress("生产", ports[0]);
                while (true) {
                    Socket socket = serverSocket.accept();
                    new Thread(() -> {
                        mq.queueCount++;
                        mq.produce(new Message(null, mq.queueCount, null));
                        try {
                            PrintStream ps = new PrintStream(socket.getOutputStream());
                            ps.println(mq.queueCount);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                }
            } catch (IOException e) {
                System.out.println("生产服务创建失败:" + e.getMessage());
            }
        }).start();
    }


    public void createQueueConsumeServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(ports[1])) {
                System.out.println("消费服务开启成功");
                showServerAddress("消费", ports[1]);
                while (true) {
                    Socket socket = serverSocket.accept();
                    new Thread(() -> {
                        try {
                            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                            PrintStream ps = new PrintStream(socket.getOutputStream());
//
//                            Message consume = mq.consume();
//
//
//
//                            String consumerId = br.readLine();


                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                }
            } catch (IOException e) {
                System.out.println("消费服务创建失败:" + e.getMessage());
            }
        }).start();
    }

    public void createQueueSubscribeServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(ports[2])) {
                System.out.println("订阅服务开启成功");
                showServerAddress("订阅", ports[2]);
                while (true) {
                    Socket socket = serverSocket.accept();
                    // 开启一个新的线程，让订阅者保持连接
                    new Thread(() -> {
                        try {
                            mq.subscribe(socket);
                            InputStream is = socket.getInputStream();
                            int ignored = is.read();
                        } catch (IOException e) {
                            mq.unsubscribe(socket);
                            try {
                                socket.close();
                            } catch (IOException ignored) {
                            }
                        }
                    }).start();
                }
            } catch (IOException e) {
                System.out.println("订阅服务创建失败:" + e.getMessage());
            }
        }).start();
    }

    /**
     * 设置三个没有被占用的端口，并保存到数组
     */
    public void setAvailablePorts() {
        // 用于去除已经设定过的端口
        Set<Integer> portSet = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            // 判断端口是否可用
            while (isPortUnavailable(port) || !portSet.add(port)) {
                // 端口不可用，替换端口
                replacePort();
            }
            // 端口可用，保存端口
            ports[i] = port;
            port++;
        }
    }

    /**
     * 修改端口，并再次测试是否被占用
     */
    public void replacePort() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("端口被占用，尝试更换端口: ");
            try {
                port = scanner.nextInt();
                break;
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("端口只能输入数字");
            }
        }
    }

    /**
     * 测试端口是否不可用
     *
     * @param port 要测试的端口
     * @return false端口可用，true端口不可用
     */
    public boolean isPortUnavailable(int port) {
        try (ServerSocket ignored = new ServerSocket(port)) {
            // 端口绑定成功，端口可用
            return false;
        } catch (IOException e) {
            // 端口绑定失败，端口不可用
            return true;
        }
    }

    /**
     * 列出所有网卡的IP地址
     *
     * @throws SocketException 异常
     */
    public void showServerAddress(String serverName, int port) throws SocketException {
        Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();

        while (nifs.hasMoreElements()) {
            NetworkInterface nif = nifs.nextElement();
            Enumeration<InetAddress> address = nif.getInetAddresses();
            while (address.hasMoreElements()) {
                InetAddress addr = address.nextElement();
                if (addr instanceof Inet4Address) {
                    System.out.println("网卡名称：" + nif.getName());
                    System.out.println(serverName + "服务网络接口地址：" + addr.getHostAddress() + ":" + port);
                    System.out.println();
                }
            }
        }
    }
}

