package org.hinoob.pharadox.commands.impl.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class BanCommand extends MessageCommand {

    @Override
    public String getName() {
        return "ban";
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
        if(!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
            event.getChannel().sendMessageEmbeds(error("You do not have permission to use this command!")).queue();
            return;
        }
        if(args.length < 2) {
            event.getChannel().sendMessageEmbeds(error("Usage: !ban <user> <reason>")).queue();
            return;
        }

        Member member = event.getMessage().getMentions().getMembers().get(0);
        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        event.getGuild().ban(member, 7, TimeUnit.DAYS).reason(reason).queue();
        event.getChannel().sendMessage(member.getAsMention() + " was hit with the ban :hammer: for 7 days!").queue();
    }
}
