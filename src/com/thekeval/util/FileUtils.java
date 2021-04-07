package com.thekeval.util;

import com.google.gson.Gson;
import com.thekeval.Models.CustomerDetails;
import com.thekeval.Models.DataModel;
import static com.thekeval.util.Constants.*;

import java.io.*;

public class FileUtils {

    private static FileUtils objFileUtils = null;
    public static FileUtils getInstance() {
        if (objFileUtils == null)
            objFileUtils = new FileUtils();

        return objFileUtils;
    }

    public FileUtils() {
        // initialize
    }

    public String getJsonString(DataModel data) {
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    public void saveData(String jsonString) {
        try {
            FileWriter fw = new FileWriter(FILE_NAME, false);
            PrintWriter pw = new PrintWriter(fw);

            pw.println(jsonString);
            pw.close();

            print("data saved");

        } catch (Exception ex) {
            print("error in saving data:");
            ex.printStackTrace();
        }
    }

    public void updateData() {
        customers.replaceCustomer(loggedInCustomer);

        String jsonStr = getJsonString(customers);
        saveData(jsonStr);
    }

    public DataModel getData() {
        DataModel data = null;

        try {
            FileInputStream impFile = new FileInputStream(FILE_NAME);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(impFile));

            String dataString = buffer.readLine();
            Gson g = new Gson();
            data = g.fromJson(dataString, DataModel.class);

            // print(data.toString());

        } catch (FileNotFoundException fileNotFoundEx) {
            // print("File not found");
        }
        catch (Exception ex) {
            print("error reading data from file.");
            ex.printStackTrace();
        }

        return data;
    }

    public void print(String str) {
        System.out.println(str);
    }

}
