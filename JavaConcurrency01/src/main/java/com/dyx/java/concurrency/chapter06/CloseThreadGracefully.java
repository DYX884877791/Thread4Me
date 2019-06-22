package com.dyx.java.concurrency.chapter06;

/**
 * CloseThreadGracefully
 * 如何优雅地关掉线程。。。
 * 1.使用状态开关
 * 2.使用interrupt方法
 * @auther: mac
 * @since: 2019-06-22 18:04
 */
public class CloseThreadGracefully {

    public static void main(String[] args) {
        WorkThread workThread = new WorkThread();
        workThread.start();
//        try {
//            Thread.sleep(5_000L);//让工作线程先运行，5秒后结束线程，这里使用带参数的join也行
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        try {
            workThread.join(5_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        workThread.shutdown();
        System.out.println("All works done ...");
    }
}

class WorkThread extends Thread {
    //线程的执行状态
    private boolean isRunning = true;

    @Override
    public void run() {
        while (isRunning) {
            System.out.println("Running...");
        }
    }

    //关掉线程
    public void shutdown() {
        System.out.println("shutdown working thread...");
        this.isRunning = false;
    }
}