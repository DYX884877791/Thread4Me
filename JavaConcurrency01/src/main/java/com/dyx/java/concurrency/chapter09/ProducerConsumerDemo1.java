package com.dyx.java.concurrency.chapter09;

/**
 * ProducerConsumerDemo
 * 生产者消费者示例第一版
 * 与实际场景不符，生产者连续生产很多个而没有被消费，或者是消费者连续消费很多个而没有继续生产，即没有线程之间的通信
 * @auther: mac
 * @since: 2019-06-30 02:22
 */
public class ProducerConsumerDemo1 {

    // 公共变量
    private Integer i = 0;

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    // 锁对象
    private static final Object LOCK = new Object();

    /**
     * 生产数据
     */
    public void produce() {
        synchronized (LOCK) {
            System.out.println("[" + Thread.currentThread().getName() + "]生产第" + (++i) + "个数据");
        }
    }

    /**
     * 消费数据
     */
    public void consume() {
        synchronized (LOCK) { // 因为此处加锁的是同一个锁对象，不会造成数据的混乱
            System.out.println("[" + Thread.currentThread().getName() + "]消费第" + (i--) + "个数据");
        }
    }

    public static void main(String[] args) {
        ProducerConsumerDemo1 producerConsumerDemo1 = new ProducerConsumerDemo1();

        int maxCapacity = 100;

        new Thread("生产者") {
            @Override
            public void run() {
                while (producerConsumerDemo1.getI() < maxCapacity) {
                    producerConsumerDemo1.produce();
                }
            }
        }.start();

        new Thread("消费者") {
            @Override
            public void run() {
                while (producerConsumerDemo1.getI() > 0) {
                    producerConsumerDemo1.consume();
                }
            }
        }.start();
    }
}
