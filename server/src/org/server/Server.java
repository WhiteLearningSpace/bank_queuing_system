package org.server;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Scanner;

public class Server {
    public static Queue queue;
    public static int PORT;

    public Server() {
        reloadData();
        shutdownListener();
        createServer();
    }

    public void createServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            showNetWorkAddress();
            while (true) {
                Socket socket = serverSocket.accept();

                new SocketHandler(socket).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 展示服务器网络地址
     *
     * @throws SocketException 异常
     */
    public void showNetWorkAddress() throws SocketException {
        Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();

        while (nifs.hasMoreElements()) {
            NetworkInterface nif = nifs.nextElement();
            Enumeration<InetAddress> address = nif.getInetAddresses();
            while (address.hasMoreElements()) {
                InetAddress addr = address.nextElement();
                if (addr instanceof Inet4Address) {
                    System.out.println("网卡名称：" + nif.getName());
                    System.out.println("网络接口地址：" + addr.getHostAddress() + ":" + PORT);
                    System.out.println();
                }
            }
        }
    }

    public void reloadData() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("是否加载上次数据 1.是 0.否");
            String userInput = sc.nextLine();
            if (userInput.equals("1")) {
                queue = readData();
                break;
            } else if (userInput.equals("0")) {
                queue = new Queue();
                break;
            }
        }
    }

    /**
     * 监听关机信号
     */
    public void shutdownListener() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("执行退出")));
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String s = scanner.nextLine();
                if (s.equals("quit") || s.equals("q")) {
                    System.exit(0);
                }
            }
        }).start();
    }

    /**
     * 保存数据
     *
     * @param queue 要保存的对象
     */
    public static void saveData(Queue queue) {
        try {
            OutputStream fos = new FileOutputStream("queue.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(queue);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取数据
     *
     * @return 返回的对象数据
     */
    public static Queue readData() {
        try {
            FileInputStream fis = new FileInputStream("queue.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (Queue) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

