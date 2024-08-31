package org.hinoob.pharadox.commands.impl.gambling;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.datastore.GlobalDatastore;
import org.hinoob.pharadox.util.Settings;

// This is gambling ONLY with FAKE "virtual" money
public class SlotsCommand extends MessageCommand {

    @Override
    public String getName() {
        return "slots";
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
    public void handle(String[] args, Datastore datastore, MessageReceivedEvent event) {
        if(args.length == 0) {
            event.getChannel().sendMessageEmbeds(error("Please provide a valid amount to bet!")).queue();
            return;
        }

        int bet = Integer.parseInt(args[0]);
        if(bet < 1) {
            event.getChannel().sendMessageEmbeds(error("Please provide a valid amount to bet!")).queue();
            return;
        }

        if(bet > 1000) {
            event.getChannel().sendMessageEmbeds(error("You can't bet more than 1000$!")).queue();
            return;
        }

        int[] slots = new int[3];
        for(int i = 0; i < 3; i++) {
            slots[i] = (int) (Math.random() * 6);
        }

        event.getChannel().sendMessage("[" + slots[0] + "] [" + slots[1] + "] [" + slots[2] + "]").queue();

        JsonObject userData = GlobalDatastore.getUserData(event.getAuthor().getIdLong());
        if(slots[0] == slots[1] && slots[1] == slots[2]) {
            event.getChannel().sendMessage("You won " + (bet * 3) + "$!").queue();
            userData.addProperty("balance", userData.get("balance").getAsInt() + (bet * 3));
        } else if(slots[0] == slots[1] || slots[1] == slots[2] || slots[0] == slots[2]) {
            event.getChannel().sendMessage("You won " + (bet * 2) + "$!").queue();
            userData.addProperty("balance", userData.get("balance").getAsInt() + (bet * 2));
        } else {
            event.getChannel().sendMessage("You lost " + bet + "$!").queue();
            userData.addProperty("balance", userData.get("balance").getAsInt() - bet);
        }
    }
}
