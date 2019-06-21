package com.dyx.java.concurrency.chapter04;

/**
 * 守护线程：当JVM中没有非守护线程运行时，JVM会退出
 */
public class DaemonThreadTest {

    public static void main(String[] args) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + "running");
                    Thread.sleep(50_000L);
                    System.out.println(Thread.currentThread().getName() + "done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        /**
         * 当没有这句代码时，得当main线程或者thread线程都在运行时，谁后结束线程，JVM才退出；
         * 当将thread线程设置为守护线程时，当main线程一结束，JVM直接退出而不管thread线程的此时的运行状态
         *
         * 适用场景：A到B之间维持一个长连接（连接需要发送心跳包进行保活），比如A向B发送一个心跳，（维持心跳的一个线程）与业务无关
         *         可以将该维持心跳的线程设置为守护线程，用于检查AB之间的连接健康状态，当主线程（业务线程）由于某种原因挂掉之后，
         *         该系统可以直接退出，不再占用资源，否则维持心跳的线程不是守护线程，该系统依旧在运行，但此时该线程无意义，
         *
         *         将守护线程用作于系统的辅助工具
         */
        thread.setDaemon(true);

        thread.start();

        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());
    }
}
