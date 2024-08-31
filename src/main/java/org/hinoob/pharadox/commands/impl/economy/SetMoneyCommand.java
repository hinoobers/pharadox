package org.hinoob.pharadox.commands.impl.economy;

import com.google.gson.JsonElement;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.datastore.GlobalDatastore;
import org.hinoob.pharadox.util.Settings;

public class SetMoneyCommand extends MessageCommand {

    @Override
    public String getName() {
        return "setmoney";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getPrefix(Datastore datastore) {
        JsonElement prefix = datastore.getData().get("prefix");
        if(prefix == null)
            return Settings.DEFAULT_PREFIX;
        return prefix.getAsString();
    }

    @Override
    public String getInfo() {
        return "Set a user's balance";
    }

    @Override
    public void handle(String[] args, Datastore datastore, MessageReceivedEvent event) {
        // check if they have perms (for now, check if they are an owner)
        if(!event.getGuild().getOwnerId().equals(event.getAuthor().getId())) {
            event.getChannel().sendMessageEmbeds(error("You don't have permission to use this command!")).queue();
            return;
        }
        if(args.length < 2) {
            event.getChannel().sendMessageEmbeds(error("Please provide a valid user and amount!")).queue();
            return;
        }

        long user = event.getMessage().getMentions().getMembers().get(0).getIdLong();
        int amount = Integer.parseInt(args[1]);

        JsonElement bal = GlobalDatastore.getUserData(user).get("balance");
        if(bal == null) {
            GlobalDatastore.getUserData(user).addProperty("balance", amount);
        } else {
            GlobalDatastore.getUserData(user).addProperty("balance", amount);
        }

        event.getChannel().sendMessage("Successfully set " + event.getMessage().getMentions().getMembers().get(0).getAsMention() + "'s balance to " + amount + "$").queue();
    }
}
