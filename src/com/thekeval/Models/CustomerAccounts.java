package com.thekeval.Models;

public class CustomerAccounts {
    private SavingAccount savingAcc;
    private SalaryAccount salaryAcc;
    private FixedDepositAccount fdAcc;

    public CustomerAccounts(SavingAccount savingAcc, SalaryAccount salaryAcc, FixedDepositAccount fdAcc) {
        this.savingAcc = savingAcc;
        this.salaryAcc = salaryAcc;
        this.fdAcc = fdAcc;
    }

    // region getters & setters
    public SavingAccount getSavingAcc() {
        return savingAcc;
    }

    public void setSavingAcc(SavingAccount savingAcc) {
        this.savingAcc = savingAcc;
    }

    public SalaryAccount getSalaryAcc() {
        return salaryAcc;
    }

    public void setSalaryAcc(SalaryAccount salaryAcc) {
        this.salaryAcc = salaryAcc;
    }

    public FixedDepositAccount getFdAcc() {
        return fdAcc;
    }

    public void setFdAcc(FixedDepositAccount fdAcc) {
        this.fdAcc = fdAcc;
    }

    // endregion

    @Override
    public String toString() {
        return "CustomerAccounts{" +
                "savingAcc=" + savingAcc +
                ", salaryAcc=" + salaryAcc +
                ", fdAcc=" + fdAcc +
                '}';
    }
}
