package com.dyx.java.concurrency.chapter06;

/**
 * ThreadInterruptDemo
 * interrupt方法。。。
 *
 * @auther: mac
 * @since: 2019-06-22 15:03
 */
public class ThreadInterruptDemo2 {
    public static void main(String[] args) {
        Thread thread = new Thread(){
            @Override
            public void run() {
                while (true) {
                    if (this.isInterrupted()) {
                        break;
                    } else {
                        System.out.println("not yet interrupted");
                    }
                }
            }
        };

        thread.start();

        try { //main线程睡眠5秒钟以确保thread线程可以运行
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(thread.isInterrupted());
        thread.interrupt();
        System.out.println(thread.isInterrupted());
    }
}
