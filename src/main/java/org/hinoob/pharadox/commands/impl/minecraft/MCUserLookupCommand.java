package org.hinoob.pharadox.commands.impl.minecraft;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.util.MojangAPI;

import java.awt.*;

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
    public String getInfo() {
        return "Look up a Minecraft user";
    }

    @Override
    public void handle(String[] args, Datastore datastore, MessageReceivedEvent event) {
        if(args.length == 0) {
            event.getChannel().sendMessageEmbeds(error("Usage: !mcuserlookup <username>")).queue();
            return;
        }

        String username = args[0];
        // Do something with the username
        MojangAPI.MojangAPIUser user = MojangAPI.fetch(username);
        if(user == null) {
            event.getChannel().sendMessageEmbeds(error("User not found.")).queue();
            return;
        }

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(user.getName(), null, "https://heads.discordsrv.com/head.png?texture=" + user.getTexture() + "&uuid=" + user.getUUID() + "&name=" + user.getName() + "&overlay")
                .addField("UUID", user.getUUID().toString(), false)
                .setColor(Color.GREEN)
                .build();
        event.getChannel().sendMessageEmbeds(embed).queue();
    }
}
