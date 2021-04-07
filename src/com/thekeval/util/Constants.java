package com.thekeval.util;

import com.thekeval.Models.CustomerDetails;
import com.thekeval.Models.DataModel;

public class Constants {

    // Constants
    public static String FILE_NAME = "data.json";

    public static DataModel customers = null;
    public static CustomerDetails loggedInCustomer = null;

    public static double SAVING_MIN_BAL = 100.0;
    public static double SAVING_INT_RATE = 6.0;
    public static double FD_INT_RATE = 9.0;
    public static int LAST_ACC_NO = 0;

    public static String mainMenu = "\n1 - Register new user\n2 - Login existing user\n9 - Show saved data\n0 - Exit\nNote: To perform transactions, you need to login";
    public static String transactionMenu = "\nEnter the number associated with the action, to perform that action.\n1 - Display current balance\n2 - Deposit money\n3 - Draw money\n4 - Transfer money to other bank account\n5 - Pay Electricity bill\n6 - Pay Credit card bill\n7 - Add new bank account\n8 - Show or Change customer details\n0 - Logout (go back to previous menu)";

    public static String firstTimeWelcomeMessage = "\nWelcome to Swift Bank. Please register yourself to get started";
    public static String welcomeMessage = "\nWelcome to JP Bank. What would you like to do today?";
    public static String exitMessage = "Thank you for using Swift Bank. See you next time! Bye..";

}
