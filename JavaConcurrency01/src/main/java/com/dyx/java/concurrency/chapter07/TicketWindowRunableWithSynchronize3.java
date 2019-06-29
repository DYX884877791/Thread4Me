package com.dyx.java.concurrency.chapter07;

/**
 * TicketWindowRunableWithSynchronize2
 *
 * @auther: mac
 * @since: 2019-06-29 16:45
 */
public class TicketWindowRunableWithSynchronize3 implements Runnable {

    //每日最大的号码
    private static final int MAX = 500;

    //当前的号码
    private int index = 1;

    /**
     * 同步方法
     */
    @Override
    public void run() {
        while (true) {

            if (ticketIsSaleOut()) {
                break; // 当票已经卖完了，直接退出循环
            }
        }
    }

    /**
     * 取号的方法，对这个方法进行同步，在方法签名上添加synchronize关键字
     * 当号已经取完，直接返回，否则继续放号
     *
     * @return
     */
    private synchronized boolean ticketIsSaleOut() {
        if (index > MAX) {  // ........1
            return true;
        }
//        try {
//            Thread.sleep(5L);      // .........2
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println(Thread.currentThread().getName() + "当前的号码是：" + index++);   // ...........3
        return false;
    }
}
