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
import me.juancarloscp52.entropy.client.VotingClient;
import me.juancarloscp52.entropy.client.integrations.Integrations;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.FastColor;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class DiscordIntegration implements Integrations {

    public JDA jda;
    public MessageChannel channel;
    public long lastId =-1;
    public final VotingClient votingClient;
    public DiscordIntegration (VotingClient votingClient){
        this.votingClient = votingClient;
        this.start();
    }
    @Override
    public void start() {
        try {
            jda = JDABuilder.createDefault(EntropyClient.getInstance().integrationsSettings.discordToken).setActivity(Activity.playing("Entropy: Chaos Mod")).build();
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
    public void sendPoll(int voteID, List<String> events) {
        if(lastId!=-1)
            channel.deleteMessageById(lastId).queue();
        EmbedBuilder poll = new EmbedBuilder();
        Random random = new Random();
        poll.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
        poll.setDescription("Entropy: Chaos Mod");
        poll.setTitle("\uD83D\uDDF3️ Vote For The Next Event");
        poll.addField("1️⃣  "+ I18n.get(events.get(0)), "",false);
        poll.addField("2️⃣  " + I18n.get(events.get(1)),"",false);
        poll.addField("3️⃣  " + I18n.get(events.get(2)),"",false);
        poll.addField("4️⃣  " + I18n.get(events.get(3)),"",false);
        poll.setFooter("React to this message with one of these emojis","https://media.forgecdn.net/avatars/356/538/637516966184620115.png");
        try{
            this.channel.sendMessageEmbeds(poll.build()).queue(message -> {
                setLastId(message.getIdLong());
                message.addReaction(Emoji.fromUnicode("1️⃣")).queue();
                message.addReaction(Emoji.fromUnicode("2️⃣")).queue();
                message.addReaction(Emoji.fromUnicode("3️⃣")).queue();
                message.addReaction(Emoji.fromUnicode("4️⃣")).queue();
            });
        }catch (NullPointerException e){
            System.err.println("Could not send discord poll. Channel was null.");
        }
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
        return FastColor.ARGB32.color(alpha,88, 101, 242);
    }
}
