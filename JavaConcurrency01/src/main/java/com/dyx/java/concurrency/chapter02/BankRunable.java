package com.dyx.java.concurrency.chapter02;

public class BankRunable {
    public static void main(String[] args) {


        /**
         * 此处不管定义了多少个线程，每个线程与逻辑部分分离开
         */
        final TicketWindowRunable ticketWindowRunable = new TicketWindowRunable();
        Thread ticketWindow1 = new Thread(ticketWindowRunable,"一号柜台");
        Thread ticketWindow2 = new Thread(ticketWindowRunable,"二号柜台");
        Thread ticketWindow3 = new Thread(ticketWindowRunable,"三号柜台");
        ticketWindow1.start();
        ticketWindow2.start();
        ticketWindow3.start();
    }
}
