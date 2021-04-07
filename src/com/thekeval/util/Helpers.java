package com.thekeval.util;

import com.thekeval.Models.*;

import static com.thekeval.util.Constants.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Helpers {

    // variable declarations
    private static Helpers objHelpers = null;
    public Scanner scan = new Scanner(System.in).useDelimiter("\n");

    // constructor
    public Helpers() {
        // Initialize
    }

    // method to get the singleton object of this 'Helpers' class
    public static Helpers getInstance() {
        if (objHelpers == null)
            objHelpers = new Helpers();

        return objHelpers;
    }


    // region registration methods

    public ArrayList<CustomerDetails> registerMultipleUsers() {
        ArrayList<CustomerDetails> customers = new ArrayList<>();

        boolean again = false;
        do {
            customers.add(registerUser());
            print("Would you like to register more user? y/n");
            again = getString().equalsIgnoreCase("y");
        }
        while (again);

        return customers;
    }

    private CustomerDetails registerUser() {
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

    // endregion

    // region creating & adding bank accounts, generating account number

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

        } while (getString().equalsIgnoreCase("y"));

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
            } else {
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
        DataModel savedData = FileUtils.getInstance().getData();
        if (savedData == null) {
            accNo = Integer.parseInt(String.format("%03d", 1));
        } else {
            for (CustomerDetails cust : savedData.getCustomers()) {

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

    // endregion

    // region Login related

    public CustomerDetails customerLogin() {
        CustomerDetails customer;
        boolean again = false;
        do {
            customer = tryLogin();
            if (customer != null) {
                print("Welcome " + customer.getName());
                again = false;
            } else {
                print("Incorrect name or password. Would you like to try again? y/n");
                again = getString().equalsIgnoreCase("y");
            }
        }
        while (again);

        return customer;
    }

    private CustomerDetails tryLogin() {
        CustomerDetails customer = null;

        print("Enter your name:");
        String name = getString();
        print("Enter your password:");
        String pass = getString();

        for (var cust : Constants.customers.getCustomers()) {
            if (cust.getName().equalsIgnoreCase(name) && cust.getPassword().equalsIgnoreCase(pass)) {
                customer = cust;
                break;
            }
        }

        return customer;
    }

    // endregion

    // region Transactions related

    public void showAndPerformTransactions() {
        int userChoice = -1;
        do {
            print(transactionMenu);
            userChoice = getInt();

            switch (userChoice) {
                case 0: // exit
                    loggedInCustomer = null;
                    print("Logout successful");
                    break;

                case 1: // display current balance
                    displayBalance(loggedInCustomer.getAccounts());
                    userChoice = -1;     // set -1 to again show the transaction menu
                    break;

                case 2: // deposit money
                    break;

                case 3: // withdraw money
                    print("Please enter the amount to withdraw: ");
                    double amount = getDouble();
                    drawMoney(loggedInCustomer.getAccounts(), amount);

                    FileUtils.getInstance().updateData();
                    userChoice = -1;
                    break;

                case 4: // transfer money to other bank account
                    break;

                case 5: // pay electricity bills
                    break;

                case 6: // pay credit card bills
                    break;

                case 7: // add new bank account
                    break;

                case 8: // show or change customer details
                    break;

                default:
                    print("Incorrect input. Please enter valid action number");
                    userChoice = -1;
                    break;
            }

        } while (userChoice == -1);
    }

    private void displayBalance(CustomerAccounts accounts) {
        if (accounts != null) {

            if (accounts.getSavingAcc() != null) {
                print("Balance in Saving account is: " + String.format("%.2f", accounts.getSavingAcc().getAccountBalance()));
            }

            if (accounts.getSalaryAcc() != null) {
                print("Balance in Salary account is: " + String.format("%.2f", accounts.getSalaryAcc().getAccountBalance()));
            }

            if (accounts.getFdAcc() != null) {
                print("Balance in Fixed Deposit account is: " + String.format("%.2f", accounts.getFdAcc().getAccountBalance()));
            }

        }
    }

    private void drawMoney(CustomerAccounts accounts, double money) {
        if (accounts == null)
            return;

        SavingAccount savAcc = null;
        SalaryAccount salAcc = null;
        FixedDepositAccount fdAcc = null;

        String str = "From which account would you like to draw money?\n";
        if (accounts.getSavingAcc() != null) {
            str += "1 - Savings Account\n";
            savAcc = accounts.getSavingAcc();
        }

        if (accounts.getSalaryAcc() != null) {
            str += "2 - Salary Account\n";
            salAcc = accounts.getSalaryAcc();
        }

        if (accounts.getFdAcc() != null) {
            str += "3 - Fixed Deposit Account\n";
            fdAcc = accounts.getFdAcc();
        }

        str += "press 0 to go back to previous menu";

        int userChoice = -1;
        do {
            print(str);
            userChoice = getInt();

            switch (userChoice) {
                case 0: // go back to previous menu
                    break;

                case 1: // saving account
                    if (savAcc != null) {
                        if (savAcc.getAccountBalance() > money) {
                            print("You withdraw " + money + " amount");
                            print("New balance in savings account is " + savAcc.deductBalance(money));
                        }
                        else {
                            print("no sufficient balance");
                        }
                    }
                    break;

                case 2: // salary account
                    if (salAcc != null) {
                        if (salAcc.getAccountBalance() > money) {
                            print("You withdraw " + money + " amount");
                            print("New balance in savings account is " + salAcc.deductBalance(money));
                        }
                        else {
                            print("no sufficient balance");
                        }
                    }
                    break;

                case 3: // fixed deposit account
                    if (fdAcc != null) {
                        if (fdAcc.getAccountBalance() > money) {
                            print("You withdraw " + money + " amount");
                            print("New balance in savings account is " + fdAcc.deductBalance(money));
                        }
                        else {
                            print("no sufficient balance");
                        }
                    }
                    break;

                default:
                    print("Invalid input. Please try again");
                    userChoice = -1;
                    break;
            }

        } while (userChoice == -1);

    }


    // endregion

    // region Miscellaneous methods

    public void printData() {
        String dataString = FileUtils.getInstance().getJsonString(Constants.customers);
        print("data as JSON: " + dataString);
        print("data as toString: " + Constants.customers.toString());
    }

    public void print(String str) {
        System.out.println(str);
    }

    // endregion


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
            } else {
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
