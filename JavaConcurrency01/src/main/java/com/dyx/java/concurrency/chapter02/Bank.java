package com.dyx.java.concurrency.chapter02;

public class Bank {

    public static void main(String[] args) {

        TicketWindow ticketWindow1 = new TicketWindow("一号柜台");
        ticketWindow1.start();

        TicketWindow ticketWindow2 = new TicketWindow("二号柜台");
        ticketWindow2.start();

        TicketWindow ticketWindow3 = new TicketWindow("三号柜台");
        ticketWindow3.start();
        //结果出乎意料，因为公共的变量index是各个对象独立的，应该是各个柜台共享同一个号码，（index）
    }

}
