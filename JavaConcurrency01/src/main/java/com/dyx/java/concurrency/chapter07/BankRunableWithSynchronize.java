package com.dyx.java.concurrency.chapter07;

/**
 * 多线程数据同步以及synchronize的引入
 */
public class BankRunableWithSynchronize {
    public static void main(String[] args) {


        /**
         * 此处不管定义了多少个线程，每个线程与逻辑部分分离开
         */
        final TicketWindowRunableWithSynchronize ticketWindowRunableWithSynchronize = new TicketWindowRunableWithSynchronize();
        Thread ticketWindow1 = new Thread(ticketWindowRunableWithSynchronize,"一号柜台");
        Thread ticketWindow2 = new Thread(ticketWindowRunableWithSynchronize,"二号柜台");
        Thread ticketWindow3 = new Thread(ticketWindowRunableWithSynchronize,"三号柜台");
        ticketWindow1.start();
        ticketWindow2.start();
        ticketWindow3.start();
    }
}
