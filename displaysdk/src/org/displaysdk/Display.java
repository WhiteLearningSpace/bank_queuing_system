package org.displaysdk;

import org.example.GlobalUtils;

public class Display implements IDisplay {

    @Override
    public void show(String str) {
        GlobalUtils.clearCMD();
        System.out.println(str);
    }

}