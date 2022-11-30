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
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DiscordEventListener extends ListenerAdapter {

    private final DiscordIntegration discordIntegration;

    public DiscordEventListener(DiscordIntegration discordIntegration){
        this.discordIntegration=discordIntegration;
    }


    @Override
    public void onReady(@NotNull ReadyEvent event) {
        if(EntropyClient.getInstance().integrationsSettings.discordChannel!=-1){
            discordIntegration.channel= discordIntegration.jda.getTextChannelById(EntropyClient.getInstance().integrationsSettings.discordChannel);
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!entropy join")){
            discordIntegration.channel = msg.getChannel();
            EntropyClient.getInstance().integrationsSettings.discordChannel=msg.getChannel().getIdLong();
            EntropyClient.getInstance().saveSettings();
            discordIntegration.channel.sendMessage("Joined Text Channel " + discordIntegration.channel.getName()).queue();
        }

    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if(event.getMessageIdLong()==discordIntegration.lastId && event.getUserIdLong()!= discordIntegration.jda.getSelfUser().getIdLong()){
            switch (event.getReactionEmote().getName()){
                case "1️⃣":
                    // Remove all previous user reactions to the message.
                    event.retrieveMessage().complete().removeReaction("2️⃣",event.getUser()).queue();
                    event.retrieveMessage().complete().removeReaction("3️⃣",event.getUser()).queue();
                    event.retrieveMessage().complete().removeReaction("4️⃣",event.getUser()).queue();
                    discordIntegration.votingClient.processVote(0, event.getUserId());
                    break;
                case "2️⃣":
                    event.retrieveMessage().complete().removeReaction("1️⃣",event.getUser()).queue();
                    event.retrieveMessage().complete().removeReaction("3️⃣",event.getUser()).queue();
                    event.retrieveMessage().complete().removeReaction("4️⃣",event.getUser()).queue();
                    discordIntegration.votingClient.processVote(1, event.getUserId());
                    break;
                case "3️⃣":
                    event.retrieveMessage().complete().removeReaction("2️⃣",event.getUser()).queue();
                    event.retrieveMessage().complete().removeReaction("1️⃣",event.getUser()).queue();
                    event.retrieveMessage().complete().removeReaction("4️⃣",event.getUser()).queue();
                    discordIntegration.votingClient.processVote(2, event.getUserId());
                    break;
                case "4️⃣":
                    event.retrieveMessage().complete().removeReaction("2️⃣",event.getUser()).queue();
                    event.retrieveMessage().complete().removeReaction("3️⃣",event.getUser()).queue();
                    event.retrieveMessage().complete().removeReaction("1️⃣",event.getUser()).queue();
                    discordIntegration.votingClient.processVote(3, event.getUserId());
                    break;
                default: break;
            }
        }
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        if(event.getMessageIdLong()==discordIntegration.lastId && event.getUserIdLong()!= discordIntegration.jda.getSelfUser().getIdLong()){
            switch (event.getReactionEmote().getName()){
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
                default: break;
            }
        }
    }
}
