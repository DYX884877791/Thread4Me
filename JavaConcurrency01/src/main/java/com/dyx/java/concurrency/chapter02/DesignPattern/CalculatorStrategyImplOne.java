package com.dyx.java.concurrency.chapter02.DesignPattern;


/**
 * 计算税的策略之一
 */
public class CalculatorStrategyImplOne implements CalculatorStrategy {

    private final static double SALARY_RATE = 0.1D;

    private final static double BONUS_RATE = 0.15D;

    @Override
    public double calculate(double salary, double bonus) {
        return salary * SALARY_RATE + bonus * BONUS_RATE;
    }
}
