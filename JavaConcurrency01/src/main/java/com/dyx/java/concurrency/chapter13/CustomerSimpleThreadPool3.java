package com.dyx.java.concurrency.chapter13;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 线程池，自己实现一个简易的线程池，并添加自动扩容机制
 *
 * 1. 必须要有一个任务队列，假设提交了一百个任务，但线程池中同时执行的任务只能有20个，那多出来的任务放在哪儿呢-----任务队列
 * 2. 拒绝策略：在任务队列中提交的任务也不能无限多，如果过多，如何处理：1. 抛出异常 2. 直接丢弃 3. 阻塞 4. 放在临时队列
 * 3. 初始化：3个重要参数 初始容量init 活跃的线程数active 最大的线程数max  max >= active >= init
 *
 *
 * 自动扩容的线程池
 * 思路：当任务进来，能否直接用初始化时的线程数来处理，如果可以，就不用添加额外的线程，否则尝试扩充线程数量到active，如果active值还是
 *  满足不了，则扩充到max，如果max还是满足不了，提交多余的任务就会加到任务队列中，如果超过任务队列的最大值，则执行拒绝策略。。。
 *  而且还要考虑到如果线程数量达到max时，比如100个，就是说在业务处理峰值时，线程池中撑到了最大容量，当峰值一过，线程池中总不可能一直
 *  维持这一百个线程（增加CPU上下文的切换开销），需要降低线程数量，可以降低到active。。。
 */
public class CustomerSimpleThreadPool3 extends Thread {

    // 1 线程池中可运行的线程数量
    private int size;

    // 1.1 任务队列中可以存放线程的数量（相对于上一版添加了这个变量，且更改了默认构造器）
    private final int queueSize;

    private final DiscardPolicy discardPolicy;

    // 1.2 线程池中可运行的线程的默认数量
//    private final static int DEFAULT_SIZE = 10;

    // 1.3 任务队列中可以存放线程的默认数量
    private final static int DEFAULT_TASK_QUEUE_SIZE = 20;

    // 2 创建线程池时指定线程数量
    public CustomerSimpleThreadPool3(int min, int active, int max, int queueSize, DiscardPolicy discardPolicy) {
        this.min = min;
        this.active = active;
        this.max = max;
        this.queueSize = queueSize;
        this.discardPolicy = discardPolicy;
        // 2.1 调用线程池构造器时需要对线程池进行初始化
        init();
    }

    // 2.2 如果创建时没有指定线程数量的话，给它分配默认大小：最小4个，活跃8个，最大12个
    public CustomerSimpleThreadPool3() {
        this(4, 8, 12, DEFAULT_TASK_QUEUE_SIZE, DEFAULT_DISCARD_POLICY);
    }

    // 2.3 因为添加了自动扩容机制，线程池的初始化方法时，创建min数量的线程
    private void init() {
        // 2.4 当线程池初始化时，创建工作线程
        for (int i = 0 ; i < min; i++) {
            createWorkerThread();
        }
        // 2.5 此时不能用final
        this.size = min;
        // 2.6 并启动
        this.start();
    }

    // 2.5 往线程池提交任务的方法，将添加的任务放在任务队列中
    public void submit(Runnable runnable) {

        // 提交时判断线程池是否被关闭
        if (isDestroy) {
            throw new RuntimeException("The Thread Pool has been shutdown...");
        }

        // 因为是任务队列的写操作，工作线程是读操作，所以加锁
        synchronized (TASK_QUEUE) {

            // 因为添加了拒绝策略，提交时判断一下
            if (TASK_QUEUE.size() > this.queueSize) {
                discardPolicy.discard();
            }

            System.out.println("A Task join the thread pool...");
            TASK_QUEUE.addLast(runnable);
            // 添加之后，唤醒被TASK_QUEUE阻塞的线程
            TASK_QUEUE.notifyAll();
        }
    }

    // 3 定义线程池中的工作线程
    private static class WorkerThread extends Thread {
        // 3.1 默认分配时的状态是空闲的
        private volatile WorkerThreadStatus status = WorkerThreadStatus.FREE;

        // 3.2 获取状态
        public WorkerThreadStatus getStatus() {
            return this.status;
        }

