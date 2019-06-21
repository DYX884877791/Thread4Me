package com.dyx.java.concurrency.chapter05;

import java.util.stream.IntStream;

/**
 * Thread的join方法的测试类，join方法重载
 *
 * @auther: mac
 * @since: 2019-06-22 00:43
 */
public class ThreadJoinDemo4 {
    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            try {
                System.out.println("thread1 is running...");

                /**
                 * thread1线程体中join，则thread1线程一直在运行，不会打印thread1 is done...
                 */
                Thread.currentThread().join();
                //使用睡眠模拟线程执行所需要的时间
                Thread.sleep(2_000L);
                System.out.println("thread1 is done...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();

        //此时如果这里使用join方法不带参数，则main线程一直等待thread1线程结束，而thread1线程也不会结束，所以main线程也不会运行了
        thread1.join(1_000L);

        System.out.println("All of Tasks finished...And main thread start...");
        IntStream.range(0, 500)
                .forEach(i -> System.out.println(Thread.currentThread().getName() + "->" + i));

    }
}
