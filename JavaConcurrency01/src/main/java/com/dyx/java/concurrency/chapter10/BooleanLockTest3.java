package com.dyx.java.concurrency.chapter10;


import java.util.stream.Stream;

/**
 * BooleanLockTest
 * 测试BooleanLock
 * @auther: mac
 * @since: 2019-07-06 10:52
 */
public class BooleanLockTest3 {

    public static void main(String[] args) {
        //
        final BooleanLock3 booleanLock3 = new BooleanLock3();
        Stream.of("T1","T2","T3","T4","T5").forEach(name -> {
            new Thread(() -> {
                try {
                    /**
                     * 某个线程先抢到booleanLock锁，最开始判断isLocked为false，不会进入while循环，lock方法一执行完毕，
                     * booleanLock被释放，isLocked被置为true，其他线程获取到这个锁，但是判断isLocked为true，进入while循环，
                     * 加入阻塞集合中并wait...，只有当unlock()方法被调用，notifyAll并将isLocked置为false，wait的线程被唤醒，
                     * 争夺锁，继续执行while，while判断为false，不再循环，又将isLocked置为true，即该线程获得了锁的标志，并移除
                     * 阻塞集合中的该线程，lock执行结束，释放锁，另外线程抢到锁，还是会判断isLocked为true，直到上一个线程调用
                     * unlock方法...
                     */
                    booleanLock3.lock(50_000L);

                    work();// 抢到锁之后，开始工作
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Lock.TimeOutException e) {
                    System.out.println("[" + Thread.currentThread().getName() + "] Time out....");
                } finally {
                    booleanLock3.unlock();// 工作完成之后，自动释放锁，只有这里释放之后，isLocked变为false，才真正视为锁被释放
                }
            },name).start();
        });



    }

    /**
     * 使用睡眠模拟正在工作...
     */
    public static void work() {
        System.out.println("["+ Thread.currentThread().getName() + "] is working...");
        try {
            Thread.sleep(20_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
