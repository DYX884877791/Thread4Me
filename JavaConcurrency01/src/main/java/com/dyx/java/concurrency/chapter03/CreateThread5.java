package com.dyx.java.concurrency.chapter03;

/**
 * 使用默认StackSize来创建线程
 * 构造Thread的时候传入StackSize代表着该线程占用的StackSize大小，如果没有指定StackSize的大小，则默认是0，0代表着忽略该参数，
 * 该参数会被JNI函数去使用
 * 需要注意：该参数在某些平台上有效，在有些平台上则无效
 *
 */
public class CreateThread5 {

    public static void main(String[] args) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
//            new Thread(() -> {
//                byte[] bytes = new byte[1024 * 1024];
//
//            }).start();
        }
    }

}
