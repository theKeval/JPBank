package com.thekeval;

import com.thekeval.Models.DataModel;
import com.thekeval.util.FileUtils;
import com.thekeval.util.Helpers;

import static com.thekeval.util.FileUtils.*;
import static com.thekeval.util.Helpers.*;

public class Main {



    public static void main(String[] args) {
	    // write your code here
        System.out.println("Welcome to JPBank");
        DataModel data = objFileUtils.getData();
        if (data == null) {
            // first time program run - no saved data
            objHelpers.registerUser();
        }
        else {
            // use saved data from 'data' variable
        }

    }


    public static void print(String str) {
        System.out.println(str);
    }

}
