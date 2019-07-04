package com.dyx.java.concurrency.chapter09.MulitCaptureThreadsDemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * MulitCaptureThreadsDemo
 * 假设使用多线程对庞大数量的服务器进行数据采集，如果机器有1000台，此时如果创建1000个采集数据的线程并让1000个线程都运行显然不合理，
 * 因为1000个线程数量过多，cpu反而会频繁地进行线程的上下文切换，反而降低了效率，所以让一部分线程先运行，其余线程等待，即保持着少部分
 * 的线程在运行，当这部分的线程运行完毕，再让其他部分的线程开始运行...最后所有的数据采集线程都运行完毕，主线程才开始工作，将各个采集
 * 线程采集到的数据进行汇总...
 *
 *      在一定范围内，机器性能随线程数增多而提高，一旦超过某个阈值，线程数增多，性能反而下降，因为cpu调度频繁
 *
 *      采集数据策略：固定数量的线程，假设固定可以有10个线程运行，也就对10个机器采集，当某台机器先采集完毕，对应的线程结束，则新建一个
 *      线程，(或者是10个之外的线程被wait，当有一台机器采集完毕之后，wait的线程被唤醒，然后来执行)，对新机器采集数据...
 * @auther: mac
 * @since: 2019-07-04 22:15
 */
public class CaptureService {

    // synchronized锁对象
    final static LinkedList<Control> CONTROLS = new LinkedList<>();

    // 最大的工作线程
    private final static int MAX_WORKER = 6;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("[startTime=========>]" + startTime);
        /**
         * 1.假设有12台机器，则会创建12个线程并启动，要让部分线程先执行，考虑使用join方法（看起来已经调用start了，但实际上不会真正地运行）
         */

        List<Thread> workThreads = new ArrayList<>();// 存放工作线程的容器
        Arrays.asList("M1","M2","M3","M4","M5","M6","M7","M8","M9","M10","M11","M12").stream()
                .map(CaptureService::createCaptureThread)
                .forEach(thread -> {
                    thread.start();
//                    try {
//                        /**
//                         * 不能在此处加入join，假设加入了join，在这个线程启动之后并join，会让当前线程先执行完，然后再调用下一个线程
//                         * 的start方法，这样第一个线程彻底执行完了，第二个线程才会被启动，然后再开始运行，第二个线程运行完毕，再启动
//                         * 第三个线程... 这里是线程依次执行，而不是多线程并发执行了
//                         *
//                         * 解决：应该先让所有线程都启动，再join
//                         */
//                        thread.join();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    workThreads.add(thread);//3.将各线程加入到容器中
                });

        workThreads.stream().forEach(thread -> {
            try {
                thread.join();// 4.遍历线程容器各线程，并join
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long endTime = System.currentTimeMillis();
        System.out.println("[endTime=========>]" + endTime);
        System.out.println("所有采集线程都运行完毕,主线程进行数据汇总...");
    }

    /**
     * 2.创建各机器对应的线程
     * @param name
     * @return
     */
    private static Thread createCaptureThread(String name) {
        return new Thread(() -> {

            /**
             * 而采集数据的线程是否实际上执行，是由当前正在运行的任务的个数决定
             */
            synchronized (CONTROLS) { // 获得了控制锁，就会直接运行
                System.out.println("The worker thread [" + Thread.currentThread().getName() + "] begin capture data...");
                while (CONTROLS.size() >= MAX_WORKER) { // 如果当前size大于等于6，则直接将该线程等待，否则是可以往下运行的
                    try {
                        System.out.println("The worker thread [" + Thread.currentThread().getName() + "] waiting~~~~~~");
                        CONTROLS.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 当每有一个线程可以运行时，将CONTROLS里面增加一个内容物作为计数
                CONTROLS.addLast(new Control());
            }

            try { // 不能将此表示运行中的代码逻辑移到synchronized语句中，否则变为串行执行了
                System.out.println("The worker thread [" + Thread.currentThread().getName() + "] working!!!!!!");

                Thread.sleep(10_000L);// 使用睡眠模拟线程正在工作

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 当运行完毕后，去除一个计数，并唤醒其他wait的线程
            synchronized (CONTROLS) {
                System.out.println("The worker thread [" + Thread.currentThread().getName() + "] end capture data...");
                CONTROLS.removeLast();
                CONTROLS.notifyAll();
            }

        }, name);
    }

    // 控制类
    private static class Control {
    }
}
