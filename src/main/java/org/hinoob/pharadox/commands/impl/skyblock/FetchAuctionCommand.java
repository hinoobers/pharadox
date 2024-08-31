package org.hinoob.pharadox.commands.impl.skyblock;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.util.SBAPI;

import java.util.List;

public class FetchAuctionCommand extends MessageCommand {

    @Override
    public String getName() {
        return "fetchauction";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getPrefix(Datastore datastore) {
        return "!";
    }

    @Override
    public void handle(String[] args, Datastore datastore, MessageReceivedEvent event) {
        if(args.length == 0) {
            event.getChannel().sendMessageEmbeds(error("Please provide a valid item name!")).queue();
            return;
        }

        String itemName = String.join(" ", args);

        SBAPI.fetchAuctions((page, auctions) -> {
            for(SBAPI.Auction auction : auctions) {
                if(auction.getItemName().toLowerCase().contains(itemName.toLowerCase())) {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle(auction.getItemName());
                    builder.addField("Starting bid", auction.getStartingBid() + " coins", true);
                    builder.addField("Highest bid", auction.getHighestBid() + " coins", true);

                    event.getChannel().sendMessageEmbeds(builder.build()).queue();
                    break;
                }
            }
        });
    }
}
