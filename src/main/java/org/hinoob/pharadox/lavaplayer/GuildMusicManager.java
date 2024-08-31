package org.hinoob.pharadox.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import lombok.Getter;

public class GuildMusicManager {

    @Getter private TrackScheduler scheduler;
    @Getter private AudioForwarder forwarder;

    public GuildMusicManager(AudioPlayerManager manager) {
        this.scheduler = new TrackScheduler(manager.createPlayer());
        this.forwarder = new AudioForwarder(scheduler.getPlayer());
        scheduler.getPlayer().addListener(scheduler);

    }
}
