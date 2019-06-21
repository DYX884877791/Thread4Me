package com.dyx.java.concurrency.chapter02.DesignPattern;

public class TaxCalculatorMainByStrategy {

    /**
     * 使用Strategy重构
     * 当计算方法有变化时，只需要新的实现类，并注入到TaxCalculatorByStrategy中
     * @param args
     */
    public static void main(String[] args) {
        TaxCalculatorByStrategy taxCalculatorByStrategy = new TaxCalculatorByStrategy(10000D,2000D);

        CalculatorStrategy calculatorStrategy = new CalculatorStrategyImplOne();

        taxCalculatorByStrategy.setCalculatorStrategy(calculatorStrategy);

        System.out.println(taxCalculatorByStrategy.calculate());
    }
}
