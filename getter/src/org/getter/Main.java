package org.getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    public static InetAddress IP;
    public static int PORT;

    public static void main(String[] args) throws UnknownHostException {
        if (args.length == 2) {
            IP = InetAddress.getByName(args[0]);
            PORT = Integer.parseInt(args[1]);
        } else {
            IP = InetAddress.getLocalHost();
            PORT = 8989;
        }
        while (true) {
            System.out.println("输入: 1-取号");
            Scanner sc = new Scanner(System.in);
            String s = sc.nextLine();
            if (s.equals("1")) {
                try (Socket socket = new Socket(IP, PORT)) {

                    PrintStream ps = new PrintStream(socket.getOutputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    ps.println("addNumber");
                    String readLine = br.readLine();
                    System.out.println("获取号码" + readLine);


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (s.equals("quit") || s.equals("q")) {
                break;
            }
        }
    }
}