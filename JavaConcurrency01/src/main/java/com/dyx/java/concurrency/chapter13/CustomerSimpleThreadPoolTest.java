package com.dyx.java.concurrency.chapter13;


public class CustomerSimpleThreadPoolTest {

    // 1. 使用默认数量的线程池，往其中添加40个线程
    public static void main(String[] args) {
        CustomerSimpleThreadPool customerSimpleThreadPool = new CustomerSimpleThreadPool();
        for (int i = 0; i < 40 ; i++) {
            customerSimpleThreadPool.submit(() -> {
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
