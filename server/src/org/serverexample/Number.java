package org.serverexample;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class Number {
    //保存排队的队列
    private static final LinkedBlockingQueue<Integer> numberQueue = new LinkedBlockingQueue<>();

    private static final ArrayList<Socket> onlineMonitor = new ArrayList<>();
    // 柜台列表
    private static final ArrayList<Integer> counterList = new ArrayList<>();

    // 叫号中的列表
    private static final ArrayList<Integer[]> numberCallingList = new ArrayList<>();

    //
    public static final HashMap<String, Consumer<String[]>> router = new HashMap<>();

    // 排队号码统计
    private static int numberCount = 1;


    /**
     * 返回号码队列
     *
     * @return - 返回号码队列
     */
    public static String getQueue() {
        return numberQueue.toString();
    }

    /**
     * 返回叫号中的号码列表
     *
     * @return - 叫号中的号码列表
     */
    public static String getCalling() {
        StringBuilder result = new StringBuilder();
        for (Integer[] array : numberCallingList) {
            result.append(Arrays.toString(array));
        }
        return result.toString();
    }

    /**
     * 向末尾添加号码
     */
    public static int addNumber() {
        numberQueue.offer(numberCount);
        System.out.println("向队列添加号码:" + numberCount);
        return numberCount++;
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
        System.out.println("取出了队列的消息:" + num + ";队列剩余消息数:" + numberQueue.size());
        if (num != null) {
            Integer[] info = {num, counterID};
            numberCallingList.add(info);
        }
        return num;
    }

    /**
     * 判断用户是否到场
     *
     * @param bool - 用户是否到场
     * @param num  - 用户的号码
     */
    public static void checkIn(String bool, String num) {
        int intNum = Integer.parseInt(num);
        if (!Boolean.parseBoolean(bool)) {
            numberQueue.add(intNum);//过号，放到队尾
        }
        for (int i = 0; i < numberCallingList.size(); i++) {
            Integer[] array = numberCallingList.get(i);
            if (array[0] == intNum) numberCallingList.remove(i);
        }
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
            return "成功";
        }
        return "失败";
    }


}
