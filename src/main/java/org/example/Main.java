package org.example;

import org.example.server.NumberServer;

public class Main {
    public static void main(String[] args) {
        new Thread(new NumberServer()).start();
    }
}