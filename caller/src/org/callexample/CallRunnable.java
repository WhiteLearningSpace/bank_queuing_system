package org.callexample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class CallRunnable implements Runnable {
    @Override
    public void run() {

        Scanner scanner = new Scanner(System.in);
        byte counterID;


        while (true) {
            System.out.println("请输入柜台号码：");
            counterID = scanner.nextByte();

            //传递柜台号码
            try (Socket socket = new Socket(InetAddress.getLocalHost(),
                    8989)) {

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                dos.writeUTF("createCaller&" + counterID);

                if (dis.readUTF().equals("失败")) {//未到场，过号处理
                    System.out.println("柜台已在别处登录");
                    continue;
                }

                dos.close();
                dis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            break;
        }

        while (true) {
            try (Socket socket = new Socket(InetAddress.getLocalHost(),
                    8989)) {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                dos.writeUTF("removeNumber&" + counterID);
                String num = dis.readUTF();


                System.out.println("用户是否到场？");
                checkIn(scanner.nextInt() != 0, num);

                dos.close();
                dis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void checkIn(Boolean bool, String str) {
        try (Socket socket = new Socket(InetAddress.getLocalHost(),
                8989)) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF("checkIn&" + bool + "&" + str);

            dos.close();
            dis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

