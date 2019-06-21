package com.dyx.java.concurrency.chapter03;

import java.util.Arrays;

/**
 * 线程与线程组
 */
public class CreateThread2 {

    public static void main(String[] args) {

        /**
         * ThreadGroup g, 线程所在的线程组
         * Runnable target, runable实例
         * String name, 线程的名字
         * long stackSize, 栈的大小
         * AccessControlContext acc,
         * boolean inheritThreadLocals
         */
        //当运行start方法时，有两个线程，一个是当前的线程Thread.currentThread，此处为main线程，另一个是要开启的那个线程
        //谁运行了start()方法，谁就是父线程，开启的线程是子线程,(这里有点绕，这里start方法实际上是main线程调用的)
        //当没有指定线程的组时，该线程与其父线程同一个组，为什么要有ThreadGroup这个东东？
        Thread thread1 = new Thread(){
            public void run() {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread1.start();
        System.out.println("thread1 threadgroup===>"+thread1.getThreadGroup());

        System.out.println(Thread.currentThread());
        System.out.println("current thread threadgroup===>"+Thread.currentThread().getThreadGroup());


        /**
         * threadGroup的一些操作
         */
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        //查看该线程组中有多少个活跃的线程
        System.out.println(threadGroup.activeCount());

        System.out.println("==============");
        //将该线程组中的各活跃线程枚举出来
        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);

        for (Thread thread : threads) {
            System.out.println(thread);
            System.out.println(thread.isDaemon());
        }
        System.out.println("==============");
        Arrays.asList(threads).forEach(System.out::println);
    }
}
