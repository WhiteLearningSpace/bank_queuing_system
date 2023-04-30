package org.displaysdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Display implements IDisplay {
    public int[] numberQueue;
    public int[][] callingList;
    public int passNumberCount;
    public int callNumberCount;

    @Override
    public void show() {
        getQueueInfo();
    }


    private void showInfo() {
        System.out.println("-------------------------------------------------------");
        for (int[] ints : callingList) {
            System.out.println("请" + ints[0] + "号用户到" + ints[1] + "号柜台办理业务");
        }
        System.out.println("已过号" + passNumberCount);
        System.out.println("已叫号" + callNumberCount);
        System.out.println("等待中" + Arrays.toString(numberQueue));
    }

    private void getQueueInfo() {
        try (Socket socket = new Socket(InetAddress.getLocalHost(),
                                        8989)) {
            PrintStream ps = new PrintStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ps.println("addOnlineMonitor");

            String readStr;
            while ((readStr = br.readLine()) != null) {
                formatMap(readStr);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void formatMap(String mapStr) {
        String regex = "\\w+=(\\[\\[.*]]|\\w|\\[.*])";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mapStr);
        while (matcher.find()) {
            String[] strS = matcher.group()
                                   .split("=");

            switch (strS[0]) {
                case "numberQueue" -> {
                    String[] strArr = strS[1].replaceAll("[\\[\\]]",
                                                         "")
                                             .split(",");
                    numberQueue = new int[strArr.length];
                    for (int i = 0; i < strArr.length; i++) {
                        numberQueue[i] = Integer.parseInt(strArr[i].trim());
                    }
                }
                case "callingList" -> {
                    String[] strArr = strS[1].replaceAll("[\\[\\]]",
                                                         "")
                                             .split(",");
                    callingList = new int[strArr.length / 2][2];
                    for (int i = 0; i < strArr.length; i += 2) {
                        int i1 = Integer.parseInt(strArr[i].trim());
                        int i2 = Integer.parseInt(strArr[i + 1].trim());
                        callingList[i / 2] = new int[]{i1, i2};
                    }
                }
                case "passNumberCount" -> passNumberCount = Integer.parseInt(strS[1]);
                case "callNumberCount" -> callNumberCount = Integer.parseInt(strS[1]);
            }
        }


        showInfo();
    }
}