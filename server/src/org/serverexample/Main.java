package org.serverexample;

public class Main {
    public static void main(String[] args) {
        if (args.length == 1) {
            NumberServer.PORT = Integer.parseInt(args[0]);
        } else {
            NumberServer.PORT = 8989;
        }
        FileSystem.reloadData();
        new NumberServer().createServer();

    }
}