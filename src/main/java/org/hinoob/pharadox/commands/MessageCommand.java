package org.hinoob.pharadox.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.datastore.Datastore;

public abstract class MessageCommand implements Command{

    @Override
    public void register() {

    }

    public abstract String getPrefix();

    public abstract void handle(String[] args, Datastore datastore, MessageReceivedEvent event);
}