        // 3.3 线程池中工作线程的运行逻辑，而且要求运行完成后不能结束
        public void run() {
            OUTER:
            while (this.status != WorkerThreadStatus.DEAD) {
                Runnable runnable;
                // 3.4 工作线程实际上去线程池的任务队列中拿一个线程来执行
                synchronized (TASK_QUEUE) {
                    // 3.6 当任务队列是空的时候，工作线程挂起等待，此时状态为BLOCKED，直到有任务提交进来
                    while (TASK_QUEUE.isEmpty()) {
                        System.out.println("The Task Queue is empty, No Worker Thread will run...");
                        try {
                            this.status = WorkerThreadStatus.BLOCKED;
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
                            // 3.7 如果wait被中断了的话，则直接退出去
                            System.out.println("[" + Thread.currentThread() + "] Closed");
                            break OUTER;
                        }
                    }
                    // 3.8 如果TASK_QUEUE中有任务被提交了，工作线程被唤醒，取到任务队列中的第一个任务
                    runnable = TASK_QUEUE.removeFirst();

                    /**
                     *  应该把执行任务的逻辑放在synchronized代码块之外，因为如果run方法里面是个很耗时的任务，会导致某一个任务一直
                     *  会杵在synchronized之内，也不会释放锁，这样其他的工作线程，根本不会抢到锁，也就不会来拿TASK_QUEUE中的任务
                     *  就相当于还是串行了
                     */
//                    if (runnable != null) {
//                        // 3.9 执行过程中，工作线程的状态变为RUNNING
//                        this.status = WorkerThreadStatus.RUNNING;
//                        System.out.println("Worker Thread [" + Thread.currentThread() + "] started...");
//                        // 实际上是工作线程从线程队列中取出被提交的线程，来直接执行任务的run方法
//                        runnable.run();
//                        System.out.println("Worker Thread [" + Thread.currentThread() + "] finished...");
//                        // 3.10 执行完毕，工作线程的状态变为FREE
//                        this.status = WorkerThreadStatus.FREE;
//                    }
                }

                // 所以此段代码应该放在synchronized代码块之外
                if (runnable != null) {
                    // 3.9 执行过程中，工作线程的状态变为RUNNING
                    this.status = WorkerThreadStatus.RUNNING;
                    System.out.println("Worker Thread [" + Thread.currentThread() + "] started...");
                    // 实际上是工作线程从线程队列中取出被提交的线程，来直接执行任务的run方法
                    runnable.run();
                    System.out.println("Worker Thread [" + Thread.currentThread() + "] finished...");
                    // 3.10 执行完毕，工作线程的状态变为FREE
                    this.status = WorkerThreadStatus.FREE;
                }
            }
        }

        // 3.11 停止工作线程
        private void close() {
            this.status = WorkerThreadStatus.DEAD;
        }

        // 3.12 为了方便管理，设置工作线程的线程组
        public WorkerThread(ThreadGroup group, String name) {
            super(group, name);
        }
    }

    // 3.5 线程池中定义的线程的任务队列
    private static final LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

    // 4 定义线程池中的工作线程的状态，因为线程池中各线程的状态与平时的普通线程不一致，需要自己来定义
    private enum WorkerThreadStatus {
        //空闲、运行中、阻塞、死亡
        FREE, RUNNING, BLOCKED, DEAD;
    }

    // 5 创建工作线程
    public void createWorkerThread() {
        WorkerThread workerThread = new WorkerThread(WORKER_THREAD_GROUP, worker_thread_prefix + (seq++));
        workerThread.start();
        // 5.1 工作线程启动后将工作线程添加至WORKER_THREAD_QUEUE中
        WORKER_THREAD_QUEUE.add(workerThread);
    }

    // 6 定义工作线程所属的线程组以及各工作线程的name，此处使用自增id
    private static final ThreadGroup WORKER_THREAD_GROUP = new ThreadGroup("SIMPLE_THREAD_GROUP");

    // 6.1 定义工作线程的name前缀
    private static String worker_thread_prefix = "SIMPLE_THREAD_POOL-";

    // 6.2 定义工作线程的后缀，此处使用自增id
    private static volatile int seq = 0;

