package com.dyx.java.concurrency.chapter06;

/**
 * ThreadInterruptDemo
 * interrupt中断wait方法。。。
 *
 * @auther: mac
 * @since: 2019-06-22 15:03
 */
public class ThreadInterruptDemo4 {

    private static final Object MONITOR = new Object();

    public static void main(String[] args) {
        Thread thread = new Thread(){
            @Override
            public void run() {
                while (true) {
                    synchronized (MONITOR) {
                        try {
                            System.out.println("This thread is running。。。");
                            System.out.println("This thread is interrupted??? >>> " + this.isInterrupted());
                            MONITOR.wait(5_000L);//wait方法会重置中断状态为false
                        } catch (InterruptedException e) {
                            System.out.println("wait 方法被中断了");
                            e.printStackTrace();//观察异常信息：sleep interrupted，但thread线程还是依旧在运行
                        }
                    }

                }
            }
        };

        thread.start();

        try { //main线程睡眠5秒钟以确保thread线程可以运行
            Thread.sleep(1_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * interrupt方法是用于中断线程的，调用该方法的线程的状态将被置为"中断"状态。
         *
         * 注意：调用interrupt()方法仅仅是在当前线程中打了一个停止的标记，并不是真的停止线程，
         *      需要用户自己去监视线程的状态为并做处理。这一方法实际上完成的是，在线程受到阻塞时抛出一个中断信号，
         *      这样线程就得以退出阻塞的状态。更确切的说，如果线程被Object.wait,Thread.join和Thread.sleep三种方法之一阻塞，
         *      此时又被调用了interrupt方法，那么，它将接收到一个中断异常（InterruptedException），从而提早地终结被阻塞状态。 
         */
        System.out.println(thread.isInterrupted());
        thread.interrupt();
        System.out.println(thread.isInterrupted());
    }
}
