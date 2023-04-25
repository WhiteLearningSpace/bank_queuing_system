package org.serverexample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

public class Number {
    //保存消息的数据容器
    private static final LinkedBlockingQueue<Integer> messageQueue = new LinkedBlockingQueue<>();
    private static final ArrayList<Integer> counterList = new ArrayList<Integer>();
    private static final ArrayList<Integer[]> numberCalledList = new ArrayList<Integer[]>();
    private static int numberCount = 1;

    public static String getQueue() {
        return messageQueue.toString();
    }

    //生产消息
    public static void produce() {
        messageQueue.offer(numberCount);
        System.out.println("向队列添加了消息:" + numberCount);

        numberCount++;
    }

    //消费消息
    public static Integer consume(String str) {
        int counterID = Integer.parseInt(str);
        Integer num = messageQueue.poll();
        System.out.println("取出了队列的消息:" + num + ";队列剩余消息数:" + messageQueue.size());
        if (num != null) {
            Integer[] info = {num, counterID};
            numberCalledList.add(info);
            for (int i = 0; i < numberCalledList.size(); i++) {
                System.out.println(Arrays.toString(numberCalledList.get(i)));
            }
        }
        return num;
    }


    public static void checkIn(String bool, String num) {
        int intNum = Integer.parseInt(num);
        if (!Boolean.parseBoolean(bool)) {
            messageQueue.add(intNum);//过号，放到队尾
        }
        for (int i = 0; i < numberCalledList.size(); i++) {
            Integer[] array = numberCalledList.get(i);
            if (array[0] == intNum) numberCalledList.remove(i);
        }
    }

    public static String createCaller(String str) {
        int counterID = Integer.parseInt(str);
        if (!counterList.contains(counterID)) {
            counterList.add(counterID);
            return "成功";
        }
        return "失败";
    }
}
