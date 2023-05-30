package org.displaysdk;

import org.example.GlobalUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Display implements IDisplay {
    private int[] numberQueue;
    private int[][] callingList;
    private int passNumberCount;
    private int callNumberCount;

    @Override
    public void show(String str) {
        formatMap(str);
        showInfo();
    }

    /**
     * 展示信息
     */
    private void showInfo() {
        GlobalUtils.clearCMD();
        for (int[] ints : callingList) {
            System.out.println("请" + ints[0] + "号用户到" + ints[1] + "号柜台办理业务");
        }
        System.out.println("已过号" + passNumberCount);
        System.out.println("已叫号" + callNumberCount);
        System.out.println("等待中" + Arrays.toString(numberQueue));
    }

    /**
     * 格式化数据字符串，并赋值给变量
     *
     * @param mapStr - 需要格式化的字符串
     */
    private void formatMap(String mapStr) {
        if (mapStr.isEmpty()) {
            System.out.println("服务器无数据返回");
            return;
        }

        String regex = "\\w+=(\\[(?:[\\[\\w,\\s\\]]*|[\\w,\\s]*)]|\\w*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mapStr);

        while (matcher.find()) {
            String[] strS = matcher.group()
                    .split("=");

            switch (strS[0]) {
                case "numberQueue" -> {
                    String[] strArr = strS[1].replaceAll("[\\[\\]]", "")
                            .split(",");
                    if ("".equals(strArr[0])) {
                        numberQueue = new int[0];
                        break;
                    }
                    numberQueue = new int[strArr.length];
                    for (int i = 0; i < strArr.length; i++) {
                        numberQueue[i] = Integer.parseInt(strArr[i].trim());
                    }
                }
                case "callingList" -> {
                    String[] strArr = strS[1].replaceAll("[\\[\\]]", "")
                            .split(",");
                    if ("".equals(strArr[0])) {
                        callingList = new int[0][];
                        break;
                    }
                    callingList = new int[strArr.length / 2][2];
                    for (int i = 0; i < strArr.length; i += 2) {
                        int i1 = Integer.parseInt(strArr[i].trim());
                        int i2 = Integer.parseInt(strArr[i + 1].trim());
                        callingList[i / 2] = new int[]{i1, i2};
                    }
                }
                case "passNumberCount" -> passNumberCount = Integer.parseInt(strS[1]);
                case "callNumberCount" -> callNumberCount = Integer.parseInt(strS[1]);
            }
        }
    }
}