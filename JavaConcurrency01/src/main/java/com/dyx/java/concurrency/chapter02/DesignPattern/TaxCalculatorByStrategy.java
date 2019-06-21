package com.dyx.java.concurrency.chapter02.DesignPattern;


/**
 * 根据工资和奖金及其分别的税率计算所要交的税费
 */
public class TaxCalculatorByStrategy {

    private final double salary;

    private final double bonus;

    private CalculatorStrategy calculatorStrategy;

    public TaxCalculatorByStrategy(double salary, double bonus) {
        this.salary = salary;
        this.bonus = bonus;
    }

    private double calcTax() {
        return calculatorStrategy.calculate(salary,bonus);
    }

    public double calculate() {
        return calcTax();
    }

    public double getSalary() {
        return salary;
    }

    public double getBonus() {
        return bonus;
    }

    public CalculatorStrategy getCalculatorStrategy() {
        return calculatorStrategy;
    }

    public void setCalculatorStrategy(CalculatorStrategy calculatorStrategy) {
        this.calculatorStrategy = calculatorStrategy;
    }
}
