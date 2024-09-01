package org.hinoob.pharadox.commands.impl.gambling;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.hinoob.pharadox.commands.SlashCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.datastore.GlobalDatastore;

public class RockPaperScissorsCommand extends SlashCommand {


    @Override
    public String getName() {
        return "rockpaperscissors";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public CommandData getCommandData() {
        OptionData option = new OptionData(OptionType.STRING, "choice", "The choice you want to make", true);
        option.addChoice("rock", "rock");
        option.addChoice("paper", "paper");
        option.addChoice("scissors", "scissors");

        return Commands.slash("rockpaperscissors", "Play a game of rock paper scissors")
                .addOptions(option)
                .addOption(OptionType.INTEGER, "bet", "The amount of coins to bet", true);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, Datastore datastore) {
        int bet = event.getOption("bet").getAsInt();
        String choice = event.getOption("choice").getAsString();

        if(bet < 1) {
            event.reply("The bet must be greater than 0").queue();
            return;
        }

        JsonObject userData = GlobalDatastore.getUserData(event.getUser().getIdLong());
        if(!userData.has("balance") || userData.get("balance").getAsInt() < bet) {
            event.reply("You don't have enough coins to bet that amount").queue();
            return;
        }

        String[] choices = {"rock", "paper", "scissors"};
        int random = (int) (Math.random() * choices.length);

        String botChoice = choices[random];

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Rock Paper Scissors");
        builder.addField("Your choice", choice.equals("paper") ? ":roll_of_paper:" : ":" + choice + ":", false);
        builder.addField("Bot's choice", botChoice.equals("paper") ? ":roll_of_paper:" : ":" + botChoice + ":", false);

        boolean won = false;
        if(choice.equals("rock")) {
            if(botChoice.equals("scissors")) {
                won = true;
            }
        } else if(choice.equals("paper")) {
            if(botChoice.equals("rock")) {
                won = true;
            }
        } else if(choice.equals("scissors")) {
            if(botChoice.equals("paper")) {
                won = true;
            }
        }

        builder.addField("Result", choice.equals(botChoice) ? "Tie" : (won ? "You win!" : "You lost!"), false);

        if(won) {
            userData.addProperty("balance", userData.get("balance").getAsInt() + bet);
        } else {
            userData.addProperty("balance", userData.get("balance").getAsInt() - bet);
        }

        event.replyEmbeds(builder.build()).queue();
    }

}
