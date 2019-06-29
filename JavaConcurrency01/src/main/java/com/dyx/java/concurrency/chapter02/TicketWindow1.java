package com.dyx.java.concurrency.chapter02;

/**
 * 模拟银行叫号机程序
 *
 * TicketWindow：柜台
 * Bank：银行
 */
public class TicketWindow1 extends Thread {

    //柜台名字
    private final String name;

    //每日最大的号码
    private static final int MAX = 500;

    //当前的号码，将index变量设置为static即各对象共享的
    /**
     * 但此时的缺点：index的生命周期很长，且index较大时，容易造成数据的安全问题
     */
    private static int index = 1;

    public TicketWindow1(String name) {
        this.name = name;
    }

    public void run() {
        while (index <= MAX) {
            System.out.println("柜台：[" + name + "]当前的号码是：" + index++);
            /**
             * 当加入线程休眠逻辑时，数据的安全问题更加显而易见
             */
//            try {
//                Thread.sleep(1000L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }
}
