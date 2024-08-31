package org.hinoob.pharadox.datastore;

import com.google.gson.JsonObject;
import lombok.Getter;

// This is per guild
public class Datastore {

    private final long guildID;
    @Getter
    private JsonObject data;

    public Datastore(long guildID) {
        this.guildID = guildID;
    }

    public long getID() {
        return this.guildID;
    }

    public void load(JsonObject data) {
        this.data = data;
    }

}
