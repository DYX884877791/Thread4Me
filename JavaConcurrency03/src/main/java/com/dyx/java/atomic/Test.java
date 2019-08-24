package com.dyx.java.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    public AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) {
        final Test test = new Test();
        for(int i=0;i<2;i++){
            new Thread(){
                public void run() {
                    for(int j=0;j<1000;j++)
                        test.atomicInteger.getAndIncrement();
                };
            }.start();
        }

        while(Thread.activeCount() > 2)  //保证前面的线程都执行完
            Thread.yield();
        System.out.println(test.atomicInteger.get());
    }
}
