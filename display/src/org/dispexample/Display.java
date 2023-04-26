package org.dispexample;

import com.display.IDisplay;

import java.util.ArrayList;
import java.util.Arrays;

public class Display implements IDisplay {

    static int[] queueNum;

    //static int todayQueueLength = 0;//当天至当前排号数

    static int sizeSum = 0;//排号总数

    //static int overNum = 0;//过号数

    static ArrayList<Integer> eatNum;//各窗口处理数，index：窗口号，值表示处理数量

    static int[] nowCalling;//当前叫号

    public static void setQueue(String queueStr) {
        if(queueStr != null && queueStr.length() != 0){
            Display.queueNum = getNum(queueStr);
        }
        else System.out.println("暂无排队信息");
    }

    public static void setNowCalling(String nowCallingStr) {
        if(nowCallingStr != null && nowCallingStr.length() != 0){
            Display.nowCalling = getNum(nowCallingStr);
        }
    }

    @Override
    public void Show() {
        System.out.println("\n----------显示排队信息----------");
        System.out.println(Arrays.toString(queueNum));
        System.out.println("\n当前共有" + queueNum.length +"人排队");
    }

    public void callingNumber(){//叫号
        if(nowCalling != null && nowCalling.length != 0){
            System.out.println("叫号：请"+ nowCalling[0] + "号到"+ nowCalling[1] + "号窗口办理业务");
        }
    }

    public void statistics(){//数据统计
        System.out.println("统计：当天至当前排号数: "+ sizeSum + "过号数: " + 0);
        System.out.println("各窗口处理数: ");
        //各窗口处理数据
        /*if(eatNum != null && eatNum.size() != 0){
            for (int i = 0; i < eatNum.size(); i++) {
                System.out.print("窗口" + i + "已处理；" + eatNum.get(i)+"个");
            }
        }else System.out.print("各窗口暂无处理数据");*/
    }

    public static int[] getNum(String str){//数字字符串转int数组
        String[] num_string = str.split("\\D");  // \D为正则表达式表示非数字
        String tmpStr = "";

        for(String s : num_string){
            tmpStr += s;
        }
        //将分离出的重新保存在新数组num中不要直接用num_string
        //在正则表达式对字符串进行选择时若前面的几个字符不符合要求但num_string数组中仍会存有其位置是空格
        String[] strNum = tmpStr.split("");
        int[] inte = new int[strNum.length];

        for(int i =0; i < strNum.length; i++){
            inte[i] = Integer.parseInt(strNum[i]);//将处理好的strNum数组中的数字存入int数组
        }

        return inte;
    }
}
