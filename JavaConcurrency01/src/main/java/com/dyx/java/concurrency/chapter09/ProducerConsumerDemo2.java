package com.dyx.java.concurrency.chapter09;

/**
 * ProducerConsumerDemo
 * 生产者消费者示例第二版：生产一个，消费一个
 * 引入线程之间的通信机制
 *
 * @auther: mac
 * @since: 2019-06-30 02:22
 */
public class ProducerConsumerDemo2 {

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
                    long startTime = System.currentTimeMillis();
                    System.out.println("startTime:" + startTime);
                    System.out.println("[" + Thread.currentThread().getName() + "]waiting...");
                    /**
                     * wait():
                     *      让当前线程在当前等待(被挂起..BLOCKED状态)直到另一个线程调用该对象(LOCK对象)的notify或者notifyAll方法，
                     *      该方法会使当前线程释放掉目前已经拥有锁资源的所有权，并在此处暂停，当LOCK的notify方法或者notifyAll方法
                     *      被调用，当前线程则会被唤醒，但并不是从此处开始立即接着运行，而是等待cpu调度（也要抢夺LOCK锁资源），当cpu
                     *      调度到该线程时，当前线程立即从此处接着运行
                     *
                     * wait方法会释放掉当前线程所持有的锁，并让该线程进入到LOCK对象中的等待队列之中
                     */
                    LOCK.wait(); // 如果已经生产数据了，

                    long endTime = System.currentTimeMillis();
                    /**
                     * 观察到endTime - startTime为2秒，正好为消费者消费的时间，说明在消费者消费的时间中，该线程的确等待了2秒
                     */
                    System.out.println("endTime:" + endTime);//

                    /**
                     * wait方法重载：wait(long timeout)，timeout：超时时间，到超时时间，自己主动唤醒自己，不需要其他线程来唤醒
                     */
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else { // 如果还没有生产数据，则会生产数据
                System.out.print("[" + Thread.currentThread().getName() + "]生产第" + (++i) + "个数据");

                try {
                    Thread.sleep(1_000L); // 使用睡眠模拟生产所需时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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

                try {
                    Thread.sleep(2_000L); // 使用睡眠模拟消费所需时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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
        ProducerConsumerDemo2 producerConsumerDemo1 = new ProducerConsumerDemo2();

        /**
         * 一启动，生产者和消费者都开始运行，进入while(true)无限循环，生产者调用produce方法，消费者调用consume方法，假设produce方法先抢到了
         * LOCK锁资源，则consume方法被阻塞住，produce方法判断isProduced为false，则会生产数据，isProduced变为true，并唤醒位于LOCK
         * 对象等待队列中的线程（让线程准备运行，并不是马上就运行），但此时并无线程，随后produce方法执行完毕，但是因为循环调用，下一次
         * produce方法被调用，isProduced为true，LOCK.wait()被调用，进入LOCK对象的等待队列中，等待。。。并释放锁，consume方法获得
         * 锁，开始消费数据，consume方法判断isProduced为true，会消费数据，isProduced变为false，并唤醒位于LOCK对象等待队列中的线程，
         * 即生产者线程，但只是让生产者线程又抢夺锁资源的机会，此时消费者线程也是无限循环，下一次也会被调用，判断isProduced为false，
         * LOCK.wait()被调用，进入LOCK对象的等待队列中，等待。。。并释放锁，生产者线程又获得锁，开始执行。。。如此反复。。。
         *
         */
        new Thread("生产者") {
            @Override
            public void run() {
                while (true) { // 生产者不断地生产
                    producerConsumerDemo1.produce();

                }
            }
        }.start();


        new Thread("消费者") {
            @Override
            public void run() {
                while (true) { // 消费者不断地消费
                    producerConsumerDemo1.consume();

                }
            }
        }.start();
    }
}
