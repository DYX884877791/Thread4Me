package com.dyx.java.concurrency.chapter02.DesignPattern;


/**
 * 根据工资和奖金及其分别的税率计算所要交的税费
 */
public class TaxCalculator {

    private final double salary;

    private final double bonus;

    public TaxCalculator(double salary, double bonus) {
        this.salary = salary;
        this.bonus = bonus;
    }

    protected double calcTax() {
        return 0.0d;
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
}
