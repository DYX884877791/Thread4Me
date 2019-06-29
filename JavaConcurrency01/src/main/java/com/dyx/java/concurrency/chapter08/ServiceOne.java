package com.dyx.java.concurrency.chapter08;

/**
 * ServiceOne
 *
 * @auther: mac
 * @since: 2019-06-30 01:23
 */
public class ServiceOne {

    private final Object lockOne = new Object();

    private ServiceTwo serviceTwo;

    public ServiceTwo getServiceTwo() {
        return serviceTwo;
    }

    public void setServiceTwo(ServiceTwo serviceTwo) {
        this.serviceTwo = serviceTwo;
    }

    /**
     * 方法1
     */
    public void methodOneInServiceOne() {
        synchronized (lockOne) {
            System.out.println("Thread [" + Thread.currentThread().getName() + "] is executing methodOneInServiceOne...");
            serviceTwo.methodOneInServiceTwo();//此处调用另一个类的方法1
        }
    }

    /**
     * 方法2
     */
    public void methodTwoInServiceOne() {
        synchronized (lockOne) {
            System.out.println("Thread [" + Thread.currentThread().getName() + "] is executing methodTwoInServiceOne...");

        }
    }
}
