package org.callexample;

import org.example.GlobalUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Caller {
    Scanner scanner = new Scanner(System.in);
    private int counterID;

    public Caller() {
        createCaller();
        waitCall();
    }


    private void waitCall() {
        while (true) {
            GlobalUtils.clearCMD();
            System.out.println("当前柜台ID：" + counterID);
            System.out.print("是否叫号(1-叫号): ");
            int userInput = scanner.nextInt();
            if (userInput == 1) {
                removeNumber();
            }
        }
    }

    /**
     * 创建柜台ID并判断是否成功
     */
    private void createCaller() {
        while (true) {
            GlobalUtils.clearCMD();
            // 输入柜台ID
            System.out.print("请输入柜台号码(数字)： ");
            try {
                counterID = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("参数错误，只能输入数字");
            }

            // 创建柜台ID并判断是否成功
            try (Socket socket = new Socket(InetAddress.getLocalHost(),
                    8989)) {
                PrintStream ps = new PrintStream(socket.getOutputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                ps.println("createCaller&" + counterID);
                if (br.readLine()
                        .equals("失败")) {// 柜台已存在
                    System.out.println("柜台已在别处登录");
                    continue;
                }
                System.out.println("创建柜台ID" + counterID);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            break;
        }
    }

    /**
     * 删除号码并判断用户是否到场，过号就将号码放回队列的末尾
     */
    private void removeNumber() {
        try (Socket socket = new Socket(InetAddress.getLocalHost(),
                8989)) {
            PrintStream ps = new PrintStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 删除号码
            ps.println("removeNumber&" + counterID);
            String num = br.readLine();
            if (num.equals("null")) {
                System.out.println("队列为空");
                return;
            }
            // 判断用户是否到场
            // 过号就将号码放回队列的末尾
            boolean isPresent;
            while (true) {
                System.out.print("用户是否到场(1-到场、0-未到场)？");
                int userInput = scanner.nextInt();
                if (userInput == 1 || userInput == 0) {
                    isPresent = userInput == 1;
                    break;
                }
            }
            checkIn(isPresent,
                    Integer.parseInt(num));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断用户是否到场
     *
     * @param isPresent - 是否到场
     * @param num       - 号码
     */
    private void checkIn(boolean isPresent, int num) {
        try (Socket socket = new Socket(InetAddress.getLocalHost(),
                8989)) {
            PrintStream ps = new PrintStream(socket.getOutputStream());

            // 判断用户是否到场
            ps.println("checkIn&" + isPresent + "&" + num);
            String isPresentStr = isPresent ? "在场，处理业务" : "不在场，过号";
            System.out.println("用户" + isPresentStr);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

