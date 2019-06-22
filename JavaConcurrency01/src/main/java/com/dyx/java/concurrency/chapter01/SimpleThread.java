package com.dyx.java.concurrency.chapter01;

/**
 * 新建一个线程，并使用jconsole工具观察
 */
public class SimpleThread {

    public static void main(String[] args) {

        //新建一个custom-thread线程
        Thread t1 = new Thread("custom-thread") {

            @Override
            public void run(){
                for (int i = 0;i < 2000;i++) {
                    System.out.println("Task 1 ==> " + i);
                    try {
                        /**
                         * 当前“正在执行的线程”休眠（暂停执行）。这个“正在执行的线程”是指 this.currentThread() 返回的线程。
                         * 这里指的是t1这个线程，t1.sleep、this.sleep、this.currentThread().sleep、Thread.currentThread().sleep等价
                         */
                        long timeBeforeSleep = System.currentTimeMillis();
                        Thread.currentThread().sleep(1000L);
                        System.out.println(System.currentTimeMillis() - timeBeforeSleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread.State state = t1.getState();
        System.out.println(state);
        t1.start();
        System.out.println(state);



        //主线程的执行单元
        for (int i = 0;i < 2000;i++) {
            System.out.println("Task 2 ==> " + i);
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
