package com.dyx.java.concurrency.chapter09;

/**
 * TestWhileSynchronized
 * 当在while(true)循环中调用同步代码块...
 *
 * @auther: mac
 * @since: 2019-07-05 02:42
 */
public class TestWhileSynchronized {

    /**
     * 长度为0的byte数组作为wait的对象锁
     */
    private final static byte[] BYTES = new byte[0];


    public static void main(String[] args) {

        TestWhileSynchronized testWhileSynchronized = new TestWhileSynchronized();


//        testSleep();
//        testWait();
        new Thread(() -> {
//            testWhileSynchronized.testWait();
            testWhileSynchronized.testWait1();
            }, "Wait线程") {
        }.start();

        testSleep();

        //此处只能是其他线程来调用这个方法，因为这个线程已经被阻塞了
//        notifyTheWait();
        new Thread(() -> {
            testWhileSynchronized.notifyTheWait();
            }, "notify线程") {
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
     * 否则一直等待...
     */
    public void testWait() {
        while (true) {// 同步代码块被多次进入
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
    }

    /**
     * 测试wait方法，wait方法需要有synchronized及对象锁，而且wait方法还需要别的线程调用notify或者notifyAll方法来唤醒等待的线程，
     * 否则一直等待...
     */
    public void testWait1() {
        synchronized (BYTES) {
            while (true) {
                try {
                    System.out.println("The time before wait is :" + System.currentTimeMillis());
                    BYTES.wait();//此处wait方法的对象是谁，之前必须对该对象进行加锁操作
                    System.out.println("The time after wait is :" + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *
     */
    public void notifyTheWait() {
//        while (true) {
            synchronized (BYTES) {
//                System.out.println("" + Thread.currentThread().getName() + "唤醒...");
                BYTES.notify();//调用notify方法之前也必须要拥有notify的目标锁对象，否则会抛出IllegalMonitorStateException异常
            }
//        }
    }
}
