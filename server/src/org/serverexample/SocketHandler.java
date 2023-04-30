package org.serverexample;

import java.io.*;
import java.net.Socket;

class SocketHandler extends Thread {

    private final Socket socket;

    private PrintStream ps;

    public SocketHandler(Socket socket) {
        this.socket = socket;
        createRouterMap();
    }

    @Override
    public void run() {
        try {
            ps = new PrintStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String readStr = br.readLine();

            Number.router.entrySet()
                         .stream()
                         .filter((entry) -> readStr.matches(entry.getKey()))
                         .findFirst()
                         .ifPresent((entry) -> entry.getValue()
                                                    .accept(entry.getKey()
                                                                 .split("&")));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createRouterMap() {
        Number.router.put("addNumber.*",
                          (strings) -> {
                              int num = Number.addNumber();
                              ps.println(num);
                          });

        Number.router.put("removeNumber.*",
                          (strings) -> {
                              int num = Number.removeNumber(strings[1]);
                              ps.println(num);
                          });

        Number.router.put("checkIn.*",
                          (strings) -> {
                              Number.checkIn(strings[1],
                                             strings[2]);
                          });

        Number.router.put("createCaller.*",
                          (strings) -> {
                              String num = Number.createCaller(strings[1]);
                          });

        Number.router.put("all.*",
                          (strings) -> {
                              Number.getQueue();
                          });

        Number.router.put("getCalling.*",
                          (strings) -> {
                              String num = Number.getCalling();
                          });


    }

}
