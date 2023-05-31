package org.server;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 监听和发送关闭信号
        ShutdownSignalListener();
        // 创建服务器
        new Server();
    }

    private static void loadData() {
//        while (true) {
//            System.out.println("是否加载上次数据 1.是 0.否");
//            Scanner sc = new Scanner(System.in);
//            String userInput = sc.nextLine();
//            if (userInput.equals("1")) {
//                FileSystem.reloadData();
//                break;
//            } else if (userInput.equals("0")) {
//                break;
//            }
//        }
    }


    /**
     * 添加钩子在关机时执行
     * 用多线程监听终端输入是q或quit就发送关闭信号
     */
    public static void ShutdownSignalListener() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("执行退出")));
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine().trim().toLowerCase();
                if (input.equals("q") || input.equals("quit")) {
                    System.exit(0);
                    break;
                }
            }
        }).start();
    }
}