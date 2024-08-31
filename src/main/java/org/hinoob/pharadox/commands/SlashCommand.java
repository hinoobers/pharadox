package org.hinoob.pharadox.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.hinoob.pharadox.datastore.Datastore;

public abstract class SlashCommand implements Command{

    public MessageEmbed error(String msg) {
        return new EmbedBuilder()
                .setTitle("Error")
                .setDescription(msg)
                .setColor(0xFF0000)
                .build();
    }

    public abstract CommandData getCommandData();

    public abstract void handle(SlashCommandInteractionEvent event, Datastore datastore);
}
