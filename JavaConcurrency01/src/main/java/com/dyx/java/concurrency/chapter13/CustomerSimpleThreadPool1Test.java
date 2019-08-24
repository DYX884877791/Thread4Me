package com.dyx.java.concurrency.chapter13;


public class CustomerSimpleThreadPool1Test {

    // 使用默认数量的线程池，往其中添加40个线程
    public static void main(String[] args) {
        CustomerSimpleThreadPool1 customerSimpleThreadPool1 = new CustomerSimpleThreadPool1();
        for (int i = 0; i < 40 ; i++) {
            customerSimpleThreadPool1.submit(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " is running...");
                    Thread.sleep(20_000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
