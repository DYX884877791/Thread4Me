package com.dyx.java.concurrency.chapter05;

import java.util.stream.IntStream;

/**
 * Thread的join方法的测试类，join方法重载
 *
 * @auther: mac
 * @since: 2019-06-22 00:43
 */
public class ThreadJoinDemo3 {
    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            try {
                System.out.println("thread1 is running...");
                //使用睡眠模拟线程执行所需要的时间
                Thread.sleep(2_000L);
                System.out.println("thread1 is done...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();

        //此处让threa1线程先执行1000毫秒（thread1还没有执行完），然后main线程就和thread1线程抢cpu来执行
        thread1.join(1_000L);

        //如果让join参数大一些,让thread1线程先运行5秒钟，此时thread1已经执行完了，所以等到5秒钟（大体）后main线程才开始运行
//        thread1.join(5_000L);
        //更加精确地控制join时间,前一个参数是毫秒，后一个参数是纳秒
//        thread1.join(5_000L, 1_000);



        System.out.println("All of Tasks finished...And main thread start...");
        IntStream.range(0, 500)
                .forEach(i -> System.out.println(Thread.currentThread().getName() + "->" + i));


        //如果加上当前线程join的话，会发生什么？
        /**
         * main方法不会退出，一直处于运行状态。。。
         * 原因：当前线程是main线程，但join方法的作用是让main线程先运行完，然后再运行main线程，也就是main线程在等待main线程结束，
         *      但main线程一直在等，所以也不会结束。。。所以一直挂起
         * 把这句话放在thread1线程体中，thread1线程也会一直挂起，见Demo4
         */
//        Thread.currentThread().join();
    }
}
