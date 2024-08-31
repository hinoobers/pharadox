package org.hinoob.pharadox.listener;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.hinoob.pharadox.PharadoxBot;
import org.hinoob.pharadox.datastore.Datastore;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class MessageListener extends ListenerAdapter {

    public MessageListener() {

    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Datastore datastore = PharadoxBot.getInstance().getDatastoreManager().get(event.getGuild().getIdLong());
        if(datastore.getData().has("swearFilter") && datastore.getData().get("swearFilter").getAsBoolean()) {
            String msg = event.getMessage().getContentRaw();
            String[] words = msg.split(" ");

            for(String word : words) {
                word = word.toLowerCase().trim();

                if(word.contains("fuck") || word.contains("bitch")) { // i cba to make a actual swear filter
                    event.getMessage().delete().queue(e -> {
                        event.getChannel().sendMessage("<@" + event.getMember().getId() + "> That message is not PROHIBITED!!!").queue(new Consumer<Message>() {
                            @Override
                            public void accept(Message message) {
                                message.delete().queueAfter(2, TimeUnit.SECONDS);
                            }
                        });
                    });
                    break;
                }
            }
        }
    }
}
