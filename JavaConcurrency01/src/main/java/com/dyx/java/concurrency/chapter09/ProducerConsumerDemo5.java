package com.dyx.java.concurrency.chapter09;

import java.util.stream.Stream;

/**
 * ProducerConsumerDemo
 * 生产者消费者示例第四版：
 * 当有多个生产者、消费者进行线程间通信时，使用notifyAll()方法，并改造
 *
 * @auther: mac
 * @since: 2019-06-30 02:22
 */
public class ProducerConsumerDemo5 {

    // 公共变量
    private Integer i = 0;

    // 锁对象
    private static final Object LOCK = new Object();

    // 是否已经生产的标记，默认无
    private boolean isProduced = false;

    /**
     * 生产数据
     */
    public void produce() {
        synchronized (LOCK) { // ......1
            System.out.println("[" + Thread.currentThread().getName() + ":]is produced?===========>" + isProduced);

            /**
             * 这里将Demo4的代码如下改造：
             *      此时使用if的弊端：假设有两个生产者线程，使用if，并且当前两个线程都判断到数据已经被生产（该数据即将被消费者所消费）
             *      而在一前一后都进入了wait...，随后消费者消费数据，并唤醒两个生产者，两个生产者争夺锁资源，假如生产者1获得锁资源，
             *      继续往下运行，并生产数据，isProduced变为true，又因为run方法中是while(true)循环，再一次运行produce方法，迅速
             *      抢到锁，通过if判断，再次进入wait等待不再运行，并释放锁资源；如果这里生产者2抢到了锁，从wait地方苏醒，又生产一个
             *      数据...造成重复生产数据，此时后生产的数据会将前一个生产的数据覆盖...
             *
             *      改进方法：if改为while
             */
//            if (isProduced) {
//                try {
//                    System.out.println("[" + Thread.currentThread().getName() + "]waiting...");
//                    LOCK.wait(); // 如果已经生产数据了，
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            System.out.print("[" + Thread.currentThread().getName() + "]生产第" + (++i) + "个数据");
//
//            // 此处生产成功了，则会唤醒位于LOCK等待队列中的线程，即消费者线程继续消费
//            LOCK.notifyAll();// 唤醒位于LOCK锁对象等待队列中的所有线程，而notify()方法只会唤醒一个线程
//            isProduced = true;
//            System.out.println("[===========>]生产第" + i + "个数据成功");


            /**
             * 为什么要使用while，此处流程分析前部分与上面一致:
             *      ...如果这里生产者2抢到了锁，从wait地方苏醒，继续往下运行，但又进入循环判断isProduced为true，（此isProduced是
             *      由生产者1置为true），所以再次进入wait，不会再生产数据...
             */
            while (isProduced) { // 如果已经
                try {
                    System.out.println("[" + Thread.currentThread().getName() + "]waiting...");
                    LOCK.wait(); // 如果已经生产数据了，
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print("[" + Thread.currentThread().getName() + "]生产第" + (++i) + "个数据");

            // 此处生产成功了，则会唤醒位于LOCK等待队列中的线程，即消费者线程继续消费
            LOCK.notifyAll();// 唤醒位于LOCK锁对象等待队列中的所有线程，而notify()方法只会唤醒一个线程
            isProduced = true;
            System.out.println("[===========>]生产第" + i + "个数据成功");
        }     // ......3
    }

    /**
     * 消费数据
     */
    public void consume() {
        synchronized (LOCK) { // ......4
            System.out.println("[" + Thread.currentThread().getName() + ":]is produced?----------->" + isProduced);
            while (!isProduced) {
                try {
                    System.out.println("[" + Thread.currentThread().getName() + "]waiting...");
                    LOCK.wait(); // 消费者线程进入到LOCK对象的等待队列中，直到有另一个线程（生产者线程）来唤醒它
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.print("[" + Thread.currentThread().getName() + "]消费第" + i + "个数据");

            // 此处消费成功了，notify用于唤醒位于LOCK等待队列中的线程，即生产者线程继续生产
            LOCK.notifyAll();
            isProduced = false;
            System.out.println("[----------->]消费第" + i + "个数据成功");

        } // ......6
    }

    public static void main(String[] args) {
        ProducerConsumerDemo5 producerConsumerDemo1 = new ProducerConsumerDemo5();


        Stream.of("生产者1","生产者2","生产者3").forEach(n -> new Thread(n) {
            @Override
            public void run() {
                while (true) { // 生产者不断地生产
                    producerConsumerDemo1.produce();
                    try {
                        Thread.sleep(5_000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start());


        Stream.of("消费者1","消费者2").forEach(n -> new Thread(n) {
            @Override
            public void run() {
                while (true) { // 消费者不断地消费
                    producerConsumerDemo1.consume();
                    try {
                        Thread.sleep(5_000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start());
    }
}
