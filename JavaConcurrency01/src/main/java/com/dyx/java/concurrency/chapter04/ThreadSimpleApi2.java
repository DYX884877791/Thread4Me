package com.dyx.java.concurrency.chapter04;

/**
 * 线程的优先级,
 * 优先级并不能决定线程的执行顺序
 */
public class ThreadSimpleApi2 {
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + "-Index-" + i);
            }
        });

        /**
         * 并不是thread1线程的优先级最高，最先被执行
         */
        thread1.setPriority(Thread.MAX_PRIORITY);

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + "-Index-" + i);
            }
        });

        thread2.setPriority(Thread.NORM_PRIORITY);

        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + "-Index-" + i);
            }
        });

        thread3.setPriority(Thread.MIN_PRIORITY);

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
