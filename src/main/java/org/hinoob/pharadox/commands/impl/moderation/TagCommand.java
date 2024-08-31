package org.hinoob.pharadox.commands.impl.moderation;

import com.google.gson.JsonElement;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.util.Settings;

public class TagCommand extends MessageCommand {

    @Override
    public String getName() {
        return "tag";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getInfo() {
        return "Display a tag";
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
            event.getChannel().sendMessageEmbeds(error("Please provide a valid tag name!")).queue();
            return;
        }

        String tagName = args[0];
        if(!datastore.getData().getAsJsonObject("tags").has(tagName)) {
            event.getChannel().sendMessageEmbeds(error("Tag not found!")).queue();
            return;
        }

        event.getChannel().sendMessage(datastore.getData().getAsJsonObject("tags").get(tagName).getAsString()).queue();
    }
}
