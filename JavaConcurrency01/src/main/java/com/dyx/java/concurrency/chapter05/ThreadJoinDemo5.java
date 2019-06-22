package com.dyx.java.concurrency.chapter05;

/**
 * ThreadJoinDemo5
 * 模拟数据采集场景：开辟几个线程去采集服务器的数据，当采集完毕然后程序退出
 * @auther: mac
 * @since: 2019-06-22 13:55
 */
public class ThreadJoinDemo5 {

    /**
     * main线程启动数据采集线程
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        System.out.println("system startup at time [ " + startTime + " ]");

        Thread thread1 = new Thread(new CaptureRunable("machine1", 10_000L));
        Thread thread2 = new Thread(new CaptureRunable("machine2", 30_000L));
        Thread thread3 = new Thread(new CaptureRunable("machine3", 15_000L));

        thread1.start();
        thread2.start();
        thread3.start();

        /**
         * join方法使三个线程先执行，再运行main线程，main线程保存数据
         */
        thread1.join();
        thread2.join();
        thread3.join();

        long endTime = System.currentTimeMillis();
        /**
         * 当三个采集线程还没有结束，系统就提示采集完成，不符合需求，所以得等三个采集线程都完成了，才提示采集完成，使用join方法
         */
        System.out.printf("All works done...Capture Data begin time is %s, end time is %s\n", startTime, endTime);
        System.out.println("Total spend time is " + (endTime - startTime) + " mills seconds");
        System.out.println("Begin save data");
    }
}


/**
 * 数据采集线程
 */
class CaptureRunable implements Runnable {

    //线程名称
    private String threadName;

    //采集数据所需要的时间
    private long spendTime;

    public CaptureRunable(String threadName, long spendTime) {
        this.threadName = threadName;
        this.spendTime = spendTime;
    }

    @Override
    public void run() {
        System.out.println("Thread [" + threadName +"] is capturing data...");
        try {
            Thread.sleep(spendTime);
            System.out.println("Thread [" + threadName +"] capture data finished!!!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