    // 7 定义一个工作的线程的集合
    private static final List<WorkerThread> WORKER_THREAD_QUEUE = new ArrayList<>();

    // 8 定义拒绝策略的接口及异常
    public interface DiscardPolicy {
        // 8.1 定义拒绝的接口方法
        void discard();
    }

    public static class DiscardException extends RuntimeException {
        public DiscardException(String message) {
            super(message);
        }
    }

    // 9 默认的拒绝策略，抛出异常
    public final static DiscardPolicy DEFAULT_DISCARD_POLICY = () -> {
        throw new DiscardException("Too many task, the task will be discarded");
    };

    // 10 添加停止线程池的方法（实际上就是停止WorkerThread），这里停止时让线程池中的所有任务都运行完毕之后才停止
    public void shutdown() {
        // 10.1 如果任务队列中还有线程，短暂暂停一下
        while (!TASK_QUEUE.isEmpty()) {
            try {
                Thread.sleep(10_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        synchronized (WORKER_THREAD_QUEUE) {
            // 10.2 如果任务队列中没有线程，但是在WORKER_THREAD_QUEUE中还有线程
            int workerSize = WORKER_THREAD_QUEUE.size();
            while (workerSize > 0) {
                for (WorkerThread workerThread : WORKER_THREAD_QUEUE) {
                    // 任务队列中没有线程，也有可能有线程被阻塞了，对应上面wait方法处
                    if (workerThread.getStatus() == WorkerThreadStatus.BLOCKED) {
                        // 当处于wait时，可使用interrupt方法，中断wait
                        workerThread.interrupt();

                        // 另外一种情况：workerThread已经被唤醒了，往下执行了，调用interrupt无效，直接调用close方法
                        workerThread.close();
                        workerSize--;
                    }
                }
            }
        }
        System.out.println("The Thread Pool Shutdown...");
        // 10.3 而且，如果关闭了线程池，则不能再往线程池中提交任务了，定义一个标记线程池是否被关闭的状态
        this.isDestroy = true;
    }

    // 10.4 定义一个标记线程池是否被关闭的状态
    private volatile boolean isDestroy = false;

    // 11 添加扩容机制的三个参数
    private int min;
    private int active;
    private int max;

    // 12 因为线程池也参与活跃线程、最大线程数的管理，它同时也是一个线程，也继承Thread类，并重写run方法
    @Override
    public void run() {
        while (!isDestroy) {
            System.out.printf("Pool MIN [%d], ACTIVE [%d], MAX [%d], Current Size [%d], Task Queue Size [%d]\n",
                    this.min, this.active, this.max, this.size, TASK_QUEUE.size());
            try {
                Thread.sleep(5_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 扩容逻辑
            // 当TASK_QUEUE大小大于active且当前运行中的线程数量小于active，则将size扩容至active，实际上就是增加WORKER_THREAD_QUEUE数量
            if (TASK_QUEUE.size() > active && size < active) {
                for (int i = size; i < active; i++) {
                    createWorkerThread();
                }
                System.out.println("Thread Pool size increase to active [" + active + "]");
                this.size = active;
            }

            // 当TASK_QUEUE大小大于max且当前运行中的线程数量小于max，则将size扩容至max
            if (TASK_QUEUE.size() > max && size < max) {
                for (int i = size; i < max; i++) {
                    createWorkerThread();
                }
                System.out.println("Thread Pool size increase to max [" + max + "]");
                this.size = max;
            }

            synchronized (WORKER_THREAD_QUEUE) {
                // 自动降容逻辑，如果当前没有任务了，且当前运行中的线程数量大于active的话，自动降容至active，实际上就是降低WORKER_THREAD_QUEUE数量
                if (TASK_QUEUE.isEmpty() && size > active) {
                    // 需要释放的线程数量
                    int release = size - active;
                    for ( Iterator<WorkerThread> iterator = WORKER_THREAD_QUEUE.iterator(); iterator.hasNext(); ) {
                        if (release <= 0) {
                            break;
                        }
                        WorkerThread workerThread = iterator.next();
                        workerThread.close();
                        workerThread.interrupt();
                        iterator.remove();
                        release--;
                    }
                    System.out.println("Thread Pool size decrease to active [" + active + "]");
                    this.size = active;
                }
            }
        }
    }
}