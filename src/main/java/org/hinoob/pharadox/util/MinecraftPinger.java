package org.hinoob.pharadox.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.InputStreamReader;
import java.net.URL;

public class MinecraftPinger {

    public static JsonObject ping(String ip, int port) {
        try {
            URL url = new URL("https://api.mcsrvstat.us/3/" + ip + ":" + port);

            return new Gson().fromJson(new InputStreamReader(url.openStream()), JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
