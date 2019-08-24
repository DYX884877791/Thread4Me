package com.dyx.java.concurrency.chapter12;

import java.util.Arrays;

/**
 * 线程组详解，在JDK1.5之前用的比较多，因为此时还没有线程池技术
 *
 * 在JVM启动以后，有一个ThreadGroup叫根ThreadGroup，里面可以包含其他的ThreadGroup或者Thread，ThreadGroup里的线程可以访问其自身的ThreadGroup的一些信息，
 * 但是没有权限访问其父ThreadGroup或者其他ThreadGroup的信息，什么信息呢，下面即将介绍
 */
public class ThreadGroupDemo1 {

    public static void main(String[] args) {
        // 使用名字，如果未指定其父线程组是谁，则设置为当前线程的线程组
        ThreadGroup tg1 = new ThreadGroup("tg1");
        Thread t1 = new Thread(tg1, "t1") {

            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println(Thread.currentThread().getThreadGroup());
                        System.out.println(Thread.currentThread().getThreadGroup().getParent());
                        // 但是此处是能访问父线程组的信息的？
                        System.out.println(Thread.currentThread().getThreadGroup().getParent().activeCount());
                        Thread.sleep(10_000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t1.start();

        // 使用父线程组和名字
        ThreadGroup tg2 = new ThreadGroup(tg1,"tg2");
        Thread t2 = new Thread(tg2, "t2") {
            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println(Thread.currentThread().getThreadGroup());
                        System.out.println(Thread.currentThread().getThreadGroup().getParent());
                        // 但是此处是能访问父线程组的信息的？
                        System.out.println(Thread.currentThread().getThreadGroup().getParent().activeCount());
                        Thread.sleep(10_000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

//        t2.start();

        // 当处于同一个线程组中，两个线程能否访问信息
        ThreadGroup tg3 = new ThreadGroup("tg3");
        Thread t3 = new Thread(tg3, "t3") {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread[] threads = new Thread[tg1.activeCount()];
                        tg1.enumerate(threads);

                        Arrays.asList(threads).forEach(System.out::println);
                        Thread.sleep(10_000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t3.start();
    }
}
