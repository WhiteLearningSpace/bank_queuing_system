package org.server;

import org.example.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue implements Serializable {
    private static final MessageQueue instance = new MessageQueue();

    private MessageQueue() {
    }

    public static MessageQueue getInstance() {
        return instance;
    }

    /**
     * 排队队列
     */
    private final LinkedBlockingQueue<Message> queueingQueue = new LinkedBlockingQueue<>();

    /**
     * 待处理队列
     */
    private final LinkedBlockingQueue<Message> pendingQueue = new LinkedBlockingQueue<>();

    /**
     * 失败队列
     */
    private final LinkedBlockingQueue<Message> failedQueue = new LinkedBlockingQueue<>();

    /**
     * 存放所有订阅者的socket
     */
    private final transient ArrayList<Socket> subscribers = new ArrayList<>();

    /**
     * 排队队列统计
     */
    public Integer queueCount = 0;

    /**
     * 生产消息
     *
     * @param message 消息
     */
    public void produce(Message message) {
        if (queueingQueue.offer(message)) {
            System.out.println("排队队列生产" + message);
        }
        notifySubscribers();
    }


    /**
     * 消费消息
     *
     * @return 消息
     */
    public Message consume() {
        Message message = queueingQueue.poll();
        if (message != null) {
            pendingQueue.offer(message);
            System.out.println("排队队列消费" + message);
            return message;
        }
        notifySubscribers();
        return null;
    }

    public void confirmConsume(Message message) {
        if (!pendingQueue.remove(message)) {
            System.out.println("待处理队列消费失败" + message);
            return;
        }
        if (message.getConfirmed()) {
            System.out.println("待处理队列消费" + message);
        } else {
            failedQueue.offer(message);
            System.out.println("消费失败" + message);
        }
        notifySubscribers();
    }

    /**
     * 给订阅者列表添加订阅者
     *
     * @param socket - 添加的订阅者socket
     */
    public void subscribe(Socket socket) {
        System.out.println("新增一名订阅者");
        subscribers.add(socket);
        notifySubscribers();
    }

    /**
     * 从订阅者列表中删除指定的订阅者
     *
     * @param socket - 要删除的订阅者socket
     */
    public void unsubscribe(Socket socket) {
        subscribers.remove(socket);
        System.out.println("有一名订阅者取消订阅");
    }


    /**
     * 给所有订阅者发送消息
     */
    public void notifySubscribers() {
        for (Socket subscriber : subscribers) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(subscriber.getOutputStream());
                oos.writeObject(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public LinkedBlockingQueue<Message> getQueueingQueue() {
        return queueingQueue;
    }

}
