package com.dyx.java.concurrency.chapter06;

/**
 * ThreadInterruptDemo
 * interrupt方法。。。
 * interrupted()测试的是"当前的线程"的中断状态。而isInterrupted()测试的是调用该方法的对象所表示的线程。
 * @auther: mac
 * @since: 2019-06-22 15:03
 */
public class ThreadInterruptDemo6 {

    public static void main(String[] args) {
        MyThread thread1 = new MyThread();
        thread1.start();
        try {
            Thread.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread1.interrupt();
        //Thread.currentThread().interrupt();
        System.out.println("thread1是否停止1？= " + thread1.interrupted());//这里测试其实是当前的线程（main线程）的中断状态
        System.out.println("thread1是否停止2？= " + thread1.interrupted());//false main线程没有被中断!!!




        MyThread thread2 = new MyThread();
        thread2.start();
        try {
            Thread.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.interrupt();
        System.out.println("thread2是否停止1？= " + thread2.isInterrupted());//true
        System.out.println("thread2是否停止1？= " + thread2.isInterrupted());//true

    }
}

class MyThread extends Thread {
    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 500; i++) {
            System.out.println("i=" + (i + 1));
        }
    }
}