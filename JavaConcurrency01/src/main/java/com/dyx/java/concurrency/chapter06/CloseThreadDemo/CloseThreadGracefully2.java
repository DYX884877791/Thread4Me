package com.dyx.java.concurrency.chapter06.CloseThreadDemo;

/**
 * CloseThreadGracefully
 * 如何优雅地关掉线程。。。
 * 1.使用状态开关
 * 2.使用interrupt方法
 * @auther: mac
 * @since: 2019-06-22 18:04
 */
public class CloseThreadGracefully2 {

    public static void main(String[] args) {
        WorkThread2 workThread2 = new WorkThread2();
        workThread2.start();
        //        try {
//            Thread.sleep(5_000L);//让工作线程先运行，5秒后结束线程，这里使用带参数的join也行
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        try {
            workThread2.join(5_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        workThread2.shutdown();
    }
}

class WorkThread2 extends Thread {

    @Override
    public void run() {
//        while (true) {
//            System.out.println("Running...");
//            try {
//                Thread.sleep(1L);
//            } catch (InterruptedException e) { // 使用捕获中断异常来结束线程的生命周期
//                System.out.println("shutdown working thread...");
//                e.printStackTrace();
//                break;
//            }
//        }

        // 或者通过判断是否中断状态来决定是否退出
        while (true) {
            if (this.isInterrupted()) {
                System.out.println("shutdown working thread...");
                break;
            } else {
                System.out.println("Running...");
            }
        }
    }

    //关掉线程
    public void shutdown() {
        this.interrupt();
    }
}