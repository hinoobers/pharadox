package org.hinoob.pharadox.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public interface Command {

    String getName();
    String[] getAliases();
}
