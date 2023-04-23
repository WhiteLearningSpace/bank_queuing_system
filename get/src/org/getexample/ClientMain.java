package org.getexample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientMain {
    public static void main(String[] args) {
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
