package org.hinoob.pharadox.commands.impl.minecraft;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;

public class MCUserLookupCommand extends MessageCommand {

    @Override
    public String getName() {
        return "mcuserlookup";
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
        if(args.length == 0) {
            event.getChannel().sendMessageEmbeds(error("Usage: !mcuserlookup <username>")).queue();
            return;
        }

        String username = args[0];
        // Do something with the username
    }
}
