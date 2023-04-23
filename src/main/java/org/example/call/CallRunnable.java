package org.example.call;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class CallRunnable implements Runnable {
    @Override
    public void run() {
        try (Socket socket = new Socket(InetAddress.getLocalHost(),
                8989)) {
            Scanner scanner = new Scanner(System.in);

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            dos.writeUTF("consume");
            String num = dis.readUTF();


            System.out.println("用户是否到场？");
            if (scanner.nextInt() == 0) {//未到场，过号处理
                pass(num);
            }

            dos.close();
            dis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void pass(String str){
        try (Socket socket = new Socket(InetAddress.getLocalHost(),
                8989)) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF("pass&"+str);

            dos.close();
            dis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

