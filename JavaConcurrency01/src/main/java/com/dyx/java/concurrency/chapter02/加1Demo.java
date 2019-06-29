package com.dyx.java.concurrency.chapter02;

/**
 * 加1Demo
 *
 * @auther: mac
 * @since: 2019-06-29 11:00
 */
public class 加1Demo {

    public static void main(String[] args) {

        int i = 1;
        int j = 1;

        while (i < 50) {
            System.out.println("i====>" + (i++));

        }

        while (j < 50) {
            System.out.println("j====>" + (++j));
        }

    }
}
