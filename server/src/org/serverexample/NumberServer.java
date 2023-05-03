package org.serverexample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NumberServer {
    public static int PORT;

    public void createServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();

                new SocketHandler(socket).start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

