package org.hinoob.pharadox.commands.impl.gambling;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.hinoob.pharadox.commands.SlashCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.datastore.GlobalDatastore;

public class GuessNumberCommand extends SlashCommand {


    @Override
    public String getName() {
        return "guessnumber";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("guessnumber", "Guess a number between 1 and 10")
                .addOption(OptionType.INTEGER, "number", "The number to guess", true)
                .addOption(OptionType.INTEGER, "bet", "The amount of coins to bet", true);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, Datastore datastore) {
        int number = event.getOption("number").getAsInt();
        int bet = event.getOption("bet").getAsInt();

        if(number < 1 || number > 10) {
            event.reply("The number must be between 1 and 10").queue();
            return;
        }

        if(bet < 1) {
            event.reply("The bet must be greater than 0").queue();
            return;
        }

        JsonObject userData = GlobalDatastore.getUserData(event.getUser().getIdLong());
        if(!userData.has("balance") || userData.get("balance").getAsInt() < bet) {
            event.reply("You don't have enough coins to bet that amount").queue();
            return;
        }

        int randomNumber = (int) (Math.random() * 10) + 1;

        if(randomNumber == number) {
            userData.addProperty("balance", userData.get("balance").getAsInt() + bet);
            event.reply("You guessed the number! You won " + bet + " coins").queue();
        } else {
            userData.addProperty("balance", userData.get("balance").getAsInt() - bet);
            event.reply(":X: The number was " + randomNumber + ". You lost " + bet + " coins").queue();
        }
    }
}
