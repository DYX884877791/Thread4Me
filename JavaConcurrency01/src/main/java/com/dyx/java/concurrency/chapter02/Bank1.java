package com.dyx.java.concurrency.chapter02;

public class Bank1 {

    public static void main(String[] args) {

        TicketWindow1 ticketWindow1 = new TicketWindow1("一号柜台");
        ticketWindow1.start();

        TicketWindow1 ticketWindow2 = new TicketWindow1("二号柜台");
        ticketWindow2.start();

        TicketWindow1 ticketWindow3 = new TicketWindow1("三号柜台");
        ticketWindow3.start();
        //此处出现顺序混乱的情况：是因为序号小的线程在没有输出之前，其他线程就已经输出完毕了
    }

}
