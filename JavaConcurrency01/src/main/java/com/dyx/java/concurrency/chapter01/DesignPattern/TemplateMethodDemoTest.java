package com.dyx.java.concurrency.chapter01.DesignPattern;

/**
 * 模板方法的测试类
 */
public class TemplateMethodDemoTest{


    public static void main(String[] args) {
        TemplateMethodDemo templateMethodDemo1 = new TemplateMethodDemo() {

            @Override
            protected void wrapPrint(String message) {
                System.out.println("*" + message + "*");
            }
        };

        templateMethodDemo1.print("templateMethodDemo1");


        TemplateMethodDemo templateMethodDemo2 = new TemplateMethodDemo() {

            @Override
            protected void wrapPrint(String message) {
                System.out.println("#" + message + "#");
            }
        };

        templateMethodDemo2.print("templateMethodDemo2");
    }
}
