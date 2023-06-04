package org.display;

import org.displaysdk.Display;
import org.example.Message;
import org.server.MessageQueue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

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

            MessageQueue read;
            while ((read = (MessageQueue) new ObjectInputStream(socket.getInputStream()).readObject()) != null) {
                // 等待数据返回，调用展示方法展示数据
                Display display = new Display();

                LinkedBlockingQueue<Message> queue = read.getQueueingQueue();


                display.show(Arrays.toString(queue.stream().map(Message::getQueueingNumber).toArray()));
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}