package com.dyx.java.concurrency.chapter04;

import java.util.Optional;

/**
 * Thread的一些简单API
 */
public class ThreadSimpleApi {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            Optional.of("Hello").ifPresent(System.out::println);
            try {
                Thread.sleep(2_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"test-thread");
        thread.start();
        System.out.println(thread.getName());
        System.out.println(thread.getThreadGroup());
        System.out.println(thread.getState());

        /**
         *Thread ID为线程的id，为++threadSeqNumber，而且父线程main占一个，说明JVM启动时已经开启了9个线程，通过jconsole工具查看
         */
        System.out.println(Thread.currentThread().getName() + " tid is " +Thread.currentThread().getId());
        System.out.println(thread.getId());
        System.out.println(thread.getStackTrace());
    }

}
