package org.dispexample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Display d =new Display();

        //循环显示
        while (true) {
            Display.setQueue(getSeverMags("all"));
            d.Show();

            Display.setNowCalling(getSeverMags("getCalling"));
            d.callingNumber();

            //d.statistics();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String getSeverMags(String str){
        String mgs;
        try (Socket socket = new Socket(InetAddress.getLocalHost(),
                8989)) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF(str);

            mgs = dis.readUTF();

            dos.close();
            dis.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mgs;
    }
}