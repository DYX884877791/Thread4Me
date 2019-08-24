package com.dyx.java.concurrency.chapter13;


public class CustomerSimpleThreadPool2Test {

    // 往其中添加40个线程
    public static void main(String[] args) {
        // 20指定了工作线程的数量，30指定了任务队列的数量，线程池中最高只能存放30个线程，当添加40个任务时，第31个任务被添加时会报错
        CustomerSimpleThreadPool2 customerSimpleThreadPool2 = new CustomerSimpleThreadPool2(20,30,
                CustomerSimpleThreadPool2.DEFAULT_DISCARD_POLICY);
        for (int i = 0; i < 40 ; i++) {
            customerSimpleThreadPool2.submit(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " is running...");
                    Thread.sleep(10_000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 10秒后关闭线程池
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customerSimpleThreadPool2.shutdown();
    }
}
