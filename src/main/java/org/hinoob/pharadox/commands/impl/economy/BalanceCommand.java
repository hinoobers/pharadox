package org.hinoob.pharadox.commands.impl.economy;

import com.google.gson.JsonElement;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.datastore.GlobalDatastore;
import org.hinoob.pharadox.util.Settings;

public class BalanceCommand extends MessageCommand {

    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"bal"};
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
        JsonElement bal = GlobalDatastore.getUserData(event.getAuthor().getIdLong()).get("balance");
        if(bal == null) {
            event.getChannel().sendMessage("Your balance: 0$").queue();

            GlobalDatastore.getUserData(event.getAuthor().getIdLong()).addProperty("balance", 0);
        } else {
            event.getChannel().sendMessage("Your balance: " + bal.getAsInt() + "$").queue();
        }
    }
}
