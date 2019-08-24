package com.dyx.java.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {

    @Test
    public void test() {
//        AtomicInteger atomicInteger = new AtomicInteger(10);
//        int i = atomicInteger.addAndGet(14);
//        System.out.println("i=====>" + i);

        AtomicInteger atomicInteger1 = new AtomicInteger(0);
//        int i1 = atomicInteger1.getAndAdd(1);
//        System.out.println("i1=====>" + i1);

        Thread thread1 = new Thread(() -> {
            for (int j = 0; j < 1000; j++) {
                atomicInteger1.getAndIncrement();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int j = 0; j < 1000; j++) {
                atomicInteger1.getAndIncrement();
            }
        });
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(atomicInteger1.get());
    }
}
