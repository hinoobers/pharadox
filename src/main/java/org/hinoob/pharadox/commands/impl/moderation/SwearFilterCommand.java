package org.hinoob.pharadox.commands.impl.moderation;

import com.google.gson.JsonElement;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;

import java.util.Arrays;

public class SwearFilterCommand extends MessageCommand {
    @Override
    public String getName() {
        return "swearfilter";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"swfilter"};
    }

    @Override
    public String getPrefix(Datastore datastore) {
        JsonElement prefix = datastore.getData().get("prefix");
        if(prefix == null)
            return "!";
        return prefix.getAsString();
    }

    @Override
    public void handle(String[] args, Datastore datastore, MessageReceivedEvent event) {
        if(!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessageEmbeds(error("You need the MANAGE_SERVER permission to use this command.")).queue();
            return;
        }

        if(args.length == 0) {
            event.getChannel().sendMessageEmbeds(error("Usage: !swearfilter <on/off>")).queue();
            return;
        }

        System.out.println(Arrays.toString(args));
        if(args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("enable")) {
            datastore.getData().addProperty("swearFilter", true);
            event.getChannel().sendMessage("Swear filter enabled.").queue();
        } else if(args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("false") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("disable")) {
            datastore.getData().addProperty("swearFilter", false);
            event.getChannel().sendMessage("Swear filter disabled.").queue();
        } else {
            event.getChannel().sendMessageEmbeds(error("Usage: !swearfilter <on/off>")).queue();
        }
    }
}
