package org.hinoob.pharadox.datastore;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DatastoreManager {

    private final Map<Long, Datastore> datastores = new HashMap<>();

    public Datastore get(long id) {
        return this.datastores.get(id);
    }
    public void load() {
        File f = new File("datastore.json");
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            StringBuilder sb = new StringBuilder();
            Scanner scanner = new Scanner(f);
            while(scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            scanner.close();

            JsonArray array = new Gson().fromJson(sb.toString(), JsonArray.class);
            for(JsonElement element : array) {
                long id = element.getAsJsonObject().get("id").getAsLong();
                Datastore datastore = new Datastore(id);
                datastore.load(element.getAsJsonObject().get("data").getAsJsonObject());
                this.datastores.put(id, datastore);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        JsonArray array = new JsonArray();
        for(Datastore datastore : this.datastores.values()) {
            JsonObject object = new JsonObject();
            object.addProperty("id", datastore.getID());
            object.add("data", datastore.getData());
            array.add(object);
        }

        try {
            File f = new File("datastore.json");
            if(!f.exists()) {
                f.createNewFile();
            }

            PrintWriter writer = new PrintWriter(f);
            writer.write(array.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
