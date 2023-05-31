package org.display;

import org.displaysdk.Display;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
            PORT = 9002;
        }

        // 向服务器请求数据
        try (Socket socket = new Socket(IP, PORT)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String readStr;
            while ((readStr = br.readLine()) != null) {
                // 等待数据返回，调用展示方法展示数据
                new Display().show(readStr);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}