package org.hinoob.pharadox.commands.impl.moderation;

import com.google.gson.JsonElement;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.util.Settings;

import java.util.Arrays;

public class KickCommand extends MessageCommand {

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getInfo() {
        return "Kick a user from the server";
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
        if(!event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
            event.getChannel().sendMessageEmbeds(error("You do not have permission to use this command!")).queue();
            return;
        }
        if(args.length < 2) {
            event.getChannel().sendMessage("Usage: " + getPrefix(datastore) + "kick <user> [reason]").queue();
            return;
        }

        Member member = event.getMessage().getMentions().getMembers().get(0);
        String reason = args.length > 2 ? String.join(" ", Arrays.copyOfRange(args, 2, args.length)) : "No reason provided";

        member.kick().reason(reason).queue();
        event.getChannel().sendMessage("Kicked " + member.getAsMention() + " for " + reason).queue();
    }

}
