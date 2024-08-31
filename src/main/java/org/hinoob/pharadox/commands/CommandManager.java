package org.hinoob.pharadox.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.PharadoxBot;
import org.hinoob.pharadox.commands.impl.SwearFilterCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public void registerAll() {
        commands.add(new SwearFilterCommand());
    }

    public void handle(MessageReceivedEvent event) {
        String raw = event.getMessage().getContentRaw();
        String cmd = raw.split(" ")[0].substring(1);
        for(Command command : commands) {
            if(command instanceof MessageCommand msgcmd) {
                if(raw.startsWith(msgcmd.getPrefix()) && cmd.equals(msgcmd.getName()) || msgcmd.getAliases().length > 0 && msgcmd.getAliases()[0].equals(cmd)) {
                    String[] args = raw.substring(msgcmd.getPrefix().length()).split(" ");
                    msgcmd.handle(args, PharadoxBot.getInstance().getDatastoreManager().get(event.getGuild().getIdLong()), event);
                    break;
                }
            }
        }
    }

    public void handle(SlashCommandInteractionEvent event) {
    }
}
