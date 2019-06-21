package com.dyx.java.concurrency.chapter03;


/**
 * Thread的各构造函数详解
 */
public class CreateThread {

    public static void main(String[] args) {

        /**
         * 构造函数：
         * Thread()
         * Thread(Runable target)
         * Thread(String name)
         * Thread(Runable target,String name)
         */
        Thread thread1 = new Thread();
        /**
         * 1.无参构造函数创建一个线程对象会指定一个默认的线程名：“Thread-”开头，尾数从零递增的线程名
         * 2.传入的target是否为null，当target为null时或者没有覆写run方法，（查看源代码）此时执行的是thread自带中的run方法，
         *   如果没有重写thread的run方法，则run方法是个空的方法，则此时该线程不做任何动作；
         *   若传入的target不为空，则执行的是target实例实现的run方法
         */
        thread1.start();
        System.out.println(thread1.getName());


        Thread thread2 = new Thread(() -> {
            //此时Thread.currentThread()指代当前线程，即thread2这个线程
            System.out.println("Runable run method with lambda..." + Thread.currentThread().getName());
        },"thread2");
        thread2.start();
    }
}
