package com.dyx.java.concurrency.chapter08;

/**
 * ServiceOne
 *
 * @auther: mac
 * @since: 2019-06-30 01:23
 */
public class ServiceTwo {

    private final Object lockTwo = new Object();

    private ServiceOne serviceOne;

    public ServiceOne getServiceOne() {
        return serviceOne;
    }

    public void setServiceOne(ServiceOne serviceOne) {
        this.serviceOne = serviceOne;
    }

    /**
     * 方法1
     */
    public void methodOneInServiceTwo() {
        synchronized (lockTwo) {
            System.out.println("Thread [" + Thread.currentThread().getName() + "] is executing methodOneInServiceTwo...");
        }
    }

    /**
     * 方法2
     */
    public void methodTwoInServiceTwo() {
        synchronized (lockTwo) {
            System.out.println("Thread [" + Thread.currentThread().getName() + "] is executing methodTwoInServiceTwo...");
            serviceOne.methodTwoInServiceOne();//此处调用另一个类的方法2
        }
    }
}
