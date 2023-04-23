
package org.example;

import com.display.IDisplay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Display implements IDisplay {

    static String queue;

    public static void main(String[] args) {
        try (Socket socket = new Socket(InetAddress.getLocalHost(),
                8989)) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF("all");

            queue = dis.readUTF();
            new Display().Show();

            dos.close();
            dis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void Show() {
        System.out.println("----------显示排队信息----------");
        System.out.println(queue);
    }
}
