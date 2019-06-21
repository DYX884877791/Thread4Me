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
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread.State state = t1.getState();
        t1.start();


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
