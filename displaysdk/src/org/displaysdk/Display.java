package org.displaysdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class Display implements IDisplay {
    public static void main(String[] args) {
        new Display().show();
    }

    @Override
    public void show() {
        getQueueInfo();
    }

    /**
     *
     */
    private void getQueueInfo() {
        try (Socket socket = new Socket(InetAddress.getLocalHost(),
                                        8989)) {
            PrintStream ps = new PrintStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ps.println("addOnlineMonitor");

            String readStr;
            while ((readStr = br.readLine()) != null) {
                System.out.println(readStr);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}