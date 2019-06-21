package com.dyx.java.concurrency.chapter05;

import java.util.stream.IntStream;

/**
 * Thread的join方法的测试类
 *
 * @auther: mac
 * @since: 2019-06-22 00:43
 */
public class ThreadJoinDemo {
    public static void main(String[] args) {

        Thread thread = new Thread(() -> {
            IntStream.range(0, 1000)
                    .forEach(i -> System.out.println(Thread.currentThread().getName() + "->" + i));
        });
        thread.start();

        /**
         * 此时（先将join方法注释掉）代码中thread和main线程会交替输出信息，
         * 那么如何让thread先输出完，再输出main的信息，那么只需要调用thread的join方法即可
         *
         * join的作用是：
         *       当我们调用某个线程的join方法时，这个方法会挂起调用线程，直到被调用线程结束执行，调用线程才会继续执行。
         *       此处是main线程调用thread线程的join方法，所以main线程会被挂起，thread线程先执行完，然后再执行main线程
         *       此外，join方法还有重载的方法，用于指定被挂起的时间，在此时间内先执行被调用的线程直到时间结束
         *
         *
         */
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        IntStream.range(0, 1000)
                .forEach(i -> System.out.println(Thread.currentThread().getName() + "->" + i));

    }
}
