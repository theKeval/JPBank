package com.thekeval.Models;

public class SalaryAccount extends BankAccount {

    private String employer;
    private double monthlySalary;

    public SalaryAccount(String accountNo, double accountBalance, String employer, double monthlySalary) {
        super(accountNo, accountBalance);
        this.employer = employer;
        this.monthlySalary = monthlySalary;
    }

    // region getters & setters
    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    // endregion


    @Override
    public String toString() {
        return "SalaryAccount{" +
                "accountNo='" + accountNo + '\'' +
                ", accountBalance=" + accountBalance +
                ", employer='" + employer + '\'' +
                ", monthlySalary=" + monthlySalary +
                '}';
    }
}
