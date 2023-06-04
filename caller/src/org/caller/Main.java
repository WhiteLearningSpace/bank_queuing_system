package org.caller;

import java.net.InetAddress;
import java.net.UnknownHostException;

//在主方法中创建并启动线程
public class Main {
    public static InetAddress IP;
    public static int PORT;

    public static void main(String[] args) throws UnknownHostException {
        if (args.length == 2) {
            IP = InetAddress.getByName(args[0]);
            PORT = Integer.parseInt(args[1]);
        } else {
            IP = InetAddress.getLocalHost();
            PORT = 9000;
        }

        new Caller();
    }
}
