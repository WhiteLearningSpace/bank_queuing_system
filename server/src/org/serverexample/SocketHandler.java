package org.serverexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
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
                                                    .accept(readStr.split("&")));

            Number.sendToOnlineMonitor();

            if (readStr.equals("addOnlineMonitor")) {
                int i = br.read();
            }
        } catch (IOException e) {
            Number.removeOnlineMonitor(socket);
        }
    }

    private void createRouterMap() {
        Number.router.put("addNumber.*",
                          strings -> {
                              int num = Number.addNumber();
                              ps.println(num);
                              try {
                                  socket.close();
                              } catch (IOException e) {
                                  throw new RuntimeException(e);
                              }
                          });

        Number.router.put("createCaller.*",
                          strings -> {
                              String isOk = Number.createCaller(strings[1]);
                              ps.println(isOk);
                              try {
                                  socket.close();
                              } catch (IOException e) {
                                  throw new RuntimeException(e);
                              }
                          });

        Number.router.put("removeNumber.*",
                          strings -> {
                              Integer num = Number.removeNumber(strings[1]);
                              ps.println(num);
                              try {
                                  socket.close();
                              } catch (IOException e) {
                                  throw new RuntimeException(e);
                              }
                          });

        Number.router.put("checkIn.*",
                          strings -> {
                              Number.checkIn(strings[1],
                                             strings[2]);

                              try {
                                  socket.close();
                              } catch (IOException e) {
                                  throw new RuntimeException(e);
                              }
                          });

        Number.router.put("addOnlineMonitor",
                          strings -> Number.addOnlineMonitor(socket));
    }

}
