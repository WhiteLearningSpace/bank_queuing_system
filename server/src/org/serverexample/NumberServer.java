package org.serverexample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NumberServer implements Runnable {
    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(8989);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            Socket socket;
            try {
                socket = serverSocket.accept();

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                String s = dis.readUTF();

                System.out.println("debug:" + s);

                if (s.matches("consume.*")) {
                    Integer integer = Number.consume(s.split("&")[1]);
                    dos.writeUTF("" + integer);
                } else if (s.matches("produce.*")) {
                    dos.writeUTF("取号");
                    Number.produce();
                } else if (s.matches("checkIn.*")) {
                    Number.checkIn(s.split("&")[1], s.split("&")[2]);
                } else if (s.matches("createCaller.*")) {
                    String result = Number.createCaller(s.split("&")[1]);
                    dos.writeUTF(result);
                } else if (s.matches("all.*")) {
                    dos.writeUTF(Number.getQueue());
                } else if (s.matches("getCalling.*")) {
                    dos.writeUTF(Number.getCalling());
                } else {
                    dos.writeUTF("参数错误");
                }


                dos.close();
                dis.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
