package org.hinoob.pharadox.commands.impl.minecraft;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.util.MinecraftPinger;
import org.hinoob.pharadox.util.StringUtils;

import java.util.stream.Collectors;

public class MCServerLookupCommand extends MessageCommand {

    @Override
    public void handle(String[] args, Datastore datastore, MessageReceivedEvent event) {
        if(args.length == 0) {
            event.getChannel().sendMessage("Usage: !mcserverlookup <ip:port>").queue();
            return;
        }

        String ip;
        int port = 25565;

        if(args[0].contains(":")) {
            String[] split = args[0].split(":");
            ip = split[0];
            port = Integer.parseInt(split[1]);
        } else {
            ip = args[0];
        }

        JsonObject response = MinecraftPinger.ping(ip, port);
        if(response == null || !response.get("online").getAsBoolean()) {
            event.getChannel().sendMessageEmbeds(error("Server not found/offline.")).queue();
            return;
        }

        String motd = response.get("motd").getAsJsonObject().get("clean").getAsJsonArray().asList().stream().map(JsonElement::getAsString).collect(Collectors.joining("\n"));

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("MC Server")
                .addField("Version", response.get("protocol").getAsJsonObject().get("name").getAsString(), false)
                .addField("Players", StringUtils.formatNumber(response.get("players").getAsJsonObject().get("online").getAsInt()) + "/" + StringUtils.formatNumber(response.get("players").getAsJsonObject().get("max").getAsInt()), false)
                .setAuthor(ip + ":" + port, null, "https://api.mcsrvstat.us/icon/" + ip + ":" + port)
                .addField("MOTD", motd, false)
                .build();

        event.getChannel().sendMessageEmbeds(embed).queue();
    }

    @Override
    public String getName() {
        return "mcserverlookup";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getPrefix(Datastore datastore) {
        return "!";
    }
}
