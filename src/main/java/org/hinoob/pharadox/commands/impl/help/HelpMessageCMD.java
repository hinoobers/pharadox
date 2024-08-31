package org.hinoob.pharadox.commands.impl.help;

import com.google.gson.JsonElement;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.hinoob.pharadox.PharadoxBot;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.commands.SlashCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.util.Settings;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class HelpMessageCMD extends MessageCommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getInfo() {
        return "Get a list of all commands";
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
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Commands");
        builder.setDescription("Here's a list of all commands:");
        builder.setColor(Color.GREEN);

        Map<String, String> infoMap = new HashMap<>();

        event.getJDA().retrieveCommands().queue(new Consumer<List<Command>>() {
            @Override
            public void accept(List<Command> commands) {
                for(Command command : commands) {
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
