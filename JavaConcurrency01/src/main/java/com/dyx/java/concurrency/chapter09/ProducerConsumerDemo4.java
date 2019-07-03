package com.dyx.java.concurrency.chapter09;

import java.util.stream.Stream;

/**
 * ProducerConsumerDemo
 * 生产者消费者示例第四版：
 * 当有多个生产者、消费者进行线程间通信时，使用notifyAll()方法
 *
 * @auther: mac
 * @since: 2019-06-30 02:22
 */
public class ProducerConsumerDemo4 {

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
            if (isProduced) { // ......2
                try {
                    /**
                     * wait():让当前线程等待(一直被挂起)直到另一个线程调用该对象(LOCK对象)的notify或者notifyAll方法，
                     *        当另一个线程调用了LOCK对象的notify或者notifyAll方法时，当前线程则会继续运行；
                     *        在当前场景下，只有当数据被消费了，生产数据的线程才会继续运行，所以LOCK的notify方法应该是由
                     *        消费方法里面消费成功之后来调用
                     *
                     *        注意：wait方法会释放掉当前线程所持有的锁，并让该线程进入到LOCK对象中的等待队列之中
                     *
                     */
                    System.out.println("[" + Thread.currentThread().getName() + "]waiting...");
                    LOCK.wait(); // 如果已经生产数据了，
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else { // 如果还没有生产数据，则会生产数据
                System.out.print("[" + Thread.currentThread().getName() + "]生产第" + (++i) + "个数据");

                // 此处生产成功了，则会唤醒位于LOCK等待队列中的线程，即消费者线程继续消费
                LOCK.notifyAll();// 唤醒位于LOCK锁对象等待队列中的所有线程，而notify()方法只会唤醒一个线程
                isProduced = true;
                System.out.println("[===========>]生产第" + i + "个数据成功");
            }
        }     // ......3
    }

    /**
     * 消费数据
     */
    public void consume() {
        synchronized (LOCK) { // ......4
            System.out.println("[" + Thread.currentThread().getName() + ":]is produced?----------->" + isProduced);
            if (isProduced) { // 如果生产者已经生产数据，则消费者直接消费 ......5

                System.out.print("[" + Thread.currentThread().getName() + "]消费第" + i + "个数据");

                // 此处消费成功了，notify用于唤醒位于LOCK等待队列中的线程，即生产者线程继续生产
                LOCK.notifyAll();
                isProduced = false;
                System.out.println("[----------->]消费第" + i + "个数据成功");
            } else { // 如果数据没有被生产，则消费者线程也会一直等待，直到有其他线程调用LOCK.notify();或者notifyAll方法将其唤醒
                try {
                    System.out.println("[" + Thread.currentThread().getName() + "]waiting...");
                    LOCK.wait(); // 消费者线程进入到LOCK对象的等待队列中，直到有另一个线程（生产者线程）来唤醒它
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } // ......6
    }

    public static void main(String[] args) {
        ProducerConsumerDemo4 producerConsumerDemo1 = new ProducerConsumerDemo4();


        Stream.of("生产者1","生产者2","生产者3").forEach(n -> new Thread(n) {
            @Override
            public void run() {
                while (true) { // 生产者不断地生产
                    producerConsumerDemo1.produce();
                }
            }
        }.start());


        Stream.of("消费者1","消费者2").forEach(n -> new Thread(n) {
            @Override
            public void run() {
                while (true) { // 消费者不断地消费
                    producerConsumerDemo1.consume();
                }
            }
        }.start());
    }
}
