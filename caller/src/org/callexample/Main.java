package org.callexample;

//在主方法中创建并启动线程
public class Main {
    public static void main(String[] args) {
        //创建Runnable对象
        CallRunnable r = new CallRunnable();
        //创建Thread对象，并将Runnable对象作为参数传入
        Thread t = new Thread(r);
        //调用start方法启动线程
        t.start();
    }
}
