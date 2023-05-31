package org.consumer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    public static InetAddress IP;
    public static int PORT;

    public static void main(String[] args) throws UnknownHostException {
        // 判断是否有传入两个参数
        // 第一个参数为IP
        // 第二个参数为端口
        if (args.length == 2) {
            IP = InetAddress.getByName(args[0]);
            PORT = Integer.parseInt(args[1]);
        } else {
            IP = InetAddress.getLocalHost();
            PORT = 9001;
        }

        // 终端输入
        Scanner scanner = new Scanner(System.in);
        // 循环判断是否叫号
        while (true) {
            System.out.println("输入: 1-叫号");
            String s = scanner.nextLine().trim().toLowerCase();
            if (s.equals("1")) {
                // 创建一个socket连接
                try (Socket ignored = new Socket(IP, PORT)) {
                    System.out.println("成功叫号");
                } catch (IOException e) {
                    System.out.println("连接服务器错误，请重试: " + e.getMessage());
                }
            } else if (s.equals("q") || s.equals("quit")) {
                System.exit(0);
            }
        }

    }
}
