package com.dyx.java.concurrency.chapter03;

/**
 * 测试StackSize的大小
 */
public class CreateThread4 {

    private static int count = 0;

    public static void main(String[] args) {

        //构造函数指定了StackSize时，
        Thread thread = new Thread(null,new Runnable() {
            @Override
            public void run() {


                try {
                    add(0);
                } catch (Throwable e) {
                    e.printStackTrace();
                    System.out.println("count===>" + count);
                }

                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            public void add(int i) {
                ++count;
                add(i++);
            }

        },"Thread with StackSize",1 << 24);
        //当指定了StackSize,明显看到递归次数增多了
        thread.start();
    }
}
