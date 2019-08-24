package com.dyx.java.concurrency.chapter09;

/**
 * TheDifferenceOfSleepAndWait
 *
 * sleep方法与wait方法的不同:
 *      1.sleep方法是属于Thread类的方法，wait方法是属于Object类的方法
 *      2.sleep方法不会让当前线程释放掉对象锁（如果当前线程拥有锁的话），而wait方法会让当前线程释放掉对象锁，并让当前线程加入到锁对象的
 *        等待队列中
 *      3.使用sleep方法并不需要前面有synchronized方法，而wait方法需要
 *      4.sleep方法并不需要去唤醒它，而wait方法需要去唤醒它，（wait(long timeout)方法除外）
 *
 *      5.sleep表示休眠并不会释放锁，所以如果有多个线程争夺锁并sleep(synchronize...sleep...)，则只有一个线程会进入到synchronize中，
 *      然后sleep...，其他线程会被阻塞；而wait方法会释放锁，如果有多个线程争夺锁并wait(synchronize...wait...)，则可能会有多个线程
 *      进入到synchronize语句中，随后等待...，其他线程不会被阻塞。
 *
 * @auther: mac
 * @since: 2019-07-03 22:47
 */
public class TheDifferenceOfSleepAndWait {

    /**
     * 长度为0的byte数组作为wait的对象锁
     */
    private final static byte[] BYTES = new byte[0];

    public static void main(String[] args) {
//        testSleep();
//        testWait();
        new Thread("Wait线程"){
            @Override
            public void run() {

                testWait();
            }
        }.start();

        testSleep();

        //此处只能是其他线程来调用这个方法，因为这个线程已经被阻塞了
//        notifyTheWait();
        new Thread("notify线程"){
            @Override
            public void run() {

                notifyTheWait();
            }
        }.start();
    }

    /**
     * 测试sleep方法
     */
    public static void testSleep() {
        try {
            System.out.println("The time before sleep is :" + System.currentTimeMillis());
            Thread.sleep(5_000L);//sleep方法为Thread类的(静态)方法，且前面并不需要有synchronized方法
            System.out.println("The time after sleep is :" + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试wait方法，wait方法需要有synchronized及对象锁，而且wait方法还需要别的线程调用notify或者notifyAll方法来唤醒等待的线程，
     *              否则一直等待...
     */
    public static void testWait() {
        synchronized (BYTES) {
            try {
                System.out.println("The time before wait is :" + System.currentTimeMillis());
                BYTES.wait();//此处wait方法的对象是谁，之前必须对该对象进行加锁操作
                System.out.println("The time after wait is :" + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     */
    public static void notifyTheWait() {
        synchronized (BYTES) {
            System.out.println("" + Thread.currentThread().getName() + "唤醒...");
            BYTES.notify();//调用notify方法之前也必须要拥有notify的目标锁对象，否则会抛出IllegalMonitorStateException异常
        }
    }
}
