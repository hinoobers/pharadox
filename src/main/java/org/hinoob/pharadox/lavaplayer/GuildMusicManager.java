package org.hinoob.pharadox.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import lombok.Getter;

public class GuildMusicManager {

    @Getter private TrackScheduler scheduler;
    @Getter private AudioForwarder forwarder;

    public GuildMusicManager(AudioPlayerManager manager) {
        AudioPlayer audioPlayer = manager.createPlayer();
        scheduler = new TrackScheduler(audioPlayer);
        audioPlayer.addListener(scheduler);
        forwarder = new AudioForwarder(audioPlayer);

    }
}
