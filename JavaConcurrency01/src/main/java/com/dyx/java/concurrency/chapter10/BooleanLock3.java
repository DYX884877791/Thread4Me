package com.dyx.java.concurrency.chapter10;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * BooleanLock  与chapter09的CaptureService比较
 * Lock锁的实现------BooleanLock布尔显式锁，根据布尔值判断，是否被锁住
 *
 * @auther: mac
 * @since: 2019-07-06 10:13
 */
public class BooleanLock3 implements Lock {

    /**
     * false 代表当前该锁没有被任何线程获得或者已经释放，
     * true  代表该锁已经被某一个线程获得
     */
    private boolean isLocked;

    // 记录被阻塞的线程的集合，存储哪些线程在获取当前锁对象时进入了阻塞状态
    private Collection<Thread> blockedThreadsCollection = new ArrayList<>();

    // BooleanLock2新增：添加获取到锁的线程标记，解决main线程调用unlock方法，提前释放锁的问题
    private Thread currentLockThread;

    // 初始化时，该lock没有被获取，即标记为false，
    public BooleanLock3() {
        this.isLocked = false;
    }

    /**
     * 获取锁的方法   synchronize... while(...) wait...
     *  如果成功取到了锁，会将isLocked从false置为true；如果刚开始isLocked已经是true了，说明锁已经被获取了，需要等待，直到锁被释放，
     *  isLocked变为false，等待的线程被唤醒（notifyAll）；唤醒的线程中抢到锁的线程会再次判断isLocked，如果isLocked为false，则该
     *  线程可以获取锁，将isLocked从false置为true，lock方法结束，释放锁，然后唤醒的线程再次抢锁，但是判断isLocked为true，再次等待。
     *
     *
     *
     * @throws InterruptedException
     */
    @Override
    public void lock() throws InterruptedException {

        // 多个线程同时来抢锁
        System.out.println("[" + Thread.currentThread().getName() + "] is try obtain lock");

        // 判断当前线程是否获取到当前锁，肯定始终为true
//        boolean isHoldsLock = Thread.holdsLock(this);
//        if (isHoldsLock) {
//            System.out.println("[" + Thread.currentThread().getName() + "] hold " + this + " Lock Monitor");
//        }

        synchronized (this) {
            /**
             * 当进入到同步代码块时，取到锁时，会判断isLocked，如果为true，说明该锁已经被某一个线程所持有，则当前线程一直等待，
             * 并将当前线程加入到阻塞集合中
             *
             * 一开始犯的错误：多线程进入synchronize方法时，变为串行，不管哪个线程谁抢到了锁，则isLocked一定为true，所以一直被wait
             * 原因：没有考虑到unlock方法，unlock方法会将wait的线程唤醒，并将isLocked置为false，而唤醒的线程会直接执行while语句，
             * 不会再次去获取锁了。
             *
             * 需要注意：此时设置的isLocked与真正的锁被释放拥有与否并无关系
             */
            while (isLocked) { // 而isLocked判断的是锁对象被某一个线程所持有
                System.out.println(this + " is locked," + Thread.currentThread().getName() + " will wait.");
                blockedThreadsCollection.add(Thread.currentThread());
                this.wait();
            }
            /**
             * 当执行到下面这里时，initValue只能为false，
             */
            System.out.println("[" + Thread.currentThread().getName() + "] have obtained the lock monitor...");
            blockedThreadsCollection.remove(Thread.currentThread());
            this.isLocked = true;
            // BooleanLock2新增：获取到lock之后，将标记线程设置为当前线程，比如T2线程获取到lock之后将标记为"T2"，而unlock时检查标记是否是"T2"
            this.currentLockThread = Thread.currentThread();

        }
        // 需要注意：此时已经跳出synchronize同步代码块，即锁被释放，没有被哪个线程拥有，但此时isLocked为true，所以此时isLocked并不能真正代表锁的真实状态

    }

    /**
     *
     * @param timeout
     * @throws InterruptedException
     * @throws TimeOutException
     */
    @Override
    public void lock(long timeout) throws InterruptedException, TimeOutException {
        // 多个线程同时来抢锁
        System.out.println("[" + Thread.currentThread().getName() + "] is try obtain lock");

        // 如果timeout<=0
        if (timeout <= 0) {
            lock();
        } else {
            // 还剩多少时间，最开始是timout，后面会渐渐减少至0
            long remainTime = timeout;
            // 标记要结束的时间
            long endTime = System.currentTimeMillis() + timeout;
            synchronized (this) {
                while (isLocked) { //如果锁对象已经被某一个线程所持有则wait，wait多长时间呢，timeout，在timeout期间内，如果有其他线程notify就notify，否则自己主动唤醒
                    if (remainTime <= 0) { // 如果剩余时间小于等于0，直接抛超时异常
                        throw new TimeOutException("Time out");
                    }
                    System.out.println(this + " is locked," + Thread.currentThread().getName() + " will wait.");
                    blockedThreadsCollection.add(Thread.currentThread());
                    /**
                     * 这里两种情况：
                     * 1.线程已经wait到时间了，自己主动唤醒了，这个时候已经过去了timeout的时间（到了标记结束时间），则剩余的时间为0
                     * 2.线程是被其他线程的notify/notifyAll唤醒的，未到标记结束时间，则当前被唤醒的时间一定小于标记结束时间，则剩余
                     *      时间为标记结束时间减当前被唤醒的时间
                     */
                    this.wait(remainTime); //唤醒之后会重新进入while循环
                    // 唤醒之后更新一下剩余的时间，使用被唤醒时的时间减
                    remainTime = endTime - System.currentTimeMillis();
                    System.out.println("[" + Thread.currentThread().getName() + "] remainTime is " + remainTime);
                }

                // 如果抢到了锁
                /**
                 * 当执行到下面这里时，initValue只能为false，
                 */
                System.out.println("[" + Thread.currentThread().getName() + "] have obtained the lock monitor...");
                this.isLocked = true;
                // BooleanLock2新增：获取到lock之后，将标记线程设置为当前线程，比如T2线程获取到lock之后将标记为"T2"，而unlock时检查标记是否是"T2"
                currentLockThread = Thread.currentThread();
                blockedThreadsCollection.remove(currentLockThread);
            }

        }
    }

    /**
     * 释放锁，当只有在获取到锁的情况下，才能释放锁，
     */
    @Override
    public synchronized void unlock() {
        // BooleanLock2新增：unlock时，检查一下标记
        if (this.currentLockThread == Thread.currentThread()) {
            this.isLocked = false;
            System.out.println("["+ Thread.currentThread().getName() + "] release the lock monitor...");
            this.notifyAll();// 释放锁之后，将其他被lock monitor阻塞住的线程唤醒
        }
    }

    /**
     * 获取被Lock锁阻塞住的线程：
     *      在当前线程获取blockedThreadsCollection的时候，其他线程可能在修改该集合
     * @return
     */
    @Override
    public Collection<Thread> getBlockedThreads() {

        // 此时直接返回的话，该blockedThreadsCollection可能正在被其他线程add、remove，不安全...，此时为何不用synchronize？
//        return blockedThreadsCollection;

        // unmodifiableCollection表示不可修改，防止该集合被强行主动修改，出现错误
        return Collections.unmodifiableCollection(blockedThreadsCollection);
    }

    @Override
    public int getBlockedThreadSize() {
        return getBlockedThreads().size();
    }
}
