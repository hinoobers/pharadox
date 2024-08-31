package org.hinoob.pharadox.commands.impl;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.hinoob.pharadox.commands.SlashCommand;
import org.hinoob.pharadox.datastore.Datastore;

import java.io.BufferedReader;
import java.net.URL;

public class MemeCommand extends SlashCommand {

    @Override
    public void register(CommandListUpdateAction update) {
        update.addCommands(Commands.slash("meme", "Get a random meme")).queue();
    }

    @Override
    public String getName() {
        return "meme";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, Datastore datastore) {
        try {
            URL url = new URL("https://meme-api.com/gimme");
            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(url.openStream()));
            JsonObject json = new com.google.gson.JsonParser().parse(reader.readLine()).getAsJsonObject();
            event.reply(json.get("url").getAsString()).queue();
        } catch (Exception e) {
            event.replyEmbeds(error("An error occurred while fetching the meme.")).queue();
        }
    }
}
