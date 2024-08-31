package org.hinoob.pharadox.commands;

public interface Command {

    void register();

    String getName();
    String[] getAliases();
}
