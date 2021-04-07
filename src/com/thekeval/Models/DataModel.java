package com.thekeval.Models;

import java.util.ArrayList;

public class DataModel {
    private ArrayList<CustomerDetails> customers;

    public DataModel(ArrayList<CustomerDetails> customers) {
        this.customers = customers;
    }

    public void replaceCustomer(CustomerDetails customer) {
        for (int i = 0; i<customers.size(); i++) {
            if (customer.getName().equalsIgnoreCase(customers.get(i).getName())) {
                customers.set(i, customer);
                break;
            }
        }
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
