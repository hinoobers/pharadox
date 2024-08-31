package org.hinoob.pharadox.commands.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ChatbotCommand extends MessageCommand {

    @Override
    public String getName() {
        return "c";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getPrefix(Datastore datastore) {
        return ":";
    }

    private final Map<List<String>, List<String>> responseMap = Map.of(
            List.of("what", "your", "name"), List.of(
                    "I'm a bot. You think these things have names?",
                    "I don't have a name. I'm just code.",
                    "Names are for humans. I'm just a bot."
            ),
            List.of("how", "are", "you"), List.of(
                    "I'm a bot. I don't have feelings.",
                    "I'm just code. I don't have feelings.",
                    "I'm a bot. I don't have feelings like you do."
            ),
            List.of("what", "your", "purpose"), List.of(
                    "I'm a bot. I don't have a purpose.",
                    "I'm just code. I don't have a purpose.",
                    "I'm a bot. I don't have a purpose like you do."
            ),

            List.of("what", "time", "is", "it"), List.of(
                    "Time for you to get a watch.",
                    "Time is :TIME:"
            ),

            List.of("how", "old", "are", "you"), List.of(
                    "I'm a bot. I don't have an age.",
                    "I'm just code. I don't have an age.",
                    "I'm a bot. I don't have an age like you do.",
                    "I'm just a bunch of 010101's and 110011's. I don't have an age."
            ),

            List.of("who", "made", "you", "created"), List.of(
                    "I was created by hinoob.",
                    "I was created by hinoob. He's a cool guy.",
                    "I was created by hinoob. He's a good programmer."
            ),

            List.of("what", "is", "your", "favorite", "color"), List.of(
                    "I'm a bot. I don't have a favorite color.",
                    "I'm just code. I don't have a favorite color.",
                    "I'm a bot. I don't have a favorite color like you do.",
                    "It's #000000. It's the color of my soul."
            )
    );
    private final Random random = new Random();

    @Override
    public void handle(String[] args, Datastore datastore, MessageReceivedEvent event) {
        // bossy chatbot for fun lol
        if(args.length == 0) {
            event.getChannel().sendMessage("Wtf do u want?").queue();
        } else {
            String message = String.join(" ", args).toLowerCase().trim();
            // find the most probable response from responseMap
            // whatever response has most matching words, we pick that response

            String[] words = message.split(" ");
            int max = 0;
            String response = "I don't know what you're talking about.";
            for(Map.Entry<List<String>, List<String>> entry : responseMap.entrySet()) {
                List<String> key = entry.getKey();
                int count = 0;
                for(String word : words) {
                    if(key.contains(word)) {
                        count++;
                    }
                }
                if(count > max) {
                    max = count;
                    response = entry.getValue().get(random.nextInt(entry.getValue().size()));
                }
            }

            // replace placeholders
            if(response.contains(":TIME:")) {
                ZonedDateTime utcTime = ZonedDateTime.now(ZoneOffset.UTC);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
                String formattedTime = utcTime.format(formatter);

                response = response.replace(":TIME:", formattedTime + " UTC");
            }

            event.getChannel().sendMessage(response).queue();
        }
    }
}
