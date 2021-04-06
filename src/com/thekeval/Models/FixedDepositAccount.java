package com.thekeval.Models;

public class FixedDepositAccount extends BankAccount {
    private int termDuration;
    private double interestRate;

    public FixedDepositAccount(String accountNo, double accountBalance, int termDuration, double interestRate) {
        super(accountNo, accountBalance);
        this.termDuration = termDuration;
        this.interestRate = interestRate;
    }

    // region getters & setters
    public int getTermDuration() {
        return termDuration;
    }

    public void setTermDuration(int termDuration) {
        this.termDuration = termDuration;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    // endregion

}
