package com.dyx.java.concurrency.chapter04;

/**
 * 心跳模拟
 */
public class DaemonThreadTest2 {
    public static void main(String[] args) {
        //业务线程
        Thread thread = new Thread(() -> {

            //维持心跳的线程
            Thread innerThread = new Thread(() -> {

                /**
                 * 心跳线程使用睡眠来模拟
                 */
//                System.out.println("HeartBeat Thread Running...");
//                try {
//                    Thread.sleep(100_000L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                System.out.println("HeartBeat Thread Still Running...");

                /**
                 * 如果心跳线程使用while(true)来模拟,此时如果心跳线程是守护线程的话,在业务线程结束后,心跳线程也会结束，否则一直运行
                 */
                while (true) {
                    System.out.println("HeartBeat Thread Running And Checking Connection");
                    try {
                        Thread.sleep(1_000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            /**
             * 如果心跳线程为非守护线程的话，业务线程结束后，得等到维持心跳的线程结束后（大概100秒以后），
             * 系统才退出，仔细观察控制台线程退出的时间
             *
             * 当心跳线程为守护线程，当业务线程结束后，心跳线程直接结束
             *
             * 如果当线程已经start了，再设置daemon属性会抛出IllegalThreadStatesException
             */
//            innerThread.setDaemon(true);

            innerThread.start();

            //业务线程，只需要花费5秒钟就运行完毕（使用睡眠模拟）
            try {
                Thread.sleep(5_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Business Thread Finished Successfully");
        });


        System.out.println("Business Thread Start...");
        thread.start();

    }
}
