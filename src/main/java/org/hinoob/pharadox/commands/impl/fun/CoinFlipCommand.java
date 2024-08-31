package org.hinoob.pharadox.commands.impl.fun;

import com.google.gson.JsonElement;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.util.Settings;

public class CoinFlipCommand extends MessageCommand {

    @Override
    public String getName() {
        return "coinflip";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"cf"};
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
        return "Flip a coin";
    }

    @Override
    public void handle(String[] args, Datastore datastore, MessageReceivedEvent event) {
        if(args.length != 1) {
            event.getChannel().sendMessageEmbeds(error("Invalid arguments. Usage: " + getPrefix(datastore) + "coinflip <heads/tails>")).queue();
            return;
        }

        String choice = args[0].toLowerCase();
        if(!choice.equals("heads") && !choice.equals("tails")) {
            event.getChannel().sendMessageEmbeds(error("Invalid choice. Usage: " + getPrefix(datastore) + "coinflip <heads/tails>")).queue();
            return;
        }

        String result = Math.random() < 0.5 ? "heads" : "tails";
        if(result.equals(choice)) {
            event.getChannel().sendMessage("You won! The coin landed on " + result + ".").queue();
        } else {
            event.getChannel().sendMessage("You lost! The coin landed on " + result + ".").queue();
        }
    }
}
