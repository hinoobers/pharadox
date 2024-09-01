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
import java.util.*;
import java.util.List;
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
        final EmbedBuilder[] builder = {new EmbedBuilder()};
        builder[0].setTitle("Commands");
        builder[0].setDescription("Here's a list of all commands:");
        builder[0].setColor(Color.GREEN);

        Map<String, String> infoMap = new HashMap<>();

        event.getJDA().retrieveCommands().queue(commands -> {
            for (net.dv8tion.jda.api.interactions.commands.Command command : commands) {
                infoMap.put(command.getName(), command.getDescription());
            }

            Collection<Command> commandList = PharadoxBot.getInstance().getCommandManager().getCommands();
            List<EmbedBuilder> embedBuilders = new ArrayList<>();

            int fieldCount = 0;

            for (org.hinoob.pharadox.commands.Command command : commandList) {
                String commandName;
                String commandInfo;

                if (command instanceof SlashCommand) {
                    commandName = "/" + command.getName();
                    commandInfo = infoMap.get(command.getName());
                } else if (command instanceof MessageCommand msgcmd) {
                    commandName = msgcmd.getPrefix(PharadoxBot.getInstance().getDatastoreManager().get(event.getGuild().getIdLong())) + command.getName();
                    commandInfo = msgcmd.getInfo() + " - " + (command.getAliases().length > 0 ? "Aliases: " + Arrays.toString(command.getAliases()) : "");
                } else {
                    continue;  // Skip if it's neither SlashCommand nor MessageCommand
                }

                if (fieldCount >= 25) {
                    embedBuilders.add(builder[0]);  // Save the current embed
                    builder[0] = new EmbedBuilder();  // Start a new embed
                    builder[0].setColor(Color.GREEN);
                    fieldCount = 0;
                }

                builder[0].addField(commandName, commandInfo, false);
                fieldCount++;
            }

            // Add the last builder if it contains any fields
            if (fieldCount > 0) {
                embedBuilders.add(builder[0]);
            }

            // Send all embeds
            for (EmbedBuilder embed : embedBuilders) {
                event.getChannel().sendMessageEmbeds(embed.build()).queue();
            }
        });
    }
}
