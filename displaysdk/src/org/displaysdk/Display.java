package org.displaysdk;

public class Display implements IDisplay {

    @Override
    public void show(String str) {
        System.out.println(str);
    }
}