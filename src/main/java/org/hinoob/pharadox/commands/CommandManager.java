package org.hinoob.pharadox.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.hinoob.pharadox.PharadoxBot;
import org.hinoob.pharadox.datastore.Datastore;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public void registerAll() {
        for(Class<? extends Command> cmdClazz : new Reflections("org.hinoob.pharadox.commands.impl").getSubTypesOf(Command.class)) {
            if(cmdClazz.equals(Command.class) || cmdClazz.equals(MessageCommand.class) || cmdClazz.equals(SlashCommand.class)) continue;

            try {
                Command command = cmdClazz.getDeclaredConstructor().newInstance();
                commands.add(command);
            } catch (Exception e) {
                System.err.println("Failed to register command: " + cmdClazz.getSimpleName());
            }
        }

        CommandListUpdateAction update = PharadoxBot.getInstance().getJda().updateCommands();
        List<CommandData> datas = commands.stream().filter(p -> p instanceof SlashCommand).map(p -> ((SlashCommand) p).getCommandData()).toList();

        update.addCommands(datas).queue();
        update.queue();
    }

    public Collection<Command> getCommands() {
        return Collections.unmodifiableCollection(commands).stream().sorted((a, b) -> {
            String aName = a.getName();
            String bName = b.getName();

            return aName.compareTo(bName);
        }).toList();
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
