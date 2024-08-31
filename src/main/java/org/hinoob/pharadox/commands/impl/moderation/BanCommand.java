package org.hinoob.pharadox.commands.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

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

        member.getUser().openPrivateChannel().queue(new Consumer<PrivateChannel>() {
            @Override
            public void accept(PrivateChannel privateChannel) {
                MessageEmbed embed = new EmbedBuilder()
                        .setTitle("BAN HAMMER")
                        .setDescription("You were banned from " + event.getGuild().getName() + " for 7 days!")
                        .addField("Reason", reason, false)
                        .setFooter("Banned by " + event.getMember().getEffectiveName(), event.getMember().getUser().getAvatarUrl())
                        .build();
                privateChannel.sendMessageEmbeds(embed).queue();
            }
        });

        event.getChannel().sendMessage(member.getAsMention() + " was hit with the ban :hammer: for 7 days!").queue();
    }
}
