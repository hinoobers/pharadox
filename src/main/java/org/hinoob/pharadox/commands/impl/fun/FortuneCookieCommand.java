package org.hinoob.pharadox.commands.impl.fun;

import com.google.gson.JsonElement;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.util.Settings;

public class FortuneCookieCommand extends MessageCommand {


    @Override
    public String getName() {
        return "fortunecookie";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"fcookie"};
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
        String[] fortunes = {
                "A beautiful, smart, and loving person will be coming into your life.",
                "A dubious friend may be an enemy in camouflage.",
                "A faithful friend is a strong defense.",
                "A feather in the hand is better than a bird in the air.",
                "A fresh start will put you on your way.",
                "A friend asks only for your time not your money.",
                "A friend is a present you give yourself.",
                "A gambler not only will lose what he has, but also will lose what he doesn’t have.",
                "A golden egg of opportunity falls into your lap this month.",
                "A good friendship is often more important than a passionate romance.",
                "A journey of a thousand miles begins with a single step.",
                "A leopard cannot change its spots.",
                "A problem shared is a problem halved.",
                "A smile is a universal welcome.",
                "A smooth long journey! Great expectations.",
                "A stitch in time saves nine"};
        int random = (int) (Math.random() * fortunes.length);
        event.getChannel().sendMessage(fortunes[random]).queue();
    }
}
