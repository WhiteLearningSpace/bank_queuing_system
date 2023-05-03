package org.displayexample;

import org.displaysdk.Display;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) throws UnknownHostException {
        if (args.length == 2) {
            Display.IP = InetAddress.getByName(args[0]);
            Display.PORT = Integer.parseInt(args[1]);
        } else {
            Display.IP = InetAddress.getLocalHost();
            Display.PORT = 8989;
        }

        new Display().show();
    }
}