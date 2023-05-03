package org.getexample;

import org.example.GlobalUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            GlobalUtils.clearCMD();
            System.out.println("输入: 1-取号");
            Scanner sc = new Scanner(System.in);
            String s = sc.nextLine();
            if (s.equals("1")) {
                try (Socket socket = new Socket(InetAddress.getLocalHost(), 8989)) {

                    PrintStream ps = new PrintStream(socket.getOutputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    ps.println("addNumber");
                    String readLine = br.readLine();
                    System.out.println("获取号码" + readLine);

                    Thread.sleep(1000);

                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}