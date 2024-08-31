package org.hinoob.pharadox.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    @Getter private final AudioPlayer player;
    @Getter private final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        player.startTrack(queue.poll(), false);
    }

    public void queue(AudioTrack track) {
        System.out.println("Queued: " + track.getInfo().title);
        if(!player.startTrack(track, true)) {
            System.out.println(player.getVolume() + " " + player.getPlayingTrack().getInfo().author);
            queue.offer(track);
        }
    }
}
