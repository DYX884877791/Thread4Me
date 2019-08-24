package com.dyx.java.concurrency.chapter13;


public class CustomerSimpleThreadPool1Test {

    // 使用默认数量的线程池，往其中添加40个线程
    public static void main(String[] args) {

        // 调用构造器初始化的时候，线程池已经构建了10个工作线程，任务队列长度为40，里面存放了40个提交的任务，工作线程从任务队列中拉取任务，自己执行
        CustomerSimpleThreadPool1 customerSimpleThreadPool1 = new CustomerSimpleThreadPool1();
        for (int i = 0; i < 40 ; i++) {
            customerSimpleThreadPool1.submit(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " is running...");
                    Thread.sleep(10_000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
