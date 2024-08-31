package org.hinoob.pharadox.commands.impl.help;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.hinoob.pharadox.PharadoxBot;
import org.hinoob.pharadox.commands.Command;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.commands.SlashCommand;
import org.hinoob.pharadox.datastore.Datastore;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class HelpCommand extends SlashCommand {

    @Override
    public CommandData getCommandData() {
        return Commands.slash("help", "Get a list of all commands");
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

        Map<String, String> infoMap = new HashMap<>();

        event.getJDA().retrieveCommands().queue(new Consumer<List<net.dv8tion.jda.api.interactions.commands.Command>>() {
            @Override
            public void accept(List<net.dv8tion.jda.api.interactions.commands.Command> commands) {
                for(net.dv8tion.jda.api.interactions.commands.Command command : commands) {
                    infoMap.put(command.getName(), command.getDescription());
                }

                for(org.hinoob.pharadox.commands.Command command : PharadoxBot.getInstance().getCommandManager().getCommands()) {
                    if(command instanceof SlashCommand) {
                        builder.addField("/" + command.getName(), infoMap.get(command.getName()), false);
                    } else if(command instanceof MessageCommand msgcmd){
                        builder.addField(msgcmd.getPrefix(PharadoxBot.getInstance().getDatastoreManager().get(event.getGuild().getIdLong())) + command.getName(), msgcmd.getInfo() + " - " + (command.getAliases().length > 0 ? "Aliases: " + Arrays.toString(command.getAliases()) : ""), false);
                    }
                }

                event.getChannel().sendMessageEmbeds(builder.build()).queue();
            }
        });
    }
}
