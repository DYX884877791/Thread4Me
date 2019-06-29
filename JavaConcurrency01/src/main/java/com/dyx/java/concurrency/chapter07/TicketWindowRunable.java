package com.dyx.java.concurrency.chapter07;

public class TicketWindowRunable implements Runnable {

    //每日最大的号码
    private static final int MAX = 500;

    //当前的号码
    private int index = 1;

    @Override
    public void run() {
        while (true) {
            if (index > MAX) {     //.........1
                break;
            }
            // 当引入线程休眠时，输出的数据会超出500，因为此时有数据同步方面的问题，此时引入synchronize关键字来进行数据之间的同步
            try {
                Thread.sleep(5L);      //.........2
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "当前的号码是：" + index++);   // ...........3
        }
    }
}

/**
 * 假设3个线程都运行到第一步，并假设此时的index已经是500了，所以在第一步3个线程都判断为false即不会退出，都会往下执行，此时线程1、
 * 线程2、线程3执行到第二步，都开始休眠，但醒来的时间有先后顺序，假设线程1先醒来，线程2其次，线程3最后醒来，则线程1会执行输出操作，
 * 输出500，并将index加一，index变成501，然后线程2再执行，因为此前已经判断为true了，所以线程2也会输出，输出501，并将index加一，
 * 变为502，最后线程3醒来，执行输出，输出502，index加一变成503，在index变为503时，后面线程1、2、3判断为true都会退出了
 *
 * 某一个值输出被跳过的原因：--------------------------
 * 线程1、线程2都执行到第三步，假设此时index是492，线程1刚把index加上1，index变为493，但还没来得及输出492，（自加1操作在输出操作之前，但是返回的还是492），
 * 此时index是493，随即线程2获得cpu执行权，拿到index为493，将index再次加1，变为494，然后输出493，输出的还是index自加1之前的值，
 * 随后cpu又执行线程1，线程1输出的还是index，但此时index已经被线程2加1操作变为494，所以输出494，即492被跳过输出
 *
 * 同一个值被输出两遍的原因：--------------------------
 * 线程1、线程2都执行到第三步加1操作，假设此时index是495，线程1执行index+1，但还没有给index赋值，此时index还是495，随即线程2获得
 * cpu执行权，拿到index是495，执行加1操作，index变为496，随后线程1又获取执行权，获得index为496，并输出index为496，最后线程2执行，
 * 也输出index为496
 *
 */