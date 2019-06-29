package com.dyx.java.concurrency.chapter07;

/**
 * SynchronizeDemo
 * 测试Synchronize关键字：Class对象锁
 * @auther: mac
 * @since: 2019-06-29 16:00
 */
public class SynchronizeDemo3 {

    public static void main(String[] args) {

        ClassLock classLock = new ClassLock();

        /**
         * 当多个线程去调用同一个类的多个同步静态方法时，或者是不同对象的相同方法时，因为争夺的都是同一个Class类对象的锁，所以会有
         * 同步
         */
        new Thread("Thread1") {
            @Override
            public void run() {
                classLock.method1();
            }
        }.start();

        new Thread("Thread2") {
            @Override
            public void run() {
                classLock.method2();
            }
        }.start();

        new Thread("Thread3") {
            @Override
            public void run() {
                ClassLock.method3();
            }
        }.start();

        // 当不同的对象调用同一个方法时，也会存在锁资源的争夺
        new Thread("Thread4") {
            @Override
            public void run() {
                new ClassLock().method2();
            }
        }.start();


        new Thread("Thread5") {
            @Override
            public void run() {
                new ClassLock().method2();
            }
        }.start();


        new Thread("Thread6") {
            @Override
            public void run() {
                new ClassLock().method3();
            }
        }.start();

        new Thread("Thread7") {
            @Override
            public void run() {
                new ClassLock().method4();
            }
        }.start();
    }
}

/**
 * 测试Class对象锁
 */
class ClassLock {

    static {
        synchronized (ClassLock.class) {
            System.out.println("Thread [ " + Thread.currentThread().getName() + " ] is executing static initializer...");
            System.out.println("initializer is locked by " + ClassLock.class.toString());
            try {
                Thread.sleep(10_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * method1方法是静态同步方法，该同步方法抢夺的是ClassLock类的Class类对象锁
     */
    public synchronized static void method1() {
        System.out.println("Thread [ " + Thread.currentThread().getName() + " ] is executing method1...");
        System.out.println("Method1 is locked by " + ClassLock.class.toString());
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * method2方法是静态同步方法，该同步方法抢夺的是ClassLock类的Class类对象锁
     */
    public synchronized static void method2() {
        System.out.println("Thread [ " + Thread.currentThread().getName() + " ] is executing method2...");
        System.out.println("Method2 is locked by " + ClassLock.class.toString());
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * method3方法无锁，但方法中的同步代码块抢夺的也是ClassLock类的Class类对象锁，等价于上面两种写法，只是锁的范围更小
     */
    public static void method3() {
        synchronized (ClassLock.class) {
            System.out.println("Thread [ " + Thread.currentThread().getName() + " ] is executing method3...");
            System.out.println("Method3 is locked by " + ClassLock.class.toString());
            try {
                Thread.sleep(10_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * method4方法无锁，但方法中的同步代码块抢夺的也是ClassLock类的Class类对象锁，等价于上面两种写法，只是锁的范围更小
     */
    public void method4() {

    }
}
