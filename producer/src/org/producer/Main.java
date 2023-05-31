package org.producer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            PORT = 9000;
        }

        // 终端输入
        Scanner scanner = new Scanner(System.in);
        // 循环判断是否取号
        while (true) {
            System.out.println("输入: 1-取号");
            String s = scanner.nextLine().trim().toLowerCase();
            if (s.equals("1")) {
                // 创建一个socket连接
                try (Socket socket = new Socket(IP, PORT)) {
                    // 获取输入流
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    // 读取服务器返回的数据
                    String readLine = br.readLine();
                    System.out.println("获取号码 " + readLine);
                } catch (IOException e) {
                    System.out.println("连接服务器错误，请重试: " + e.getMessage());
                }
            } else if (s.equals("q") || s.equals("quit")) {
                System.exit(0);
            }
        }
    }
}