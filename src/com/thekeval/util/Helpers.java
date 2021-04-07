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
                    print("Please add the amount to deposit: ");
                    double amountToDeposit = getDouble();
                    depositMoney(loggedInCustomer.getAccounts(), amountToDeposit);

                    FileUtils.getInstance().updateData();
                    userChoice = -1;
                    break;

                case 3: // withdraw money
                    print("Please enter the amount to withdraw: ");
                    double amountToWithdraw = getDouble();
                    drawMoney(loggedInCustomer.getAccounts(), amountToWithdraw);

                    FileUtils.getInstance().updateData();
                    userChoice = -1;
                    break;

                case 4: // transfer money to other bank account
                    transferMoney(loggedInCustomer.getAccounts());

                    FileUtils.getInstance().updateData();
                    userChoice = -1;
                    break;

                case 5: // pay electricity bills
                    payUtilityBills(loggedInCustomer.getAccounts(), UtilityBills.electricityBill);

                    FileUtils.getInstance().updateData();
                    userChoice = -1;
                    break;

                case 6: // pay credit card bills
                    payUtilityBills(loggedInCustomer.getAccounts(), UtilityBills.creditCardBill);

                    FileUtils.getInstance().updateData();
                    userChoice = -1;
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

    private void depositMoney(CustomerAccounts accounts, double money) {
        if (accounts == null)
            return;

        SavingAccount savAcc = null;
        SalaryAccount salAcc = null;
        FixedDepositAccount fdAcc = null;

        var str = "In which account would you like to deposit?\n";
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
                        print("You deposited " + money + " amount");
                        print("New balance in savings account is " + savAcc.addBalance(money));
                    }
                    break;

                case 2: // salary account
                    if (salAcc != null) {
                        print("You deposited " + money + " amount");
                        print("New balance in salary account is " + salAcc.addBalance(money));
                    }
                    break;

                case 3: // fixed deposit account
                    if (fdAcc != null) {
                        print("You deposited " + money + " amount");
                        print("New balance in fixed deposit account is " + fdAcc.addBalance(money));
                    }
                    break;

                default:
                    print("Invalid input. Please try again");
                    userChoice = -1;
                    break;
            }

        } while (userChoice == -1);

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
                            print("New balance in salary account is " + salAcc.deductBalance(money));
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
                            print("New balance in fixed deposit account is " + fdAcc.deductBalance(money));
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

    // region transfer money related methods

    private void transferMoney(CustomerAccounts accounts) {
        if (accounts == null)
            return;

        SavingAccount savAcc = null;
        SalaryAccount salAcc = null;
        FixedDepositAccount fdAcc = null;

        String str = "From which account would you like to transfer?\n";
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
                        print("You have " + String.format("%.2f", savAcc.getAccountBalance()) + " amount in your Saving account.\nHow much money would you like to transfer?");
                        double money = getDouble();

                        if (money < savAcc.getAccountBalance()) {
                            addToBeneficiary(money);
                            double amount = savAcc.deductBalance(money);
                            print("New balance in " + loggedInCustomer.getName() + "'s saving account is " + amount);
                            print("Transfer Successful !");
                        }

                    }
                    break;

                case 2: // salary account
                    if (salAcc != null) {
                        print("You have " + String.format("%.2f", salAcc.getAccountBalance()) + " amount in your Salary account.\nHow much money would you like to transfer?");
                        double money = getDouble();

                        if (money < salAcc.getAccountBalance()) {
                            addToBeneficiary(money);
                            double amount = salAcc.deductBalance(money);
                            print("New balance in " + loggedInCustomer.getName() + "'s salary account is " + amount);
                            print("Transfer Successful !");
                        }

                    }
                    break;

                case 3: // fd account
                    if (fdAcc != null) {
                        print("You have " + String.format("%.2f", fdAcc.getAccountBalance()) + " amount in your Fixed deposit account.\nHow much money would you like to transfer?");
                        double money = getDouble();

                        if (money < fdAcc.getAccountBalance()) {
                            addToBeneficiary(money);
                            double amount = fdAcc.deductBalance(money);
                            print("New balance in " + loggedInCustomer.getName() + "'s fixed deposit account is " + amount);
                            print("Transfer Successful !");
                        }

                    }
                    break;

                default:
                    print("Invalid input. please try again.");
                    userChoice = -1;
                    break;
            }

        } while (userChoice == -1);

    }

    public void addToBeneficiary(double money) {

        print("Enter the account number in which you would like to transfer money.");
        print("Available account numbers are: " + getAvailableAccountNumbers());
        String accToTransfer = getString();

        for (CustomerDetails item : customers.getCustomers()) {

            if (item.getAccounts().getSavingAcc() != null) {
                if (item.getAccounts().getSavingAcc().getAccountNo().equalsIgnoreCase(accToTransfer)) {
                    double amount = item.getAccounts().getSavingAcc().addBalance(money);
                    print("New balance in " + item.getName() + "'s saving account is: " + amount);
                    break;
                }
            }

            if (item.getAccounts().getSalaryAcc() != null) {
                if (item.getAccounts().getSalaryAcc().getAccountNo().equalsIgnoreCase(accToTransfer)) {
                    double amount = item.getAccounts().getSalaryAcc().addBalance(money);
                    print("New balance in " + item.getName() + "'s saving account is: " + amount);
                    break;
                }
            }

            if (item.getAccounts().getFdAcc() != null) {
                if (item.getAccounts().getFdAcc().getAccountNo().equalsIgnoreCase(accToTransfer)) {
                    double amount = item.getAccounts().getFdAcc().addBalance(money);
                    print("New balance in " + item.getName() + "'s saving account is: " + amount);
                    break;
                }
            }

        }

    }

    public ArrayList<String> getAvailableAccountNumbers() {
        ArrayList<String> accNos = new ArrayList<>();

        if (customers != null) {

            for (CustomerDetails cust : customers.getCustomers()) {
                if (cust.getAccounts().getSavingAcc() != null) {
                    accNos.add(cust.getAccounts().getSavingAcc().getAccountNo());
                }

                if (cust.getAccounts().getSalaryAcc() != null) {
                    accNos.add(cust.getAccounts().getSalaryAcc().getAccountNo());
                }

                if (cust.getAccounts().getFdAcc() != null) {
                    accNos.add(cust.getAccounts().getFdAcc().getAccountNo());
                }

            }

        }

        return accNos;
    }

    // endregion

    // region Utility bill payment related

    enum UtilityBills {
        electricityBill,
        creditCardBill
    }

    public void payUtilityBills(CustomerAccounts accounts, UtilityBills utilityType) {
        if (accounts == null)
            return;

        SavingAccount savAcc = null;
        SalaryAccount salAcc = null;
        FixedDepositAccount fdAcc = null;

        String str = "From which account would you like to pay your " + (utilityType.equals(UtilityBills.electricityBill) ? "Electricity Bill" : "Credit Card Bill") + "\n";

        if (accounts.getSavingAcc() != null) {
            str += "1 - Saving Account\n";
            savAcc = accounts.getSavingAcc();
        }

        if (accounts.getSalaryAcc() != null) {
            str += "1 - Salary Account\n";
            salAcc = accounts.getSalaryAcc();
        }

        if (accounts.getFdAcc() != null) {
            str += "1 - Fixed Deposit Account\n";
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
                        processUtilityPayment(utilityType, "Saving account", savAcc);
                    }

                case 2: // salary account
                    if (salAcc != null) {
                        processUtilityPayment(utilityType, "Salary account", salAcc);
                    }

                case 3: // FD account
                    if (fdAcc != null) {
                        processUtilityPayment(utilityType, "Fixed Deposit account",fdAcc);
                    }

                default:
                    print("Invalid input. please try again");
                    userChoice = -1;
            }

        } while(userChoice == -1);

    }

    public void processUtilityPayment(UtilityBills utilityType, String accName, BankAccount acc) {
        print("Available balance in your " + accName + " is " + acc.getAccountBalance() + ". Enter the amount of bill you'd like to pay:");

        boolean again = false;
        do {
            double billAmount = getDouble();
            if (billAmount < acc.getAccountBalance()) {
                print("You have successfully paid your " + (utilityType.equals(UtilityBills.electricityBill) ? "Electricity Bill" : "Credit Card Bill"));
                print("new balance in your " + accName + " is " + acc.deductBalance(billAmount));

                again = false;
            }
            else {
                print("You only have " + acc.getAccountBalance() + " amount in your bank, please enter less amount than that:");
                again = true;
            }

        } while(again);
    }

    // endregion

    

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
