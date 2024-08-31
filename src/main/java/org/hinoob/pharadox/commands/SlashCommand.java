package org.hinoob.pharadox.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.hinoob.pharadox.datastore.Datastore;

public abstract class SlashCommand implements Command{

    @Override
    public void register() {

    }

    public abstract void handle(SlashCommandInteractionEvent event, Datastore datastore);
}
