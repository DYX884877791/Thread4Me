package com.dyx.java.concurrency.chapter05;

import java.util.stream.IntStream;

/**
 * Thread的join方法的测试类
 *
 * @auther: mac
 * @since: 2019-06-22 00:43
 */
public class ThreadJoinDemo2 {
    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            IntStream.range(0, 1000)
                    .forEach(i -> System.out.println(Thread.currentThread().getName() + "->" + i));
        });

        Thread thread2 = new Thread(() -> {
            IntStream.range(0, 1000)
                    .forEach(i -> System.out.println(Thread.currentThread().getName() + "->" + i));
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        /**
         * 这里会先让thread1和thread2线程先执行完，然后再执行main线程，而thread1和thread2线程之间是可以交替执行的，
         * 如果在thread2.start()方法之前调用thread1.join()方法，则会thread1线程先运行完之后，thread2线程才开始启动
         */

        System.out.println("All of Tasks finished...And main thread start...");
        IntStream.range(0, 1000)
                .forEach(i -> System.out.println(Thread.currentThread().getName() + "->" + i));

    }
}
