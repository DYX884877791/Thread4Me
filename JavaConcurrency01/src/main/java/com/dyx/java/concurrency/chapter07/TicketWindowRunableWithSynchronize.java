package com.dyx.java.concurrency.chapter07;

public class TicketWindowRunableWithSynchronize implements Runnable {

    //每日最大的号码
    private static final int MAX = 500;

    //当前的号码
    private int index = 1;

    private final static Object MONITOR = new Object();

    @Override
    public void run() {
        while (true) {
            /**
             * 通过命令：javap -c 类名.class 观察其JVM指令
             *
             * 多个线程运行到此处，但最终只有一个线程才能抢到MONITOR对象的锁，对应的JVM指令为monitorenter，其他线程进入到该对象
             * 的同步队列里面，等待下一次的抢夺锁资源，当抢到锁的线程的方法执行完了，会释放锁资源，不再拥有锁，对应的JVM指令
             * 为monitorexit，随后其他线程来抢夺锁，谁抢到了锁就会执行它的代码逻辑
             */
            synchronized (MONITOR) {

                if (index > MAX) {     //.........1
                    break;
                }
                try {
                    Thread.sleep(5L);      //.........2
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "当前的号码是：" + index++);   // ...........3
            }
        }
    }
}

/**
 * synchronize关键字：
 *
 */
