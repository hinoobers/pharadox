package org.hinoob.pharadox.listener;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.hinoob.pharadox.PharadoxBot;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;

        System.out.println("Message received: " + event.getMessage().getContentRaw());
        PharadoxBot.getInstance().getCommandManager().handle(event);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        System.out.println("Slash command received: " + event.getName());
        PharadoxBot.getInstance().getCommandManager().handle(event);
    }
}
