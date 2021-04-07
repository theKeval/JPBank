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
            print("\nWelcome to Swift Bank. Please register yourself to get started");
            ArrayList<CustomerDetails> users = Helpers.getInstance().registerMultipleUsers();

            DataModel _data = new DataModel(users);
            String dataJson = FileUtils.getInstance().getJsonString(_data);
            FileUtils.getInstance().saveData(dataJson);
        } else {
            // use saved data from 'data' variable
        }

        print("\nWelcome to JP Bank. What would you like to do today?");

        int userChoice = -1;
        do {
            print("1 - Register new user\n2 - Login existing user\n9 - Show saved data\n0 - Exit\nNote: To perform transactions, you need to login");
            userChoice = Helpers.getInstance().getInt();

            switch (userChoice) {
                case 0:
                    print("Thank you for using Swift Bank. See you next time! Bye..");
                    break;

                case 1:

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

                case 2:
                    break;

                case 9:
                    break;

                default:
                    break;
            }

        } while (userChoice == -1);

    }


    public static void print(String str) {
        System.out.println(str);
    }

}
