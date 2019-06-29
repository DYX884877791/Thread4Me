package com.dyx.java.concurrency.chapter07;

/**
 * SynchronizeDemo
 * 测试Synchronize关键字
 * @auther: mac
 * @since: 2019-06-29 16:00
 */
public class SynchronizeDemo {

    private final static Object LOCK = new Object();

    public static void main(String[] args) {

        Runnable runnable = () -> {
            /**
             * 同步代码块
             *
             * 多个线程运行到此，变成串行化运行
             */
            synchronized (LOCK) { //实际上是获取LOCK对象的锁标记，通过monitorenter和monitorexit指令
                try {
                    Thread.sleep(200_000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };


        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);
        thread1.start();
        thread2.start();
        thread3.start();

        /**
         * 可以使用jstack + 进程号，来查看堆栈信息
         */
    }
}
