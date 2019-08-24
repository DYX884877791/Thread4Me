package com.dyx.java.concurrency.chapter10;

import java.util.Collection;

/**
 * Lock
 * 使其具备synchronized关键字所有的功能，同时又具备可中断和lock超时的功能
 *
 * 自定义一个显示锁，拥有lock和unlock方法
 * @auther: mac
 * @since: 2019-07-06 09:59
 */
public interface Lock {

    class TimeOutException extends Exception {
        public TimeOutException() {
            super();
        }
        public TimeOutException(String message) {
            super(message);
        }
    }

    /**
     * 获取锁的方法
     * lock方法跟synchronized同步非常类似，除非获取到了锁，否则就会一直阻塞，区别在于此种方法可以中断，抛出异常
     * @throws InterruptedException
     */
    void lock() throws InterruptedException;

    // 获取锁的方法，并设置超时时间，该过程中允许被中断，当在超时时间外还没有获取到锁，抛出超时异常
    void lock(long timeout) throws InterruptedException,TimeOutException;

    // 释放锁
    void unlock();

    // 获取这个锁中所阻塞住的线程
    Collection<Thread> getBlockedThreads();

    // 获取被阻塞的线程数量
    int getBlockedThreadSize();
}
