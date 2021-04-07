package com.thekeval;

import com.thekeval.Models.CustomerDetails;
import com.thekeval.Models.DataModel;
import com.thekeval.util.Constants;
import com.thekeval.util.FileUtils;
import com.thekeval.util.Helpers;

import java.util.ArrayList;

import static com.thekeval.util.FileUtils.*;
import static com.thekeval.util.Helpers.*;
import static com.thekeval.util.Constants.*;

public class Main {


    public static void main(String[] args) {

        DataModel data = FileUtils.getInstance().getData();

        if (data == null) {
            // first time program run - no saved data
            print(firstTimeWelcomeMessage);
            ArrayList<CustomerDetails> users = Helpers.getInstance().registerMultipleUsers();

            customers = new DataModel(users);
            String dataJson = FileUtils.getInstance().getJsonString(customers);
            FileUtils.getInstance().saveData(dataJson);
        } else {
            // use saved data from 'data' variable
            customers = data;
        }

        print(welcomeMessage);

        int userChoice = -1;
        do {
            print(mainMenu);
            userChoice = Helpers.getInstance().getInt();

            switch (userChoice) {
                case 0: // exit
                    print(exitMessage);
                    break;

                case 1: // register users

                    ArrayList<CustomerDetails> users = Helpers.getInstance().registerMultipleUsers();

                    // loop in all registered users to add them in our global 'customers' object
                    for (CustomerDetails user : users) {
                        customers.getCustomers().add(user);
                    }

                    // if customers obj is not null, get the json string
                    var jsonStr = FileUtils.getInstance().getJsonString(customers);

                    // save json to file
                    FileUtils.getInstance().saveData(jsonStr);

                    // reset user choice to show main menu again
                    userChoice = -1;

                break;

                case 2: // login

                    loggedInCustomer = Helpers.getInstance().customerLogin();

                    // if user logged in successfully
                    // then show them the Transaction menu
                    if (loggedInCustomer != null) {
                        Helpers.getInstance().showAndPerformTransactions();
                    }

                    // reset user choice to redirect to main menu
                    userChoice = -1;

                    break;

                case 9: // show data
                    Helpers.getInstance().printData();
                    userChoice = -1;
                    break;

                default:
                    print("Please enter valid choice: ");
                    userChoice = -1;
                    break;
            }

        } while (userChoice == -1);

    }
    
    public static void print(String str) {
        System.out.println(str);
    }

}
