package org.serverexample;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class Number {
    //保存排队的队列
    private static final LinkedBlockingQueue<Integer> numberQueue = new LinkedBlockingQueue<>();

    // 在线显示器
    private static final ArrayList<Socket> onlineMonitor = new ArrayList<>();

    // 柜台列表
    private static final ArrayList<Integer> counterList = new ArrayList<>();

    // 叫号中的列表
    private static final ArrayList<Integer[]> callingList = new ArrayList<>();

    // 服务器方法路由器
    public static final HashMap<String, Consumer<String[]>> router = new HashMap<>();

    // 排队号码统计
    private static int numberCount = 1;

    // 叫号数
    private static int callNumberCount = 0;

    // 过号数
    private static int passNumberCount = 0;

    /**
     * 添加记录在线的显示器
     *
     * @param socket - 显示器的socket
     */
    public static void addOnlineMonitor(Socket socket) {
        System.out.println("有新的显示器连接");
        onlineMonitor.add(socket);
    }

    /**
     * 删除记录在线的显示器
     *
     * @param socket - 显示器的socket
     */
    public static void removeOnlineMonitor(Socket socket) {
        if (onlineMonitor.remove(socket)) {
            System.out.println("有显示器下线了");
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 给每个记录的显示器发送队列信息
     */
    public static void sendToOnlineMonitor() {
        for (Socket socket : onlineMonitor) {
            try {
                PrintStream ps = new PrintStream(socket.getOutputStream());
                ps.println(getQueueInfo());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 返回号码队列各种信息
     *
     * @return - 返回号码队列各种信息
     */
    public static String getQueueInfo() {
        HashMap<String, Object> objMap = new HashMap<>();
        objMap.put("numberQueue",
                   numberQueue);

        objMap.put("callingList",
                   callingList);

        objMap.put("callNumberCount",
                   callNumberCount);

        objMap.put("passNumberCount",
                   passNumberCount);

        return objMap.toString();
    }

    //    /**
    //     * 返回叫号中的号码列表
    //     *
    //     * @return - 叫号中的号码列表
    //     */
    //    public static String getCalling() {
    //        StringBuilder result = new StringBuilder();
    //        for (Integer[] array : callingList) {
    //            result.append(Arrays.toString(array));
    //        }
    //        return result.toString();
    //    }

    /**
     * 向末尾添加号码
     */
    public static int addNumber() {
        numberQueue.offer(numberCount);
        System.out.println("向队列添加号码:" + numberCount);
        return numberCount++;
    }

    /**
     * 创建叫号器
     *
     * @param str - 叫号器编号
     * @return - 成功 / 失败
     */
    public static String createCaller(String str) {
        int counterID = Integer.parseInt(str);
        if (!counterList.contains(counterID)) {
            counterList.add(counterID);
            System.out.println("创建柜台" + counterID + "成功");
            return "成功";
        }
        System.out.println("创建柜台" + counterID + "失败");
        return "失败";
    }

    /**
     * 传入柜台号码与叫的号绑定，并移除头部的号码
     *
     * @param counterNum - 柜台号码
     * @return - 返回号码
     */
    public static Integer removeNumber(String counterNum) {
        int counterID = Integer.parseInt(counterNum);
        Integer num = numberQueue.poll();
        System.out.println("删除队列号码:" + num + ";队列剩余号码数:" + numberQueue.size());
        if (num != null) {
            Integer[] info = {num, counterID};
            callingList.add(info);
            callNumberCount++;
        }
        return num;
    }

    /**
     * 判断用户是否到场，过号就将号码放回队列的末尾，
     *
     * @param isPresent - 用户是否到场
     * @param num       - 用户的号码
     */
    public static void checkIn(String isPresent, String num) {
        int intNum = Integer.parseInt(num);
        if (!Boolean.parseBoolean(isPresent)) {
            numberQueue.offer(intNum);// 过号，放到队尾
            passNumberCount++;
            System.out.println("用户不在场，号码放置队尾");
        }
        // 未过号，删除叫号中列表的号码
        System.out.println("从叫号列表中删除号码");
        callingList.removeIf(integers -> integers[0] == intNum);
    }


}
