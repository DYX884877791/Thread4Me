package com.dyx.java.concurrency.chapter06;

/**
 * ThreadInterruptDemo
 * interrupt方法。。。
 *
 * @auther: mac
 * @since: 2019-06-22 15:03
 */
public class ThreadInterruptDemo {
    public static void main(String[] args) {
        Thread thread = new Thread(){
            @Override
            public void run() {
                while (true) {
//                    System.out.println("thread is running。。。");
                }
            }
        };

        thread.start();

        try { //main线程睡眠5秒钟，从而使得thread线程获得cpu调度
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(thread.isInterrupted());//false
        /**
         *   main线程调用thread线程的interrupt方法，中断thread线程，并不会停止thread线程，
         *   只是改变thread的中断状态而已，从false->true，然后我们可以根据其不同的中断状态做出相应的处理，见Demo2
         */
        thread.interrupt();
        System.out.println(thread.isInterrupted());//true
    }
}
