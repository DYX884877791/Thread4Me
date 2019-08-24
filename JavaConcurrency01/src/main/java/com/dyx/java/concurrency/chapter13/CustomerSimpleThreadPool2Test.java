package com.dyx.java.concurrency.chapter13;


public class CustomerSimpleThreadPool2Test {

    // 往其中添加40个线程
    public static void main(String[] args) {
        // 如果指定了queueSize为10，此时再当往线程池中添加第11个任务时，会抛出异常


        CustomerSimpleThreadPool2 customerSimpleThreadPool2 = new CustomerSimpleThreadPool2(6,40,
                CustomerSimpleThreadPool2.DEFAULT_DISCARD_POLICY);
        for (int i = 0; i < 40 ; i++) {
            customerSimpleThreadPool2.submit(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " is running...");
                    Thread.sleep(5_000L);
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
