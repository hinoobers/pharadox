package org.hinoob.pharadox.datastore;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GlobalDatastore {

    private static JsonObject o = new JsonObject();

    public static void load() {
        File file = new File("global_datastore.json");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // load the data
        StringBuilder builder = new StringBuilder();
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            scanner.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        // parse the data
        o = new Gson().fromJson(builder.toString(), JsonObject.class);
    }

    public static void save() {
        File file = new File("global_datastore.json");
        try {
            java.io.FileWriter writer = new java.io.FileWriter(file);
            writer.write(o.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JsonObject getUserData(long userId) {
        if(!o.has(String.valueOf(userId)))
            o.add(String.valueOf(userId), new JsonObject());
        return o.getAsJsonObject(String.valueOf(userId));
    }
}
