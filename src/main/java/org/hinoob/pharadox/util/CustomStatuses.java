package org.hinoob.pharadox.util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.hinoob.pharadox.PharadoxBot;

import java.util.Random;

public class CustomStatuses {

    public static void flip() {
        int i = new Random().nextInt(5);
        JDA jda = PharadoxBot.getInstance().getJda();

        switch (i) {
            case 0:
                jda.getPresence().setActivity(Activity.playing("with the code"));
                break;
            case 1:
                jda.getPresence().setActivity(Activity.watching("over " + jda.getGuilds().size() + " servers"));
                break;
            case 2:
                jda.getPresence().setActivity(Activity.listening("to music"));
                break;
            case 3:
                int users = jda.getUsers().size();
                jda.getPresence().setActivity(Activity.watching("over " + users + (users == 1 ? " user" : " users")));
                break;
            case 4:
                jda.getPresence().setActivity(Activity.playing("with the API"));
                break;
        }
    }
}
