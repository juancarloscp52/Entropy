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

package me.juancarloscp52.entropy.client.integrations.twitch;

import me.juancarloscp52.entropy.client.EntropyClient;
import me.juancarloscp52.entropy.client.EntropyIntegrationsSettings;
import me.juancarloscp52.entropy.client.VotingClient;
import me.juancarloscp52.entropy.client.integrations.Integrations;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.FastColor;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PingEvent;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TwitchIntegrations extends ListenerAdapter implements Integrations {

    private final Configuration config;
    EntropyIntegrationsSettings settings = EntropyClient.getInstance().integrationsSettings;
    private PircBotX ircChatBot;
    private ExecutorService botExecutor;
    private final VotingClient votingClient;
    private long lastJoinMessage = 0;

    public TwitchIntegrations(VotingClient votingClient) {
        this.votingClient = votingClient;
        config = new Configuration.Builder()
                .setAutoNickChange(false)
                .setOnJoinWhoEnabled(false)
                .setCapEnabled(true)
                .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
                .addCapHandler(new EnableCapHandler("twitch.tv/commands"))
                .addCapHandler(new EnableCapHandler("twitch.tv/membership"))
                .setEncoding(StandardCharsets.UTF_8)
                .addServer("irc.chat.twitch.tv", 6697)
                .setSocketFactory(SSLSocketFactory.getDefault())
                .setName(settings.channel.toLowerCase())
                .setServerPassword(settings.authToken.startsWith("oauth:") ? settings.authToken : "oauth:" + settings.authToken)
                .addAutoJoinChannel("#" + settings.channel.toLowerCase())
                .addListener(this)
                .setAutoSplitMessage(false)
                .buildConfiguration();


        this.start();
    }

    @Override
    public void start() {


        this.ircChatBot = new PircBotX(config);

        botExecutor = Executors.newCachedThreadPool();
        botExecutor.execute(() -> {
            try {
                this.ircChatBot.startBot();
            } catch (IOException e) {
                EntropyClient.LOGGER.error("IO Exception while starting bot: " + e.getMessage());
                e.printStackTrace();
            } catch (IrcException e) {
                EntropyClient.LOGGER.error("IRC Exception while starting bot: " + e.getMessage());
                e.printStackTrace();
            }
        });

    }

    @Override
    public void stop() {

        ircChatBot.stopBotReconnect();
        ircChatBot.close();
        botExecutor.shutdown();

    }

    @Override
    public void onMessage(MessageEvent event) {
        EntropyClient.getInstance().clientEventHandler.votingClient.processVote(event.getMessage(), event.getUser().getLogin());
    }

    @Override
    public void onJoin(JoinEvent event) {
        long currentTime = System.currentTimeMillis();
        if(currentTime-lastJoinMessage>30000){
            votingClient.sendMessage("Connected to Entropy Mod");
            lastJoinMessage=currentTime;
        }

    }

    @Override
    public void onPing(PingEvent event) {
        System.out.println("Received Ping from twitch, anwsering...");
        ircChatBot.sendRaw().rawLineNow(String.format("PONG %s\r\n", event.getPingValue()));
    }

    @Override
    public void sendPoll(int voteID, List<String> events) {

        int altOffset = voteID % 2 == 0 ? 4 : 0;
        StringBuilder stringBuilder = new StringBuilder("Current poll:");
        for (int i = 0; i < events.size(); i++)
            stringBuilder.append(String.format("[ %d - %s ] ", 1 + i + altOffset, I18n.get(events.get(i))));

        ircChatBot.sendIRC().message("#" + settings.channel.toLowerCase(), "/me [Entropy Bot] " + stringBuilder);
    }

    @Override
    public void sendMessage(String message) {
        ircChatBot.sendIRC().message("#" + settings.channel.toLowerCase(), "/me [Entropy Bot] " + message);
    }

    @Override
    public int getColor(int alpha) {
        return FastColor.ARGB32.color(alpha,145, 70, 255);
    }
}
