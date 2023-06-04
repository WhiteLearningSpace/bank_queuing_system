package org.server;

public class Main {
    public static void main(String[] args) {
        if (args.length == 1) {
            Server.PORT = Integer.parseInt(args[0]);
        } else {
            Server.PORT = 9000;
        }

        new Server();

    }
}