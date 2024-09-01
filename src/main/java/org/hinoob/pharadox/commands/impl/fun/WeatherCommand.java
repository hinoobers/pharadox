package org.hinoob.pharadox.commands.impl.fun;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.hinoob.pharadox.commands.SlashCommand;
import org.hinoob.pharadox.datastore.Datastore;

import java.awt.*;
import java.io.BufferedReader;
import java.net.URL;
import java.net.URLConnection;

public class WeatherCommand extends SlashCommand {


    @Override
    public String getName() {
        return "weather";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public CommandData getCommandData() {
        OptionData option = new OptionData(OptionType.STRING, "location", "The location to get the weather for", true);

        option.addChoice("New York", "newyork");
        option.addChoice("Los Angeles", "losangeles");
        option.addChoice("Chicago", "chicago");
        option.addChoice("Houston", "houston");
        option.addChoice("Austria", "austria");
        option.addChoice("Germany", "germany");
        option.addChoice("India", "india");

        return Commands.slash("weather", "Get the weather for a specific location")
                .addOptions(option);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event, Datastore datastore) {
        String apiKey = Dotenv.load().get("WEATHER_API");
        String loc = event.getOption("location").getAsString();

        try {
            URL url = new URL("https://api.tomorrow.io/v4/weather/forecast?location="+loc+"&apikey=" + apiKey);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();

            JsonObject json = new Gson().fromJson(builder.toString(), JsonObject.class);
            JsonObject data = json.get("timelines").getAsJsonObject().get("minutely").getAsJsonArray().get(0).getAsJsonObject();
            String time = data.get("time").getAsString();
            data = data.get("values").getAsJsonObject();

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Weather for " + json.get("location").getAsJsonObject().get("name").getAsString());
            embed.setDescription(time);
            embed.addField("Temperature", data.get("temperature").getAsFloat() + "°C", false);
            embed.addField("Wind Speed", data.get("windSpeed").getAsFloat() + "m/s", false);
            embed.addField("Humidity", data.get("humidity").getAsInt() + "%", false);
            embed.addField("UV Index", data.get("uvIndex").getAsInt() + "", false);
            if(data.get("temperature").getAsInt() >= 22)
                embed.setColor(Color.YELLOW);
            else {
                embed.setColor(Color.BLUE);
            }

            event.replyEmbeds(embed.build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
            event.reply("An error occurred while fetching the weather").queue();
        }
    }
}
