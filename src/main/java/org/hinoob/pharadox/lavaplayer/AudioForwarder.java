package org.hinoob.pharadox.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

public class AudioForwarder implements AudioSendHandler {

    private final AudioPlayer audioPlayer;
    private final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    private final MutableAudioFrame audioFrame = new MutableAudioFrame();

    public AudioForwarder(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.audioFrame.setBuffer(byteBuffer);
    }

    @Override
    public boolean canProvide() {
        return audioPlayer.provide(audioFrame);
    }

    @Nullable
    @Override
    public ByteBuffer provide20MsAudio() {
        return byteBuffer.flip();
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}