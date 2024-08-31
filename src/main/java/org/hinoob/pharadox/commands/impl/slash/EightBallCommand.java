package org.hinoob.pharadox.commands.impl.slash;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.hinoob.pharadox.commands.SlashCommand;
import org.hinoob.pharadox.datastore.Datastore;

public class EightBallCommand extends SlashCommand {


    @Override
    public String getName() {
        return "8ball";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void register(CommandListUpdateAction update) {
        update.addCommands(Commands.slash("8ball", "Ask the magic 8ball a question").addOption(OptionType.STRING, "question", "The question to ask", true)).queue();
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, Datastore datastore) {
        String question = event.getOption("question").getAsString();

        String[] responses = new String[] {
            "It is certain",
            "It is decidedly so",
            "Without a doubt",
            "Yes, definitely",
            "You may rely on it",
            "As I see it, yes",
            "Most likely",
            "Outlook good",
            "Yes",
            "Signs point to yes",
            "Reply hazy, try again",
            "Ask again later",
            "Better not tell you now",
            "Cannot predict now",
            "Concentrate and ask again",
            "Don't count on it",
            "My reply is no",
            "My sources say no",
            "Outlook not so good",
            "Very doubtful"
        };

        MessageEmbed embed = new net.dv8tion.jda.api.EmbedBuilder()
                .setTitle("Magic 8ball")
                .setDescription("You asked: " + question)
                .addField("Answer", responses[(int) (Math.random() * responses.length)], false)
                .build();
        event.replyEmbeds(embed).queue();
    }
}
