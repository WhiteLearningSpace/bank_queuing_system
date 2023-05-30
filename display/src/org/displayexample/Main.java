package org.displayexample;

import org.displaysdk.Display;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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

        Display display = new Display();

        // 向服务器请求数据
        try (Socket socket = new Socket(IP, PORT)) {
            PrintStream ps = new PrintStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 发送请求参数
            ps.println("addOnlineMonitor");

            String readStr;
            while ((readStr = br.readLine()) != null) {
                // 等待数据返回，调用展示方法展示数据
                display.show(readStr);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}