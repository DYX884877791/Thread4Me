package com.dyx.java.concurrency.chapter03;


/**
 * StackSize构造函数，详情请参照JVM
 */
public class CreateThread3 {

    //该成员变量位于方法区
    private int i = 0;

    //该成员变量位于方法区，其引用的值位于堆中
    private byte[] bytes = new byte[1024];


    //计数器
    private static int count = 0;


    //JVM will create a thread named "main"
    public static void main(String[] args) {

        //该局部变量位于虚拟机栈（栈帧的局部变量表中）
        int j = 0;

        //StackSize指定虚拟机栈的容量,因为此处main线程是JVM创建的，无法指定StackSize的大小，所以另创建一个Thread并指定其的StackSize，见CreateThread4

        //模拟
        try {
            add(0);
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println("count===>" + count);
        }
    }

    //此处递归调用，会造成栈溢出
    private static void add(int i) {
        ++count;
        add(i++);
    }
}
