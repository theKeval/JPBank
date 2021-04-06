package com.thekeval.util;

import com.google.gson.Gson;
import com.thekeval.Models.DataModel;
import static com.thekeval.util.Constants.*;

import java.io.*;

public class FileUtils {



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

            Log.print("data saved");

        } catch (Exception ex) {
            Log.print("error in saving data:");
            ex.printStackTrace();
        }
    }

    public static DataModel getData() {
        DataModel data = null;

        try {
            FileInputStream impFile = new FileInputStream(FILE_NAME);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(impFile));

            String dataString = buffer.readLine();
            Gson g = new Gson();
            data = g.fromJson(dataString, DataModel.class);

            Log.print(data.toString());

        } catch (FileNotFoundException fileNotFoundEx) {
            Log.print("File not found");
        }
        catch (Exception ex) {
            Log.print("error reading data from file.");
            ex.printStackTrace();
        }

        return data;
    }

}
