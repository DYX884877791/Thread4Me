package com.dyx.java.concurrency.chapter06.CloseThreadDemo;

/**
 * CloseThreadForcibly
 * 暴力结束线程
 * @auther: mac
 * @since: 2019-06-22 19:17
 */
public class CloseThreadForcibly {

    public static void main(String[] args) {

        ThreadService threadService = new ThreadService();

        long startTime = System.currentTimeMillis();

        threadService.execute(() -> {

            while (true) {
                // load a very heavy resource...
                // 这里使用死循环模拟耗时的操作
            }

            // 使用下面代码模拟不耗时的操作
//            int count = 0;
//            while (count < 100) {
//                count++;
//            }
        });

        // 设置五秒钟以后结束线程，对于耗时操作来说，会被暴力结束，对于不耗时操作来说，会自动结束
        threadService.shutdown(5_000L);

        long endTime = System.currentTimeMillis();

        System.out.println( endTime - startTime );
    }
}
