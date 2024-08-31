package org.hinoob.pharadox.commands.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.hinoob.pharadox.PharadoxBot;
import org.hinoob.pharadox.commands.Command;
import org.hinoob.pharadox.commands.SlashCommand;
import org.hinoob.pharadox.datastore.Datastore;

import java.awt.*;
import java.util.Arrays;

public class HelpCommand extends SlashCommand {

    @Override
    public void register(CommandListUpdateAction update) {
        update.addCommands(Commands.slash("help", "Get a list of all commands")).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, Datastore datastore) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Commands");
        builder.setDescription("Here's a list of all commands:");
        builder.setColor(Color.GREEN);

        for(Command command : PharadoxBot.getInstance().getCommandManager().getCommands()) {
            if(command instanceof SlashCommand) {
                builder.addField(command.getName(), "Slash Command", false);
            } else {
                builder.addField(command.getName(), "Aliases: " + Arrays.toString(command.getAliases()), false);
            }
        }

        event.replyEmbeds(builder.build()).queue();
    }
}
