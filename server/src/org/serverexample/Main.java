package org.serverexample;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length == 1) {
            NumberServer.PORT = Integer.parseInt(args[0]);
        } else {
            NumberServer.PORT = 8989;
        }

        while (true){
            System.out.println("是否加载上次数据 1.是 0.否");
            Scanner sc = new Scanner(System.in);
            String userInput =  sc.nextLine();
            if(userInput.equals("1")){
                FileSystem.reloadData();
                break;
            }
            else if (userInput.equals("0")){
                break;
            }
        }
        new NumberServer().createServer();

    }
}