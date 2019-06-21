package com.dyx.java.concurrency.chapter02.DesignPattern;

public class TaxCalculatorMain {

    /**
     * 缺点：税率是变化的，以及计算方法也是变化的，
     * 并且有税率的控制以及计算方法都在这个Main类里，
     * 更改一个势必会造成大的改动
     * 如果后续方法要该改变的话，需要重新修改代码，
     * 即不同的税率、计算方法对应于不同的策略
     *
     * @param args
     */
    public static void main(String[] args) {
        TaxCalculator taxCalculator = new TaxCalculator(10000D,2000D) {
            @Override
            public double calcTax() {
                return getSalary() * 0.1 + getBonus() * 0.15;
            }
        };

        double tax = taxCalculator.calculate();

        System.out.println(tax);
    }
}
