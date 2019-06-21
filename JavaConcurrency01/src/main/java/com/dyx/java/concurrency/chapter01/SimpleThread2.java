package com.dyx.java.concurrency.chapter01;

/**
 * 新建一个线程，并使用jconsole工具观察
 */
public class SimpleThread2 {

    public static void main(String[] args) {

        Thread thread1 = new Thread("read-thread"){

            public void run(){
                //Thread.currentThread()表示当前run方法所表示的线程
                System.out.println("2"+Thread.currentThread());
                readDataFromDB();
            }

        };

        thread1.start();
        System.out.println("1"+Thread.currentThread());
        //start()表示启动线程，表明上面的thread1线程是由main线程启动的,
        //即main线程是thread1线程的父线程
        //当调用start方法的时候，此时至少有两个线程，一个是调用start方法的线程，此处即main线程，一个是执行run方法的线程，
        //      即start最终要作用于的那个线程
        //而且不能两次调用start方法，会抛出IllegalThreadStateException异常

        //start方法间接地调用了run方法，=====>模板方法设计模式
        //如果直接调用run方法,则thread1线程只是在创建状态中，不会真正地运行，
        //只会运行run方法
//        thread1.run();


        new Thread("write-thread"){
            public void run(){
                writeDataToFile();
            }
        }.start();
    }

    public static void readDataFromDB() {
        System.out.println("read data from database");
        try {
            Thread.sleep(30000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("read data finished");
    }

    public static void writeDataToFile() {
        System.out.println("write data to file");
        try {
            Thread.sleep(20000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("write data finished");
    }
}


