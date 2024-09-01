package org.hinoob.pharadox.commands.impl.fun;

import com.google.gson.JsonElement;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.util.Settings;

public class JokeCommand extends MessageCommand {


    @Override
    public String getName() {
        return "joke";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getInfo() {
        return "Generate a hilarious joke";
    }

    @Override
    public String getPrefix(Datastore datastore) {
        JsonElement prefix = datastore.getData().get("prefix");
        if(prefix == null)
            return Settings.DEFAULT_PREFIX;
        return prefix.getAsString();
    }

    private final String[] jokes = {
            "Why did the scarecrow win an award? Because he was outstanding in his field!",
            "What do you call a fish wearing a crown? A kingfish!",
            "Why don't skeletons fight each other? They don't have the guts!",
            "Why did the tomato turn red? Because it saw the salad dressing!",
            "What do you call a bear with no teeth? A gummy bear!",
            "Why did the math book look sad? Because it had too many problems!",
            "What do you call a pile of cats? A meowtain!",
            "Why did the golfer bring two pairs of pants? In case he got a hole in one!",
            "What do you call a fake noodle? An impasta!",
            "Why did the coffee file a police report? It got mugged!",
            "What do you call a belt made out of watches? A waist of time!",
            "Why did the scarecrow win an award? Because he was outstanding in his field!",
            "What do you call a fish wearing a crown? A kingfish!",
            "Why don't skeletons fight each other? They don't have the guts!",
            "Why did the tomato turn red? Because it saw the salad dressing!",
            "What do you call a bear with no teeth? A gummy bear!",
            "Why did the math book look sad? Because it had too many problems!",
            "What do you call a pile of cats? A meowtain!",
            "Why did the golfer bring two pairs of pants? In case he got a hole in one!",
            "What do you call a fake noodle? An impasta!",
            "Why did the coffee file a police report? It got mugged!",
            "What do you call a belt made out of watches? A waist of time!",
            "Why did the scarecrow win an award? Because he was outstanding in his field!",
            "What do you call a fish wearing a crown? A kingfish!",
            "Why don't skeletons fight each other? They don't have the guts!",
            "Why did the tomato turn red? Because it saw the salad dressing!",
            "What do you call a bear with no teeth? A gummy bear!",
            "Why did the math book look sad? Because it had too many problems!",
            "What do you call a pile of cats? A meowtain",
            "Why did the golfer bring two pairs of pants? In case he got a hole in one!",
            "What do you call a fake noodle? An impasta!",
            "Why did the coffee file a police report? It got mugged!",
            "What do you call a belt made out of watches? A waist of time!",
            "Why did the scarecrow win an award? Because he was outstanding in his field!"};

    @Override
    public void handle(String[] args, Datastore datastore, MessageReceivedEvent event) {
        int random = (int) (Math.random() * jokes.length);
        event.getChannel().sendMessage(jokes[random]).queue();
    }
}
