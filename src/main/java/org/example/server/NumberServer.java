package org.example.server;


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

                if (s.equalsIgnoreCase("consume")) {
                    dos.writeUTF("叫号");
                    Number.consume();
                } else if (s.equalsIgnoreCase("produce")) {
                    dos.writeUTF("取号");
                    Number.produce();
                } else if (s.equalsIgnoreCase("all")) {
                    dos.writeUTF(Number.getQueue());
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
