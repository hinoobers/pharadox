package org.hinoob.pharadox.commands.impl;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.hinoob.pharadox.commands.SlashCommand;
import org.hinoob.pharadox.datastore.Datastore;

import java.util.Base64;

public class EncryptCommand extends SlashCommand {


    @Override
    public CommandData getCommandData() {
        OptionData algos = new OptionData(OptionType.STRING, "algorithm", "The algorithm to use", true)
                .addChoice("Base64", "base64")
                .addChoice("Reverse", "reverse");

        return Commands.slash("encrypt", "Encrypt a message")
                .addOption(OptionType.STRING, "message", "The message to encrypt", true)
                .addOptions(algos);
    }

    @Override
    public String getName() {
        return "encrypt";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, Datastore datastore) {
        String message = event.getOption("message").getAsString();
        String algorithm = event.getOption("algorithm").getAsString();
        event.reply(encrypt(message, algorithm)).queue();
    }

    private String encrypt(String message, String algorithm) {
        if(algorithm.equalsIgnoreCase("base64")) {
            // do encryption

            return Base64.getEncoder().encodeToString(message.getBytes());
        } else if(algorithm.equalsIgnoreCase("reverse")) {
            StringBuilder result = new StringBuilder();

            for(int i = message.length() - 1; i >= 0; i--) {
                result.append(message.charAt(i));
            }

            return result.toString();
        }

        return "Invalid algorithm";
    }
}
