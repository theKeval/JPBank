package com.thekeval.Models;

public class SavingAccount extends BankAccount {

    private double minBalance;
    private double interestRate;

    public SavingAccount(String accountNo, double accountBalance, double minBalance, double interestRate) {
        super(accountNo, accountBalance);
        this.minBalance = minBalance;
        this.interestRate = interestRate;
    }

    // region getters and setters
    public double getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(double minBalance) {
        this.minBalance = minBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    // endregion

}
