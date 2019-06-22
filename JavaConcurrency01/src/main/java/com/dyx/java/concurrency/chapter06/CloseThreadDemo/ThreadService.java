package com.dyx.java.concurrency.chapter06.CloseThreadDemo;

/**
 * ThreadService
 * 暴力结束线程的辅助类
 *      原理：父线程结束，则子线程不一定结束，除非子线程是守护线程；
 *           即如果子线程都是守护线程的话，父线程一结束，子线程也会结束
 *
 *      思路：将要结束的线程的业务逻辑设置为某个父线程的子线程，并设置为守护线程，要结束的时候直接结束父线程，则子线程（业务线程）也会结束
 * @auther: mac
 * @since: 2019-06-23 01:01
 */
public class ThreadService {

    // 父线程
    private Thread parentThread;

    // 父线程的状态标识
    private boolean isFinished;

    /**
     * 执行线程
     * @param task target代表实际的业务逻辑target
     */
    public void execute(Runnable task) {

        parentThread = new Thread() {
            @Override
            public void run() {
                Thread runnerThread = new Thread(task);
                runnerThread.setDaemon(true);
                runnerThread.start();
                System.out.println("Task is running...");

                /**
                 * 因为要确保runnerThread要执行，而且runner是守护线程，
                 * 所以需要在parentThread线程体中调用runner的join方法，让runner先执行完，
                 * parentThread才开始运行后续，否则，parentThread执行完了，作为守护线程的runner根本没有执行，不符合要求
                 */
                try {
                    runnerThread.join();
                    /**
                     * 如果这里如果task的任务很耗时，或者是需要某种资源但一直被阻塞住而没有执行完毕，(join一直没有执行完)
                     * 导致parentThread也没有执行完毕，所以可以通过判断parentThread线程的状态来判断runner的状态
                     *
                     * 并且这里的join的调用者是parentThread线程，如果在某处parentThread被中断了，
                     * 则在中断异常处理中会结束parentThread线程，则其守护线程（这里是runner线程）也会退出，
                     * 所以要结束runner线程的时候，可以直接调用parentThread的interrupt方法
                     *
                     */
                    isFinished = true;// 如果runner执行完毕（join执行完了），可以推断出parentThread线程也执行完了，更新状态
                    System.out.println("parentThread is finished??? >>> " + isFinished);
                } catch (InterruptedException e) {
                    System.out.println("parentThread 被中断了!!! parentThread is finished??? === " + isFinished);
                    // 这里join方法被中断后，isFinished依旧是false，但因为后续没有处理逻辑了，parentThread可以视作"执行完毕"
                    e.printStackTrace();
                }
            }
        };

        parentThread.start();// parentThread线程只是启动runner线程，并无后续操作

    }

    /**
     * 结束线程
     * @param timeout 超时时间：先判断业务线程是否运行完毕（可以通过判断parentThread的状态来确定），如果完毕则直接结束，
     *                否则判断是否超过超时时间，如果没有超过也直接结束，否则暴力结束
     *                那么，如何判断运行完毕？（设置状态标识）
     */
    public void shutdown(long timeout) {
        /**
         * 判断parentThread的状态
         *
         */
        long currentTime = System.currentTimeMillis();
        while (!isFinished) { // 当parentThread没有结束，说明runner也还没有结束
            // 判断超时时间
            if (System.currentTimeMillis() - currentTime >= timeout) { // 已经超时了
                System.out.println("业务线程已经超时，即将暴力结束...");
                parentThread.interrupt();// 中断parentThread线程，parentThread捕获到中断异常，不再执行join，parentThread可以视作"执行完毕"
                break;
            }

            // 当没有超时，不处理

        }

        // 重置状态标识
        isFinished = false;

    }

    /**
     * 核心点：线程A 与 线程B(耗时操作) join，则B要先执行完，才会执行A，除非线程A的join方法被中断，此时A则会先执行完然后结束生命周期，
     *        再将线程B设置为线程A的子线程，并将B设置为守护线程，如果A结束生命周期，则B一定也会结束生命周期
     */
}
