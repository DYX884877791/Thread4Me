package com.dyx.java.concurrency.chapter02;

/**
 * 模拟银行叫号机程序
 *
 * TicketWindow：柜台
 * Bank：银行
 */
public class TicketWindow extends Thread {

    //柜台名字
    private final String name;

    //每日最大的号码
    private static final int MAX = 50;

    //当前的号码
    private int index = 1;

    public TicketWindow(String name) {
        this.name = name;
    }

    public void run() {
        while (index <= MAX) {
            System.out.println("柜台：" + name + "当前的号码是：" + index++);
        }
    }
}
