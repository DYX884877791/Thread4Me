package com.dyx.java.concurrency.chapter06;

/**
 * ThreadInterruptDemo
 * interrupt方法。。。
 *
 * @auther: mac
 * @since: 2019-06-22 15:03
 */
public class ThreadInterruptDemo3 {
    public static void main(String[] args) {
        Thread thread = new Thread(){
            @Override
            public void run() {
            while (true) {
                try {
                    System.out.println("This thread is running。。。");
                    System.out.println("This thread is interrupted??? >>> " + this.isInterrupted());
                    Thread.sleep(5_000L);//sleep方法会重置中断状态为false
                } catch (InterruptedException e) {
                    System.out.println("This thread is interrupted??? === " + this.isInterrupted());
                    System.out.println("sleep 方法被中断了");
                    e.printStackTrace();
                    //**
                    // 观察异常信息：sleep interrupted，但thread线程还是依旧在运行，思考一下：为什么下一次打印没有异常信息（捕获到异常）呢？
                    // 因为只是thread的第一次sleep被中断，并且异常被捕获到也不会让该线程停止运行，但整个线程体还是死循环，所以还会继续运行，
                    // 只是后面的sleep方法不会执行，还是会输出开始两行信息
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
