package org.serverexample;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class NumberServer {
    public static int PORT;

    public void createServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();

            while (nifs.hasMoreElements()) {
                NetworkInterface nif = nifs.nextElement();
                Enumeration<InetAddress> address = nif.getInetAddresses();
                while (address.hasMoreElements()) {
                    InetAddress addr = address.nextElement();
                    if (addr instanceof Inet4Address) {
                        System.out.println("网卡名称：" + nif.getName());
                        System.out.println("网络接口地址：" + addr.getHostAddress() + ":" + PORT);
                        System.out.println();
                    }
                }
            }
            while (true) {
                Socket socket = serverSocket.accept();

                new SocketHandler(socket).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

