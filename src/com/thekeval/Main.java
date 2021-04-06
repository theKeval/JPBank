package com.thekeval;

import com.thekeval.Models.DataModel;

import static com.thekeval.util.FileUtils.getData;

public class Main {

    public static void main(String[] args) {
	    // write your code here
        System.out.println("Welcome to JPBank");
        DataModel data = getData();
        if (data == null) {
            // first time program run - no saved data
        }
        else {
            // use saved data from 'data' variable
        }

    }
}
