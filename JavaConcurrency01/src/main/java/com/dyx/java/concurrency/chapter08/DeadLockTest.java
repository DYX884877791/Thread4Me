package com.dyx.java.concurrency.chapter08;

/**
 * DeadLock
 * 死锁，当线程中存在多个锁时，并存在争夺同一个资源的场景，则会发生死锁
 * @auther: mac
 * @since: 2019-06-30 01:10
 */
public class DeadLockTest {

    public static void main(String[] args) {
        ServiceOne serviceOne = new ServiceOne();
        ServiceTwo serviceTwo = new ServiceTwo();
        serviceOne.setServiceTwo(serviceTwo);
        serviceTwo.setServiceOne(serviceOne);


        /**
         * 运行该方法，发生死锁：
         *      原因分析：
         *      假设第一个线程先执行，执行service1的第一个方法，获得了service1中的lackOne对象锁，
         *      而service1的第一个方法调用了service2的第一个方法，而service2的第一个方法会获得service2中的lackTwo对象锁；
         *
         *      假如此时第二个线程就在此刻lackTwo对象锁被service2的第一个方法获得之前开始执行，调用了service2的第二个方法，
         *      而且是刚好service2的第二个方法获得了service2中的lackTwo对象锁，则service2的第二个方法继续执行，
         *      而线程1中service2的第一个方法因为没有获取到lackTwo对象锁而被阻塞住，但线程1还是持有lackOne对象锁，
         *      service2的第二个方法调用service1的第二个方法，service1的第二个方法也会争夺lackOne对象锁，但是lackOne对象锁已经被
         *      线程1给抢走了，service1的第二个方法也会被阻塞住，所以第二个线程也会被阻塞住，如此形成了死锁。。。
         *
         */
        /**
         *  此时可以通过命令查看是否存在死锁：jstack + java进程号，进程号可以通过jps查看
         *
         * Java stack information for the threads listed above:
         * ===================================================
         * "Thread2":
         *         at com.dyx.java.concurrency.chapter08.ServiceOne.methodTwoInServiceOne(ServiceOne.java:38)
         *         - waiting to lock <0x000000076acc03f8> (a java.lang.Object)
         *         at com.dyx.java.concurrency.chapter08.ServiceTwo.methodTwoInServiceTwo(ServiceTwo.java:38)
         *         - locked <0x000000076acc5540> (a java.lang.Object)
         *         at com.dyx.java.concurrency.chapter08.DeadLockTest$2.run(DeadLockTest.java:45)
         * "Thread1":
         *         at com.dyx.java.concurrency.chapter08.ServiceTwo.methodOneInServiceTwo(ServiceTwo.java:28)
         *         - waiting to lock <0x000000076acc5540> (a java.lang.Object)
         *         at com.dyx.java.concurrency.chapter08.ServiceOne.methodOneInServiceOne(ServiceOne.java:29)
         *         - locked <0x000000076acc03f8> (a java.lang.Object)
         *         at com.dyx.java.concurrency.chapter08.DeadLockTest$1.run(DeadLockTest.java:36)
         *
         * Found 1 deadlock.
         */
        new Thread("Thread1") {
            @Override
            public void run() {
                while (true) {
                    serviceOne.methodOneInServiceOne();
                }
            }
        }.start();

        new Thread("Thread2"){
            @Override
            public void run() {
                while (true) {
                    serviceTwo.methodTwoInServiceTwo();
                }
            }
        }.start();
    }

}
