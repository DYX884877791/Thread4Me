package com.dyx.java.concurrency.chapter02;

public class TicketWindowRunable implements Runnable {

    //每日最大的号码
    private static final int MAX = 5000;

    //当前的号码
    private int index = 1;

    @Override
    public void run() {
        while(index <= MAX) {
            System.out.println(Thread.currentThread().getName() + "当前的号码是：" + index++);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
