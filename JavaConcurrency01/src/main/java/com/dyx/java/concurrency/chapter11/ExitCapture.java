package com.dyx.java.concurrency.chapter11;

/**
 * ExitCapture
 *
 * 在应用程序中注入钩子程序
 * 将线程运行过程中产生的异常信息捕获，在Linux环境下运行并观察
 * @auther: mac
 * @since: 2019-07-07 00:23
 */
public class ExitCapture {

    public static void main(String[] args) {

        /**
         *
         * 注入钩子程序，利用Runtime，当程序运行过程中抛出运行时异常，或者（新开一个tab）手动结束该进程，kill + pid，不要使用kill -9 pid
         *
         * 当程序遇到异常或手动结束时，会打印出This application will exit...和运行notifyAndRelease方法
         */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("This application will exit...");
            notifyAndRelease();
        }));

        int i = 0;

        while(true) {
            try {
                Thread.sleep(1_000L);
                System.out.println("I am working...");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            i++;
            if (i > 20) {
                throw new RuntimeException("Error");
            }
        }
    }

    private static void notifyAndRelease() {
        System.out.println("notify to the admin...");
        try {
            Thread.sleep(1_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("will release resources(socket,file,connection...)");
        try {
            Thread.sleep(1_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("notify and release done");
    }
}
