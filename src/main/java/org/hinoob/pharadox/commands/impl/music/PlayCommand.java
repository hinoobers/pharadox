package org.hinoob.pharadox.commands.impl.music;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hinoob.pharadox.commands.MessageCommand;
import org.hinoob.pharadox.commands.SlashCommand;
import org.hinoob.pharadox.datastore.Datastore;
import org.hinoob.pharadox.lavaplayer.PlayerManager;

public class PlayCommand extends MessageCommand {


    @Override
    public String getName() {
        return "play";
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
        GuildVoiceState voiceState = event.getMember().getVoiceState();

        if(!voiceState.inAudioChannel()) {
            event.getChannel().sendMessageEmbeds(error("You need to be in a voice channel to use this command!")).queue();
            return;
        }

        GuildVoiceState state = event.getGuild().getSelfMember().getVoiceState();
        if(!state.inAudioChannel()) {
            event.getGuild().getAudioManager().openAudioConnection(voiceState.getChannel());
        } else {
            if(state.getChannel() != voiceState.getChannel()) {
                event.getGuild().getAudioManager().openAudioConnection(voiceState.getChannel());
            }
        }

        PlayerManager playerManager = PlayerManager.get();
        playerManager.play(event.getGuild(), args[0]);
    }
}
