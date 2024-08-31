package org.hinoob.pharadox.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private Map<Long, GuildMusicManager> musicManagers = new HashMap<>();
    private AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

    private PlayerManager() {

        YoutubeAudioSourceManager yt = new YoutubeAudioSourceManager(true);
        yt.setPlaylistPageCount(10);
        audioPlayerManager.registerSourceManager(yt);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public GuildMusicManager get(Guild guild) {
        return musicManagers.computeIfAbsent(guild.getIdLong(), (i) -> {
            GuildMusicManager musicManager = new GuildMusicManager(audioPlayerManager);

            guild.getAudioManager().setSendingHandler(musicManager.getForwarder());
            return musicManager;
        });
    }

    public void play(Guild guild, String trackUrl) {
        GuildMusicManager musicManager = get(guild);
        System.out.println(trackUrl);
        audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                System.out.println("Track loaded: " + audioTrack.getInfo().title);
                musicManager.getScheduler().queue(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                AudioTrack firstTrack = audioPlaylist.getSelectedTrack();

                if(firstTrack == null) {
                    firstTrack = audioPlaylist.getTracks().get(0);
                }

                System.out.println("Playlist loaded: " + firstTrack.getInfo().title);
                musicManager.getScheduler().queue(firstTrack);
            }

            @Override
            public void noMatches() {
                System.out.println("No matches found!");
            }

            @Override
            public void loadFailed(FriendlyException e) {
                e.printStackTrace();
            }
        });

    }

    public static PlayerManager get() {
        if(INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }

}
