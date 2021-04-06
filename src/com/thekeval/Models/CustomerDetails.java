package com.thekeval.Models;

public class CustomerDetails {
    private String name;
    private String contactNo;
    private String address;
    private String password;
    private CustomerAccounts accounts;

    public CustomerDetails(String name, String contactNo, String address, String password, CustomerAccounts accounts) {
        this.name = name;
        this.contactNo = contactNo;
        this.address = address;
        this.password = password;
        this.accounts = accounts;
    }

    // region getters & setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CustomerAccounts getAccounts() {
        return accounts;
    }

    public void setAccounts(CustomerAccounts accounts) {
        this.accounts = accounts;
    }

    // endregion

    public void addBankAccount(CustomerAccounts _accounts) {
        this.accounts = _accounts;
    }

    @Override
    public String toString() {
        return "CustomerDetails{" +
                "name='" + name + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}
