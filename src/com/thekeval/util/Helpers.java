package com.thekeval.util;

import com.thekeval.Models.*;
import static com.thekeval.util.FileUtils.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Helpers {

    public static Helpers objHelpers = null;
    public Scanner scan = new Scanner(System.in).useDelimiter("\n");

    public Helpers() {
        if (objHelpers == null) {
            objHelpers = new Helpers();
        }
    }

    public void print(String str) {
        System.out.println(str);
    }

    public ArrayList<CustomerDetails> registerMultipleUsers() {
        ArrayList<CustomerDetails> customers = new ArrayList<>();

        boolean again = false;
        do {
            customers.add(registerUser());
            print("Would you like to register more user? y/n");
            again = getString().equals("y");
        }
        while (again);

        return customers;
    }

    public CustomerDetails registerUser() {
        print("Enter your name:");
        String name = getString();
        print("Set your password:");
        String password = getString();
        print("Enter your contact number:");
        String contactNo = getString();
        print("Enter your address/city:");
        String address = getString();

        CustomerAccounts accounts = letAddBankAccounts(null);
        CustomerDetails customer = new CustomerDetails(name, contactNo, address, password, accounts);

        print("Registration successful!");
        return customer;
    }

    public CustomerAccounts letAddBankAccounts(CustomerAccounts _accounts) {
        CustomerAccounts accounts = new CustomerAccounts(null, null, null);
        if (_accounts != null) {
            accounts = _accounts;
        }

        do {
            print("Which account would you like to open?\n1 - Saving account\n2 - Salary account\n3 - Fixed Deposit account");
            int choice = getInt();
            switch (choice) {
                case 1:
                    SavingAccount savingAcc = createSavingAccount();
                    accounts.setSavingAcc(savingAcc);
                    break;

                case 2:
                    SalaryAccount salaryAcc = createSalaryAccount();
                    accounts.setSalaryAcc(salaryAcc);
                    break;

                case 3:
                    FixedDepositAccount fdAcc = createFdAccount();
                    accounts.setFdAcc(fdAcc);
                    break;

                default:
                    print("Sorry, incorrect input!");
                    break;
            }

            print("Would you like to add more bank account? y/n");

        } while (getString().equals("y"));

        return accounts;
    }

    public SavingAccount createSavingAccount() {
        String accNo = generateAccountNumber();
        print("Minimum balance you need to maintain is: " + Constants.SAVING_MIN_BAL + "\nand interest rate is: " + Constants.SAVING_INT_RATE);

        double accBalance = 0;
        boolean repeat;
        print("Enter the account balance you would like to add: ");
        do {
            accBalance = getDouble();
            if (accBalance > Constants.SAVING_MIN_BAL) {
                repeat = false;
            }
            else {
                print("Please enter amount more than " + Constants.SAVING_MIN_BAL);
                repeat = true;
            }

        } while (repeat);

        return new SavingAccount(accNo, accBalance, Constants.SAVING_MIN_BAL, Constants.SAVING_INT_RATE);
    }

    public SalaryAccount createSalaryAccount() {
        String accNo = generateAccountNumber();
        print("Enter the account balance you would like to add:");
        double accBal = getDouble();
        print("Enter the name of your employer: ");
        String employer = getString();
        print("Enter your monthly salary: ");
        double monthlySal = getDouble();

        return new SalaryAccount(accNo, accBal, employer, monthlySal);
    }

    public FixedDepositAccount createFdAccount() {
        String accNo = generateAccountNumber();
        print("Enter the account balance you would like to add:");
        double accBal = getDouble();
        print("Enter the number of months as term duration for FD: ");
        int termDur = getInt();

        print("Interest rate for Fixed Deposit is " + Constants.FD_INT_RATE);

        return new FixedDepositAccount(accNo, accBal, termDur, Constants.FD_INT_RATE);
    }

    public String generateAccountNumber() {
        int accNo = 0;
        int lastAccNo = Constants.LAST_ACC_NO;
        DataModel savedData = objFileUtils.getData();
        if (savedData == null) {
            accNo = Integer.parseInt(String.format("%03d", 1));
        }
        else {
            for (CustomerDetails cust: savedData.getCustomers()) {

                if (cust.getAccounts() != null && cust.getAccounts().getSavingAcc() != null) {
                    SavingAccount _savingAcc = cust.getAccounts().getSavingAcc();
                    if (Integer.parseInt(_savingAcc.getAccountNo()) > lastAccNo) {
                        lastAccNo = Integer.parseInt(_savingAcc.getAccountNo());
                    }
                }

                if (cust.getAccounts() != null && cust.getAccounts().getSalaryAcc() != null) {
                    SalaryAccount _salaryAcc = cust.getAccounts().getSalaryAcc();
                    if (Integer.parseInt(_salaryAcc.getAccountNo()) > lastAccNo) {
                        lastAccNo = Integer.parseInt(_salaryAcc.getAccountNo());
                    }
                }

                if (cust.getAccounts() != null && cust.getAccounts().getFdAcc() != null) {
                    FixedDepositAccount _fdAcc = cust.getAccounts().getFdAcc();
                    if (Integer.parseInt(_fdAcc.getAccountNo()) > lastAccNo) {
                        lastAccNo = Integer.parseInt(_fdAcc.getAccountNo());
                    }
                }

            }
        }

        accNo = lastAccNo + 1;
        Constants.LAST_ACC_NO = accNo;
        return String.format("%03d", accNo);
    }

    // region Scanner Utils - getting user input from command line

    public String getString() {
        String input = scan.next();
        return input;
    }

    public int getInt() {
        int input = 0;
        boolean isValid = false;
        while (!isValid) {
            if (scan.hasNextInt()) {
                input = scan.nextInt();
                isValid = true;
            }
            else {
                System.out.println("Error! Invalid number. Try again.");
            }
            scan.nextLine();
        }
        return input;
    }

    public double getDouble() {
        double dbl = 0.0;
        boolean isValid = false;
        while (!isValid) {
            // If input is number execute this,
            if (scan.hasNextDouble()) {
                dbl = scan.nextDouble();
                isValid = true;
            }
            // If input is not a number execute this block,
            else {
                System.out.println("Error! Invalid number. Try again.");
            }
            scan.nextLine(); // discard any other data
        }
        return dbl;
    }

    // endregion

}
