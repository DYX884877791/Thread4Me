package com.dyx.java.concurrency.chapter09;

import java.util.stream.Stream;

/**
 * ProducerConsumerDemo
 * 生产者消费者示例第三版：当有多个生产者、消费者使用notify()方法会怎样？
 *
 * 程序卡住，发生了什么，此时使用jstack 进程号 查看也并没有死锁，发现四个线程都在wait状态中。。。
 *
 * @auther: mac
 * @since: 2019-06-30 02:22
 */
public class ProducerConsumerDemo3 {

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
                LOCK.notify();
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
                LOCK.notify();
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
        ProducerConsumerDemo3 producerConsumerDemo1 = new ProducerConsumerDemo3();


        /**
         * 提示：生产者唤醒另一个生产者无意义，只能唤醒消费者才有意义，消费者同理
         *
         * (根据实际情况具体分析)
         * 1.假设生产者1先运行，获得了LOCK锁，先生产一个数据，这里唤醒没有目标线程，isProduced变为true，然后LOCK.wait()...释放锁，
         * 2.生产者2获得锁，判断isProduced变为true（数据已生产），直接wait()...，
         * 3.随后消费者1，消费数据，并唤醒其中一个生产者，假设生产者1，此时消费者1（由于循环）再次消费，也进入wait()...，
         * 4.然后生产者1生产数据，唤醒消费者1，然后while循环，再次wait()...，
         * 5.然后消费者2，消费上一步产生的数据，唤醒生产者2，然后while循环进入，wait()...，
         * 6.随后消费者1因为没有数据可以消费，直接wait()...，
         * 7.最后被消费者2唤醒的生产者2，生产数据，却唤醒了生产者1，生产者2再次while循环，进入wait()...，
         * 8.剩下生产者1，已经有数据了，所以也直接wait()...，
         * 从上文看，两个生产/消费者都进入了wait状态，出现了"假死"
         *
         * 根本原因：某一次的单个唤醒把同类给唤醒了，没有唤醒异性
         * 解决方法：唤醒的时候把异性也一同唤醒，即不使用notify()方法，而使用notifyAll()方法
         *
         */
        Stream.of("生产者1","生产者2").forEach(n -> new Thread(n) {
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
