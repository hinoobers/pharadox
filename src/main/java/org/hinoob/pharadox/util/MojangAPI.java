package org.hinoob.pharadox.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class MojangAPI {

    public static MojangAPIUser fetch(UUID uuid) {
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString());
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            String response = builder.toString();
            JsonObject object = new Gson().fromJson(response, JsonObject.class);
            MojangAPIUser user = new MojangAPIUser(object);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static MojangAPIUser fetch(String username) {
        System.out.println("Fetching user: " + username);
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            String response = builder.toString();
            JsonObject object = new Gson().fromJson(response, JsonObject.class);

            return fetch(fixUUID(object.get("id").getAsString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static UUID fixUUID(String uuid) {
        // add dashes
        return UUID.fromString(uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20));
    }

    public static class MojangAPIUser {

        private final JsonObject data;

        public MojangAPIUser(JsonObject data) {
            this.data = data;
            System.out.println(data);
        }
    }
}
