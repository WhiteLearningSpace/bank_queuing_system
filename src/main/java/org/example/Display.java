
package org.example;

import com.display.IDisplay;

public class Display implements IDisplay {

    String number;//当前取号

    String windowName;//办理窗口名称

    String queue;//排队信息

    @Override
    public void Show() {
        System.out.println("----------显示大屏排队信息----------");
        System.out.println(queue);
    }

    //菜单
    public void menu(){
        System.out.println("----------欢迎使用银行取号系统----------\n1.取号\n0.退出");
    }

    //取号，姓名：张三，您的取号是：12号
    public void GetNumber(){
        System.out.println("您的取号是："+ number +"号");
    }

    //叫号，请12号张三到 窗口1 办理业务
    public void CallNumber(){
        System.out.println("请"+ number +"号到 "+windowName+" 办理业务");
    }

    //过号，12号张三，已过号
    public void OverNumber(){
        System.out.println(number +"号，已过号");
    }

    //消费，正在为12号张三客户办理业务
    /*public void EatNumber(){
        System.out.println("正在为"+ number +"号客户办理业务");
    }*/

    //消费完成提示
    public void EatSuccess(){
        System.out.println(number +"号，您办理的业务已完成");
    }
}
