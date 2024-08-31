package org.hinoob.pharadox.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.PharadoxBot;
import org.hinoob.pharadox.commands.impl.moderation.SetPrefixCommand;
import org.hinoob.pharadox.commands.impl.moderation.SwearFilterCommand;
import org.hinoob.pharadox.datastore.Datastore;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public void registerAll() {
        commands.add(new SwearFilterCommand());
        commands.add(new SetPrefixCommand());
    }

    public void handle(MessageReceivedEvent event) {
        String raw = event.getMessage().getContentRaw();
        String cmd = raw.split(" ")[0].substring(1);
        Datastore datastore = PharadoxBot.getInstance().getDatastoreManager().get(event.getGuild().getIdLong());

        for(Command command : commands) {
            if(command instanceof MessageCommand msgcmd) {
                if(!raw.startsWith(msgcmd.getPrefix(datastore))) continue;

                if(cmd.equals(msgcmd.getName()) || msgcmd.getAliases().length > 0 && msgcmd.getAliases()[0].equals(cmd)) {
                    String[] args = raw.substring(msgcmd.getPrefix(datastore).length()).split(" ");
                    // remove first element
                    args = args.length == 1 ? new String[0] : raw.substring(msgcmd.getPrefix(datastore).length() + args[0].length() + 1).split(" ");
                    msgcmd.handle(args, datastore, event);
                    break;
                }
            }
        }
    }

    public void handle(SlashCommandInteractionEvent event) {
    }
}
