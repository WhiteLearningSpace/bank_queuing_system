package org.serverexample;

import java.util.concurrent.LinkedBlockingQueue;

public class Number {
    //保存消息的数据容器
    private static final LinkedBlockingQueue<Integer> messageQueue = new LinkedBlockingQueue<>();
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
    public static Integer consume() {
        Integer num = messageQueue.poll();
        System.out.println("取出了队列的消息:" + num + ";队列剩余消息数:" + messageQueue.size());
        return num;
    }


    public static void pass(String str) {
        messageQueue.add(Integer.parseInt(str));
    }
}
