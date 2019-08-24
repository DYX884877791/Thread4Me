package com.dyx.java.concurrency.chapter10;


import java.util.concurrent.TimeUnit;

/**
 * BadSynchronizedDemo
 * Synchronize锁的缺陷
 *
 * 1.无法控制由synchronize引起的阻塞时长
 * 2.由synchronize引起的阻塞不可被中断
 * @auther: mac
 * @since: 2019-07-06 11:17
 */
public class BadSynchronizedDemo {

    /**
     *
     * @param args
     */
    public static void main(String[] args){
        Runnable runable = new MyRunnable();

        Thread thread1 = new Thread(runable, "Thread1");
        Thread thread2 = new Thread(runable, "Thread2");

        /**
         * thread1先启动拿到了锁，睡眠1小时，在这个时间内，thread1不会释放锁，于是thread2一直在等待thread1释放锁而被阻塞
         * 且这种阻塞并且无法打断，主动调用thread2的interrupt方法也无法将thread2的阻塞给打断，thread2会一直阻塞。
         */
        thread1.start();
        // 为了保证thread1先启动
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.start();
        // 为了保证thread2也启动
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("thread2 state : %s  \n", thread2.getState());
        thread2.interrupt(); // main线程想要打断thread2的阻塞状态，但无法打断
        System.out.printf("thread2 interrupted : %s  \n", thread2.isInterrupted());

        System.out.printf("thread2 state : %s  \n", thread2.getState());

    }
}
class MyRunnable implements Runnable{

    @Override
    public void run() {
        synchronized (this) {
            try {
                System.out.println(Thread.currentThread().getName() + "我抢到了锁...");
                TimeUnit.HOURS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "我被打断了...");
                e.printStackTrace();
            }
        }
    }
}

