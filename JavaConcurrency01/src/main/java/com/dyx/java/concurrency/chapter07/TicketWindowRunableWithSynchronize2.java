package com.dyx.java.concurrency.chapter07;

/**
 * TicketWindowRunableWithSynchronize2
 *
 * @auther: mac
 * @since: 2019-06-29 16:45
 */
public class TicketWindowRunableWithSynchronize2 implements Runnable {

    //每日最大的号码
    private static final int MAX = 500;

    //当前的号码
    private int index = 1;

    /**
     * 同步方法，但此时只有一个线程一直执行
     *      原因分析：此时的锁对象是this，即执行run方法的那个对象，即TicketWindowRunableWithSynchronize2的实例对象，
     *              即main方法中的ticketWindowRunableWithSynchronize2对象，某个线程抢到了上述对象的锁，执行run方法，
     *              但此时run方法是while循环，则会一直执行，直到while中条件不满足break，该线程的代码逻辑才完毕，释放锁，
     *              随后其他线程抢到锁，来执行，但因为条件判断不满足直接退出，以此类推，所以其他线程没有执行的机会
     */
    @Override
    public synchronized void run() {
        while (true) {
            // 通过命令：javap -c 类名.class 观察其JVM指令

            if (index > MAX) {     //.........1
                break;
            }
            try {
                Thread.sleep(5L);      //.........2
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "当前的号码是：" + index++);   // ...........3
        }
    }
}
