package com.dyx.java.concurrency.chapter01.DesignPattern;

/**
 * 线程启动的设计模式之一：模板方法设计模式
 *
 * 模板设计模式定义：定义一个操作中的算法骨架，将步骤延迟到子类中。
 *
 *     模板设计模式是一种行为设计模式，一般是准备一个抽象类，将部分逻辑以具体方法或者具体的构造函数实现，
 *     然后声明一些抽象方法，这样可以强制子类实现剩余的逻辑。不同的子类以不同的方式实现这些抽象方法，
 *     从而对剩余的逻辑有不同的实现。这就是模板设计模式能达成的功能。
 *
 *     适用于一些复杂操作进行步骤分割、抽取公共部分由抽象父类实现、将不同的部分在父类中定义抽象实现、
 *     而将具体实现过程由子类完成。对于整体步骤很固定，但是某些部分易变，可以将易变的部分抽取出来，供子类实现。
 *
 *     角色：
 *
 *         抽象类：实现模板方法、定义算法骨架
 *
 *         具体类：实现抽象类中的抽象方法，完成特定的算法
 *
 * 线程的启动过程中：调用start()方法，start()方法调用本地start0(),而start0()方法通过JNI最终调用run()方法，
 * run方法逻辑不同最终的执行也不同
 *
 *
 */
public abstract class TemplateMethodDemo {

    public final void print(String message) {
        System.out.println("##########");
        wrapPrint(message);
        System.out.println("##########");

    }
    protected abstract void wrapPrint(String message);
}
