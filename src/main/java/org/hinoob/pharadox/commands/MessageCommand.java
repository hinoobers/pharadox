package org.hinoob.pharadox.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.datastore.Datastore;

public abstract class MessageCommand implements Command{

    @Override
    public void register() {

    }

    public MessageEmbed error(String msg) {
        return new EmbedBuilder()
                .setTitle("Error")
                .setDescription(msg)
                .setColor(0xFF0000)
                .build();
    }

    public abstract String getPrefix(Datastore datastore);

    public abstract void handle(String[] args, Datastore datastore, MessageReceivedEvent event);
}
