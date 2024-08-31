package org.hinoob.pharadox.commands.impl;

import com.google.gson.JsonElement;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.PharadoxBot;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.util.Settings;

public class UptimeCommand extends MessageCommand {

    @Override
    public String getName() {
        return "uptime";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getPrefix(Datastore datastore) {
        JsonElement prefix = datastore.getData().get("prefix");
        if(prefix == null)
            return Settings.DEFAULT_PREFIX;
        return prefix.getAsString();
    }

    @Override
    public String getInfo() {
        return "Get the bot's uptime";
    }

    @Override
    public void handle(String[] args, Datastore datastore, MessageReceivedEvent event) {
        long uptime = System.currentTimeMillis() - PharadoxBot.getInstance().getStartTime();
        long seconds = uptime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        long months = weeks / 4;
        long years = months / 12;

        StringBuilder builder = new StringBuilder();
        if(years > 0) {
            builder.append(years).append(" year").append(years == 1 ? "" : "s").append(", ");
        }
        if(months > 0) {
            builder.append(months).append(" month").append(months == 1 ? "" : "s").append(", ");
        }
        if(weeks > 0) {
            builder.append(weeks).append(" week").append(weeks == 1 ? "" : "s").append(", ");
        }
        if(days > 0) {
            builder.append(days).append(" day").append(days == 1 ? "" : "s").append(", ");
        }
        if(hours > 0) {
            builder.append(hours).append(" hour").append(hours == 1 ? "" : "s").append(", ");
        }
        if(minutes > 0) {
            builder.append(minutes).append(" minute").append(minutes == 1 ? "" : "s").append(", ");
        }
        if(seconds > 0) {
            builder.append(seconds).append(" second").append(seconds == 1 ? "" : "s");
        }

        event.getChannel().sendMessage("I have been online for " + builder.toString() + ".").queue();
    }
}
