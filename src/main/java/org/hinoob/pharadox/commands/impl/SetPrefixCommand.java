package org.hinoob.pharadox.commands.impl;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;

public class SetPrefixCommand extends MessageCommand {


    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getPrefix(Datastore datastore) {
        return "!";
    }

    @Override
    public void handle(String[] args, Datastore datastore, MessageReceivedEvent event) {
        if(!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessageEmbeds(error("You need the MANAGE_SERVER permission to use this command.")).queue();
            return;
        }

        if(args.length == 0) {
            event.getChannel().sendMessageEmbeds(error("Usage: !setprefix <prefix>")).queue();
            return;
        }

        datastore.getData().addProperty("prefix", args[0]);
        event.getChannel().sendMessage("Prefix set to: " + args[0]).queue();
    }
}
