package org.example;

import java.io.IOException;

public class GlobalUtils {
    public static void clearCMD() {
        String osName = System.getProperty("os.name");
        try {
            if (osName.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
