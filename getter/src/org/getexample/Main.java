package org.getexample;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("取号请按输入1");
        System.out.println("输入2结束");
        while (true) {
            Scanner sc = new Scanner(System.in);
            String s = sc.nextLine();
            if (s.equals("1")) {
                try (Socket socket = new Socket(InetAddress.getLocalHost(),
                                                8989)) {

                    PrintStream ps = new PrintStream(socket.getOutputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    ps.println("addNumber");
                    String readLine = br.readLine();
                    System.out.println("获取号码" + readLine);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (s.equals("2")) {
                System.out.println("退出");
                break;
            }
        }
    }
}