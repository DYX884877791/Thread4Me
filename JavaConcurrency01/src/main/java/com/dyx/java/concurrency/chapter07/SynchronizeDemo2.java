package com.dyx.java.concurrency.chapter07;

/**
 * SynchronizeDemo
 * 测试Synchronize关键字：this锁
 * @auther: mac
 * @since: 2019-06-29 16:00
 */
public class SynchronizeDemo2 {

    public static void main(String[] args) {

        ThisLock thisLock = new ThisLock();

        /**
         * 当多个线程去调用同一个类的多个同步实例方法时，且这些同步方法争夺的是同一个锁对象时，如分别调用method1和method2，
         * 且method1和method2的锁是同一个对象（thisLock对象），所以会观察到thread1和thread2有明显的先后执行顺序，
         * 因为前一个线程已经先抢到了这个对象的锁，得等该线程执行完毕，释放锁资源，后一个线程才会抢到锁，然后执行
         */
        new Thread("Thread1") {
            @Override
            public void run() {
                thisLock.method1();
            }
        }.start();

        new Thread("Thread2") {
            @Override
            public void run() {
                thisLock.method2();
            }
        }.start();

        new Thread("Thread3") {
            @Override
            public void run() {
                thisLock.method3();
            }
        }.start();

        // 当this不是指向同一个锁对象时：
        new Thread("Thread4") {
            @Override
            public void run() {
                new ThisLock().method2();
            }
        }.start();

        // 当this不是指向同一个锁对象时：
        new Thread("Thread5") {
            @Override
            public void run() {
                new ThisLock().method3();
            }
        }.start();

    }
}

/**
 * 测试this锁
 */
class ThisLock {

    /**
     * method1是实例同步方法，该同步方法抢夺的是this锁，即ThisLock实例对象的锁
     */
    public synchronized void method1() {
        System.out.println("Thread [ " + Thread.currentThread().getName() + " ] is executing method1...");
        System.out.println("Method1 is locked by " + this);
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * method2是实例同步方法，该同步方法抢夺的是this锁，即ThisLock实例对象的锁
     */
    public synchronized void method2() {
        System.out.println("Thread [ " + Thread.currentThread().getName() + " ] is executing method2...");
        System.out.println("Method2 is locked by " + this);
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * method3方法无锁，但方法中的同步代码块抢夺的也是this锁，即ThisLock实例对象的锁，等价于上面两种写法，只是锁的范围更小
     */
    public void method3() {
        synchronized (this) {
            System.out.println("Thread [ " + Thread.currentThread().getName() + " ] is executing method3...");
            System.out.println("Method3 is locked by " + this);
            try {
                Thread.sleep(10_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
