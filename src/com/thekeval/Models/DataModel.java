package com.thekeval.Models;

import java.util.ArrayList;

public class DataModel {
    private ArrayList<CustomerDetails> customers;

    public DataModel(ArrayList<CustomerDetails> customers) {
        this.customers = customers;
    }

    // region getters & setters
    public ArrayList<CustomerDetails> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<CustomerDetails> customers) {
        this.customers = customers;
    }

    // endregion

    @Override
    public String toString() {
        return "DataModel{" +
                "customers=" + customers +
                '}';
    }
}
