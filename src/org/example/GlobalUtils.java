package org.example;

import java.io.IOException;
import java.util.Scanner;

public class GlobalUtils {
    public static void clearCMD() {
        String osName = System.getProperty("os.name");
        try {
            if (osName.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送关机信号
     * 用多线程监听键盘输入是q或quit就关闭程序
     */
    public static void sendShutdownSignal() {
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
