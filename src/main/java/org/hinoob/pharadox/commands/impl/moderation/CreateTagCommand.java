package org.hinoob.pharadox.commands.impl.moderation;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.hinoob.pharadox.commands.SlashCommand;
import org.hinoob.pharadox.datastore.Datastore;

public class CreateTagCommand extends SlashCommand {

    @Override
    public void register(CommandListUpdateAction update) {
        update.addCommands(Commands.slash("createtag", "Create a tag").addOption(OptionType.STRING, "name", "The name of the tag", true).addOption(OptionType.STRING, "content", "The content of the tag", true)).queue();
    }

    @Override
    public String getName() {
        return "createtag";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, Datastore datastore) {
        String name = event.getOption("name").getAsString();
        String content = event.getOption("content").getAsString();

        if(!datastore.getData().has("tags")) {
            datastore.getData().add("tags", new JsonObject());
        }

        datastore.getData().getAsJsonObject("tags").addProperty(name, content);

        event.reply("Tag created successfully!").queue();
    }
}
