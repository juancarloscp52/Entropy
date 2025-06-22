/*
 * Copyright (c) 2021 juancarloscp52
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package me.juancarloscp52.entropy.client.integrations.discord;

import me.juancarloscp52.entropy.client.EntropyClient;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DiscordEventListener extends ListenerAdapter {
    private final DiscordIntegration discordIntegration;

    public DiscordEventListener(DiscordIntegration discordIntegration){
        this.discordIntegration=discordIntegration;
    }


    @Override
    public void onReady(@NotNull ReadyEvent event) {
        if(EntropyClient.getInstance().integrationsSettings.discord.channel!=-1){
            discordIntegration.channel= discordIntegration.jda.getTextChannelById(EntropyClient.getInstance().integrationsSettings.discord.channel);
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!entropy join")){
            discordIntegration.channel = msg.getChannel();
            EntropyClient.getInstance().integrationsSettings.discord.channel=msg.getChannel().getIdLong();
            EntropyClient.getInstance().saveSettings();
            discordIntegration.channel.sendMessage("Joined Text Channel " + discordIntegration.channel.getName()).queue();
        }

    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if(event.getMessageIdLong()==discordIntegration.lastId && event.getUserIdLong()!= discordIntegration.jda.getSelfUser().getIdLong()){
            List<UnicodeEmoji> emojisToRemove = new ArrayList<>();
            UnicodeEmoji addedEmoji = event.getReaction().getEmoji().asUnicode();

            Stream.of("1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣").map(Emoji::fromUnicode).forEach(emojisToRemove::add);

            if(emojisToRemove.contains(addedEmoji)) {
                Message message = event.retrieveMessage().complete();
                User user = event.getUser();
                int index = emojisToRemove.indexOf(addedEmoji);

                emojisToRemove.remove(index);
                if (index > 3) {
                    index -= 4;
                }
                discordIntegration.votingClient.processVote(index, event.getUserId());

                for(UnicodeEmoji emoji : emojisToRemove) {
                    message.removeReaction(emoji, user).queue();
                }
            }
        }
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        if(event.getMessageIdLong()==discordIntegration.lastId && event.getUserIdLong()!= discordIntegration.jda.getSelfUser().getIdLong()){
            switch (event.getReaction().getEmoji().getName()){
                case "1️⃣":
                    discordIntegration.votingClient.removeVote(0, event.getUserId());
                    break;
                case "2️⃣":
                    discordIntegration.votingClient.removeVote(1, event.getUserId());
                    break;
                case "3️⃣":
                    discordIntegration.votingClient.removeVote(2, event.getUserId());
                    break;
                case "4️⃣":
                    discordIntegration.votingClient.removeVote(3, event.getUserId());
                    break;
                case "5️⃣":
                    discordIntegration.votingClient.removeVote(0, event.getUserId());
                    break;
                case "6️⃣":
                    discordIntegration.votingClient.removeVote(1, event.getUserId());
                    break;
                case "7️⃣":
                    discordIntegration.votingClient.removeVote(2, event.getUserId());
                    break;
                case "8️⃣":
                    discordIntegration.votingClient.removeVote(3, event.getUserId());
                    break;
                default:
                    break;
            }
        }
    }
}
