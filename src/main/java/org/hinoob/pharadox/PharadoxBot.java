package org.hinoob.pharadox;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.logging.Logger;

@Getter
public class PharadoxBot {

    @Setter public static PharadoxBot instance;

    private static Logger logger = Logger.getLogger("PharadoxBot");

    private JDA jda;

    public void start() {
        String token = Dotenv.load().get("TOKEN");
        if(token == null) {
            logger.severe("No token provided in .env file!");
            return;
        }

        logger.info("Starting bot...");
        this.jda = JDABuilder.createDefault(token).build();

        try {
            this.jda.awaitReady();
            logger.info("Bot started successfully!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
