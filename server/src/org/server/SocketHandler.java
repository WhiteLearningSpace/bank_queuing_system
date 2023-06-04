package org.server;

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

            // 根据传来的参数，路由至对应的方法
            Queue.router.entrySet().stream().filter((entry) -> readStr.matches(entry.getKey())).findFirst().ifPresent((entry) -> entry.getValue().accept(readStr.split("&")));

            Server.queue.notifyChange();

            if (readStr.equals("addOnlineMonitor")) {
                int ignore = br.read();
            }
        } catch (IOException e) {
            Queue.removeOnlineMonitor(socket);
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * 创建路由规则
     */
    private void createRouterMap() {
        // 添加号码
        Queue.router.put("addNumber.*", strings -> {
            int num = Server.queue.addNumber();
            ps.println(num);
        });

        // 创建柜台
        Queue.router.put("createCaller.*", strings -> {
            String isOk = Queue.createCaller(strings[1]);
            ps.println(isOk);
        });

        // 删除号码
        Queue.router.put("removeNumber.*", strings -> {
            Integer num = Server.queue.removeNumber(strings[1]);
            ps.println(num);
        });

        // 确认是否到场
        Queue.router.put("checkIn.*", strings -> {
            Server.queue.checkIn(strings[1], strings[2]);
        });

        // 添加显示器
        Queue.router.put("addOnlineMonitor", strings -> Queue.addOnlineMonitor(socket));
    }

}
