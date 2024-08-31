package org.hinoob.pharadox.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.hinoob.pharadox.PharadoxBot;
import org.hinoob.pharadox.commands.impl.ChatbotCommand;
import org.hinoob.pharadox.commands.impl.CoinFlipCommand;
import org.hinoob.pharadox.commands.impl.PingCommand;
import org.hinoob.pharadox.commands.impl.UptimeCommand;
import org.hinoob.pharadox.commands.impl.moderation.BanCommand;
import org.hinoob.pharadox.commands.impl.music.PlayCommand;
import org.hinoob.pharadox.commands.impl.slash.EightBallCommand;
import org.hinoob.pharadox.commands.impl.slash.MemeCommand;
import org.hinoob.pharadox.commands.impl.minecraft.MCServerLookupCommand;
import org.hinoob.pharadox.commands.impl.minecraft.MCUserLookupCommand;
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
        commands.add(new MCUserLookupCommand());
        commands.add(new MCServerLookupCommand());
        commands.add(new MemeCommand());
        commands.add(new EightBallCommand());
        commands.add(new CoinFlipCommand());
        commands.add(new PingCommand());
        commands.add(new UptimeCommand());
        commands.add(new PlayCommand());
        commands.add(new BanCommand());
        commands.add(new ChatbotCommand());

        CommandListUpdateAction update = PharadoxBot.getInstance().getJda().updateCommands();
        for(Command command : commands) {
            if(command instanceof SlashCommand slashcmd) {
                slashcmd.register(update);
            }
        }
        update.queue();
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
        for(Command command : commands) {
            if(command instanceof SlashCommand slashcmd) {
                if(event.getName().equals(slashcmd.getName())) {
                    slashcmd.handle(event, PharadoxBot.getInstance().getDatastoreManager().get(event.getGuild().getIdLong()));
                    break;
                }
            }
        }
    }
}
