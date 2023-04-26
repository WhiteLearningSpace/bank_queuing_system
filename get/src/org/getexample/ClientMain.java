package org.getexample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        System.out.println("取号请按输入1");
        System.out.println("输入2结束");
        while (true){
            Scanner sc=new Scanner(System.in);
            String s=sc.nextLine();
            if (s=="1")
            {
                produce();
            }
            else if (s=="2")
            {
                break;
            }
        }

    }
    public static void produce(){
        try (Socket socket = new Socket(InetAddress.getLocalHost(),
                8989)) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF("produce");

            System.out.println(dis.readUTF());

            dos.close();
            dis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
