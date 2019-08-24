package com.dyx.java.concurrency.chapter13;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 线程池，自己实现一个简易版的线程池
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
public class CustomerSimpleThreadPool2 {

    // 1 初始化时线程池中的线程数量
    private final int size;

    // 1.1 线程池中的线程的默认数量
    private final static int DEFAULT_SIZE = 10;

    // 2 创建线程池时指定线程数量
    public CustomerSimpleThreadPool2(int size) {
        this.size = size;
        // 2.1 调用线程池构造器时需要对线程池进行初始化
        init();
    }

    // 2.2 如果创建时没有指定线程数量的话，给它分配默认大小
    public CustomerSimpleThreadPool2() {
        this(DEFAULT_SIZE);
    }

    // 2.3 线程池的初始化方法
    private void init() {
        // 2.4 当线程池初始化时，创建工作线程
        for (int i = 0 ; i < size; i++) {
            createWorkerThread();
        }
    }

    // 2.5 往线程池提交任务的方法，将添加的任务放在任务队列中
    public void submit(Runnable runnable) {
        // 因为是任务队列的写操作，工作线程是读操作，所以加锁
        synchronized (TASK_QUEUE) {
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
}
