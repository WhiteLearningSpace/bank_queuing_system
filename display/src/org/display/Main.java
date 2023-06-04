package org.display;

import org.displaysdk.Display;
import org.server.Queue;

import java.io.*;
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
            PORT = 9000;
        }

        Display display = new Display();

        // 向服务器请求数据
        try (Socket socket = new Socket(IP, PORT)) {
            PrintStream ps = new PrintStream(socket.getOutputStream());

            // 发送请求参数
            ps.println("addOnlineMonitor");

            Queue read;
            // 等待数据返回，调用展示方法展示数据
            while ((read = (Queue) new ObjectInputStream(socket.getInputStream()).readObject()) != null) {
                display.show("已排队人数: " + read.numberCount);
                display.show("已叫号人数: " + read.callNumberCount);
                display.show("已过号人数: " + read.passNumberCount);
                for (Integer[] integers : read.callingList) {
                    display.show(integers[0] + "号用户到" + integers[1] + "号柜台办理业务");
                }
                display.show(read.numberQueue.toString());
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}