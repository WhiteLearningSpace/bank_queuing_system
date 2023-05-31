package org.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Queue {
    private static final LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
    private static final ArrayList<Socket> subscribers = new ArrayList<>();
    private static Integer queueCount = 0;


    /**
     * 为队列生产数据
     *
     * @return 返回生产的数据
     */
    public static Integer produce() {
        queueCount++;
        if (queue.offer(queueCount)) {
            System.out.println("生产" + queueCount);
        }
        notifySubscribers();
        return queueCount;
    }


    /**
     * 从队列中消费数据
     */
    public static void consume() {
        Integer num = queue.poll();
        if (num != null) {
            System.out.println("消费" + num);
        }
        notifySubscribers();
    }

    /**
     * 给订阅者列表添加订阅者
     *
     * @param socket - 添加的订阅者socket
     */
    public static void subscribe(Socket socket) {
        System.out.println("新增一名订阅者");
        subscribers.add(socket);
        notifySubscribers();
    }

    /**
     * 从订阅者列表中删除指定的订阅者
     *
     * @param socket - 要删除的订阅者socket
     */
    public static void unsubscribe(Socket socket) {
        subscribers.remove(socket);
        System.out.println("有一名订阅者取消订阅");
    }


    /**
     * 给所有订阅者发送消息
     */
    public static void notifySubscribers() {
        for (Socket subscriber : subscribers) {
            try {
                PrintStream ps = new PrintStream(subscriber.getOutputStream());
                ps.println(queue);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
