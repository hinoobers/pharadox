package org.hinoob.pharadox.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;

public class SBAPI {

    public static void fetchAuctions(AuctionCallback callback) {
        try {
            URL url = new URL("https://api.hypixel.net/v2/skyblock/auctions");
            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(url.openStream()));
            StringBuilder builder = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();

            JsonObject json = new Gson().fromJson(builder.toString(), JsonObject.class);
            int pages = (int) (json.get("totalPages").getAsInt() * 0.45);

            callback.onAuctionPage(0, json.get("auctions").getAsJsonArray().asList().stream().map(element -> new Auction(element.getAsJsonObject())).toList());

            for(int page = 1; page < pages; page++) {
                URL pageUrl = new URL("https://api.hypixel.net/v2/skyblock/auctions?page=" + page);
                BufferedReader pageReader = new BufferedReader(new java.io.InputStreamReader(pageUrl.openStream()));
                StringBuilder pageBuilder = new StringBuilder();
                String pageLine;

                while((pageLine = pageReader.readLine()) != null) {
                    pageBuilder.append(pageLine);
                }

                pageReader.close();

                JsonObject pageJson = new Gson().fromJson(pageBuilder.toString(), JsonObject.class);

                callback.onAuctionPage(page, pageJson.get("auctions").getAsJsonArray().asList().stream().map(element -> new Auction(element.getAsJsonObject())).toList());
            }
        } catch(MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public interface AuctionCallback {

        void onAuctionPage(int page, List<Auction> auctions);
    }

    @Getter
    public static class Auction {

        private String id;
        private String itemName;
        private int startingBid;
        private int highestBid;

        public Auction(String id) {
            this.id = id;
        }

        public Auction(JsonObject data) {
            this.id = data.get("uuid").getAsString();
            this.itemName = data.get("item_name").getAsString();
            this.startingBid = data.get("starting_bid").getAsInt();
            this.highestBid = data.get("highest_bid_amount").getAsInt();
        }

    }
}
