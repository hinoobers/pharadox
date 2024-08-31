package org.hinoob.pharadox.commands.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;

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
    public String getPrefix() {
        return "!";
    }

    @Override
    public void handle(String[] args, Datastore datastore, MessageReceivedEvent event) {
        event.getChannel().sendMessage("Test").queue();
    }
}
