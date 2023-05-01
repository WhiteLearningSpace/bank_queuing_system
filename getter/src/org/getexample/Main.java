package org.getexample;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import org.example.GlobalUtils;

public class Main {
    public static void main(String[] args) {
        while (true) {
            GlobalUtils.clearCMD();
            System.out.println("输入: 1-取号、2-退出");
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