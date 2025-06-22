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
import me.juancarloscp52.entropy.client.EntropyIntegrationsSettings;
import me.juancarloscp52.entropy.client.VotingClient;
import me.juancarloscp52.entropy.client.integrations.Integration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class DiscordIntegration implements Integration {

    public JDA jda;
    public MessageChannel channel;
    public long lastId =-1;
    public final VotingClient votingClient;
    private final EntropyIntegrationsSettings settings = EntropyClient.getInstance().integrationsSettings;

    public DiscordIntegration (VotingClient votingClient){
        this.votingClient = votingClient;
        this.start();
    }
    @Override
    public void start() {
        try {
            jda = JDABuilder.createDefault(EntropyClient.getInstance().integrationsSettings.discord.token).setActivity(Activity.playing("Entropy: Chaos Mod")).build();
            jda.addEventListener(new DiscordEventListener(this));
        } catch(Exception e) {
            System.err.println("Error occured while connecting to Discord");
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {
        if(jda != null)
            jda.shutdownNow();
    }

    @Override
    public void sendPoll(int voteID, List<Component> events) {
        if(lastId!=-1)
            channel.deleteMessageById(lastId).queue();
        EmbedBuilder poll = new EmbedBuilder();
        Random random = new Random();
        poll.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
        poll.setDescription("Entropy: Chaos Mod");
        poll.setTitle("\uD83D\uDDF3️ Vote For The Next Event");
        poll.addField(getReaction(voteID, 1) + "  " + events.get(0).getString(), "", false);
        poll.addField(getReaction(voteID, 2) + "  " + events.get(1).getString(), "", false);
        poll.addField(getReaction(voteID, 3) + "  " + events.get(2).getString(), "", false);
        poll.addField(getReaction(voteID, 4) + "  " + events.get(3).getString(), "", false);
        poll.setFooter("React to this message with one of these emojis","https://media.forgecdn.net/avatars/356/538/637516966184620115.png");
        try{
            this.channel.sendMessageEmbeds(poll.build()).queue(message -> {
                setLastId(message.getIdLong());
                message.addReaction(Emoji.fromUnicode(getReaction(voteID, 1))).queue();
                message.addReaction(Emoji.fromUnicode(getReaction(voteID, 2))).queue();
                message.addReaction(Emoji.fromUnicode(getReaction(voteID, 3))).queue();
                message.addReaction(Emoji.fromUnicode(getReaction(voteID, 4))).queue();
            });
        }catch (NullPointerException e){
            System.err.println("Could not send discord poll. Channel was null.");
        }
    }

    private String getReaction(final int voteID, int option) {
        if (settings.shouldUseAlternateOffsets() && voteID % 2 == 0) {
            option += 4;
        }
        return switch (option) {
            case 1 -> "1️⃣";
            case 2 -> "2️⃣";
            case 3 -> "3️⃣";
            case 4 -> "4️⃣";
            case 5 -> "5️⃣";
            case 6 -> "6️⃣";
            case 7 -> "7️⃣";
            case 8 -> "8️⃣";
            default -> "⚠";
        };
    }

    private void setLastId(long id){
        this.lastId=id;
    }

    @Override
    public void sendMessage(String message) {
        this.channel.sendMessage(message).queue();
    }

    @Override
    public int getColor(int alpha) {
        return ARGB.color(alpha,88, 101, 242);
    }
}
