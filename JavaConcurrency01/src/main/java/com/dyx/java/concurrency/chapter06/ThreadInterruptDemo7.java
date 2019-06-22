package com.dyx.java.concurrency.chapter06;

/**
 * ThreadInterruptDemo
 * interrupt方法。。。
 * interrupt方法如何中断join方法
 * @auther: mac
 * @since: 2019-06-22 15:03
 */
public class ThreadInterruptDemo7 {

    public static void main(String[] args) {

        Thread thread1 = new Thread(() -> {
           while (true) {
           }
        });
        thread1.start();

        Thread main = Thread.currentThread();

        Thread thread2 = new Thread( () -> {
            try {
                System.out.println("thread2 running...");
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            /**
             * 使用thread1.interrupt方法并不会中断join方法，因为join方法的调用者其实是main线程，
             * 所以thread2线程中断的对象应该是main线程，而不应该是thread1线程
             *
             * join方法底层调用的是wait方法，所以与Demo4的异常信息类似
             */
//            thread1.interrupt();
            main.interrupt();

            System.out.println("thread2 interrupt other thread...");

        });

        thread2.start();

        try {
            thread1.join();//假如要让thread2线程来中断此处的join方法，thread2线程应该中断谁？
        } catch (InterruptedException e) {
            System.out.println("join is interrupted...");
            e.printStackTrace();
        }
    }
}
