package org.hinoob.pharadox.commands.impl;

import com.google.gson.JsonElement;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.util.Settings;

import java.util.function.Consumer;

public class PingCommand extends MessageCommand {

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"pong"};
    }

    @Override
    public String getInfo() {
        return "Get the bot's latency";
    }

    @Override
    public String getPrefix(Datastore datastore) {
        JsonElement prefix = datastore.getData().get("prefix");
        if(prefix == null)
            return Settings.DEFAULT_PREFIX;
        return prefix.getAsString();
    }

    @Override
    public void handle(String[] args, Datastore datastore, MessageReceivedEvent event) {
        // get ping
        long ping = event.getJDA().getGatewayPing();

        long start = System.currentTimeMillis();
        event.getChannel().sendMessage("Testing..").queue(new Consumer<Message>() {
            @Override
            public void accept(Message message) {
                message.delete().queue(new Consumer<Void>() {
                    @Override
                    public void accept(Void aVoid) {
                        MessageEmbed embed = new EmbedBuilder()
                                .setTitle("Pong!")
                                .addField("API Latency", ping + "ms", false)
                                .addField("Bot Latency", (System.currentTimeMillis() - start) + "ms", false)
                                .build();
                        event.getChannel().sendMessageEmbeds(embed).queue();
                    }
                });
            }
        });
    }
}
