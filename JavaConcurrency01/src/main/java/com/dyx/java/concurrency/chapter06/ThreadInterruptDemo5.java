package com.dyx.java.concurrency.chapter06;

/**
 * ThreadInterruptDemo
 * interrupt方法。。。
 * 比较isInterrupted方法与interrupted方法
 * @auther: mac
 * @since: 2019-06-22 15:03
 */
public class ThreadInterruptDemo5 {

    public static void main(String[] args) {
        /**
         * interrupted 是作用于当前线程，并清除中断状态
         * isInterrupted 是作用于调用该方法的线程对象所对应的线程，不会清除中断状态
         * （线程对象对应的线程不一定是当前运行的线程。例如我们可以在A线程中去调用B线程对象的isInterrupted方法。）
         * 这两个方法最终都会调用同一个方法-----isInterrupted( Boolean 参数)，，只不过参数固定为一个是true，一个是false；
         *
         *
         *
         *
         * interrupted()测试的是"当前的线程"的中断状态。而isInterrupted()测试的是调用该方法的对象所表示的线程。
         * 一个是静态方法（它测试的是当前线程的中断状态），一个是实例方法（它测试的是实例对象所表示的线程的中断状态）。
         */

        Thread thread1 = new Thread(){

            int count1 = 0;
            public void run() {
                while (count1 < 500) {
                    // isInterrupted不会清除中断状态，所以在thread1线程被中断后，输出结果会发生改变
                    // 因为thread1线程在main线程之前运行，第一次运行时输出为false，当main线程中断thread1线程时，之后的输出为true
                    System.out.println("thread1 [" + count1 + "] is interrupted??? >>>  " + this.isInterrupted());
                    count1++;
                }
            }
        };

        Thread thread2 = new Thread(){

            int count2 = 0;
            public void run() {
                while (count2 < 500) {
                    //
                    // 此处interrupted测试的是thread2这个线程，与isInterrupted的不同见Demo6
                    // interrupted则会清除中断状态，所以在thread2线程被中断后，输出结果不会发生改变，
                    // 因为thread2线程在main线程之前运行，第一次运行时输出为false，并且会清除状态，重新置为false，
                    // 当main线程中断thread2线程时，之后的输出还是为false
                    System.out.println("thread2 [" + count2 + "]  is interrupted??? ===  " + Thread.interrupted());
                    count2++;
                }
            }
        };

        thread1.start();
        thread2.start();

        try {
            Thread.sleep(2L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread1.interrupt();
        thread2.interrupt();

    }
}
