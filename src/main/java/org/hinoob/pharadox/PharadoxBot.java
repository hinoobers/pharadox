package org.hinoob.pharadox;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.hinoob.pharadox.commands.CommandManager;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.datastore.DatastoreManager;
import org.hinoob.pharadox.listener.CommandListener;
import org.hinoob.pharadox.listener.MessageListener;
import org.hinoob.pharadox.util.CustomStatuses;
import org.hinoob.pharadox.util.Settings;

import java.util.logging.Logger;

@Getter
public class PharadoxBot {

    @Setter @Getter private static PharadoxBot instance;

    private static Logger logger = Logger.getLogger("PharadoxBot");

    private JDA jda;
    private final DatastoreManager datastoreManager = new DatastoreManager();
    private final CommandManager commandManager = new CommandManager();
    private final long startTime = System.currentTimeMillis();

    private int loopTicks = 0;

    public void start() {
        String token = Dotenv.load().get("TOKEN");
        if(token == null) {
            logger.severe("No token provided in .env file!");
            return;
        }

        logger.info("Starting bot...");
        this.jda = JDABuilder.createDefault(token)
                .addEventListeners(new MessageListener(), new CommandListener())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES)
                .build();
        CustomStatuses.flip();
        this.datastoreManager.load();
        this.commandManager.registerAll();

        try {
            this.jda.awaitReady();
            logger.info("Bot started successfully!");

            // start a loop
            new Thread(() -> {
                while(true) {
                    try {
                        Thread.sleep(1000);

                        for(Guild guild : jda.getGuilds()) {
                            Datastore datastore = datastoreManager.get(guild.getIdLong());
                            if(datastore == null) {
                                datastore = new Datastore(guild.getIdLong());
                                datastoreManager.add(datastore);
                            }
                        }
                        datastoreManager.save();

                        ++loopTicks;
                        if(loopTicks > 0 && loopTicks % Settings.STATUS_FLIP_INTERVAL == 0) {
                            CustomStatuses.flip();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
