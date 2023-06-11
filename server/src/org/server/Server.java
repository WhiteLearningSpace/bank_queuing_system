package org.server;

import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Scanner;

public class Server {
    public static Queue queue;
    public static int PORT;

    public Server() {
        reloadData();
        shutdownListener();
        createServer();
        createWebServer();
    }

    /**
     * 创建网页服务
     */
    public void createWebServer() {
        new Thread(() -> {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 9001), 0);
                // 设置处理请求的处理程序
                server.createContext("/", exchange -> {

                    String params = exchange.getRequestURI().getQuery();

                    Integer integer = queue.checkQueueProgress(Integer.valueOf(params));

                    String waitMsg;
                    if (integer != null) {
                        waitMsg = "<h1>当前前面还有" + integer + "人</h1>";
                    } else {
                        waitMsg = "<h1>号码不存在</h1>";
                    }

                    String response = "<!doctype html>" +
                            "<html lang=\"zh-CN\">" +
                            "<head>" +
                            "<meta charset=\"utf-8\">" +
                            "<title>进度</title>" +
                            "</head>" +
                            "<body><center>" +
                            waitMsg +
                            "</center></body>" +
                            "</html>"; // 设置响应内容

                    byte[] responseData = response.getBytes(StandardCharsets.UTF_8);

                    // 设置响应头
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(200, responseData.length); // 设置响应状态码和内容长度

                    // 发送响应内容
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(responseData);
                    outputStream.close();
                });

                // 启动服务器
                server.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * 创建队列服务
     */
    public void createServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                showNetWorkAddress();
                while (true) {
                    Socket socket = serverSocket.accept();

                    new SocketHandler(socket).start();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
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

