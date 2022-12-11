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

package me.juancarloscp52.entropy.client;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.client.integrations.discord.DiscordIntegration;
import me.juancarloscp52.entropy.client.integrations.twitch.TwitchIntegrations;
import me.juancarloscp52.entropy.client.integrations.youtube.YoutubeIntegrations;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClientEventHandler {


    public List<Event> currentEvents = new ArrayList<>();
    public VotingClient votingClient;
    public MinecraftClient client;
    short eventCountDown;
    private ServerBossBar bar;
    short timerDuration;
    final short timerDurationFinal;
    boolean serverIntegrations;

    public ClientEventHandler(short timerDuration, short baseEventDuration, boolean integrations) {
        this.client = MinecraftClient.getInstance();
        this.timerDuration = timerDuration;
        this.timerDurationFinal = timerDuration;
        this.eventCountDown = timerDuration;
        this.serverIntegrations = integrations;

        Entropy.getInstance().settings.baseEventDuration = baseEventDuration;

        if (Entropy.getInstance().settings.integrations && integrations) {
            votingClient = new VotingClient();
            votingClient.setIntegrations(switch(EntropyClient.getInstance().integrationsSettings.integrationType) {
                case 1 -> new TwitchIntegrations(votingClient);
                case 2 -> new DiscordIntegration(votingClient);
                default  -> new YoutubeIntegrations(votingClient);
            });
            votingClient.enable();
        }

        this.bar=new ServerBossBar(Text.of(""), BossBar.Color.GREEN, BossBar.Style.NOTCHED_20);
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player ->  bar.addPlayer(player));
    }

    public void tick(short eventCountDown) {
        this.timerDuration= (short) (timerDurationFinal/Variables.timerMultiplier);
        this.eventCountDown = eventCountDown;

        if (eventCountDown % 10 == 0 && votingClient != null) {
            votingClient.sendVotes();
        }
        if(!client.player.isSpectator()) {
            for (Event event : currentEvents) {
                if (!event.hasEnded())
                    event.tickClient();
            }
        }
    }

    public void render(MatrixStack matrixStack, float tickdelta) {

        // Render active event effects
        currentEvents.forEach(event -> {
            if (!event.hasEnded() && !client.player.isSpectator())
                event.render(matrixStack, tickdelta);
        });


        double time = timerDuration - eventCountDown;
        int width = MinecraftClient.getInstance().getWindow().getScaledWidth();

        // Render top bar
        this.bar.setPercent((float)(time / timerDuration));

        // Render Event Queue...
        for (int i = 0; i < currentEvents.size(); i++) {
            currentEvents.get(i).renderQueueItem(matrixStack, tickdelta, width - 200, 20 + (i * 13));
        }

        // Render Poll...
        if (Entropy.getInstance().settings.integrations && serverIntegrations && votingClient != null && votingClient.enabled) {
            votingClient.render(matrixStack);
        }

    }

    public void remove(byte index) {
        currentEvents.remove(index);
    }

    public void addEvent(String registryIndex) {
        Event event = EventRegistry.get(registryIndex);
        if(!client.player.isSpectator())
            event.initClient();
        currentEvents.add(event);
    }

    public void endChaos() {

        EntropyClient.LOGGER.info("Ending events...");
        currentEvents.forEach(eventIntegerPair -> {
            if (!eventIntegerPair.hasEnded())
                eventIntegerPair.endClient();
        });

        if (votingClient != null && votingClient.enabled)
            votingClient.disable();

        // Reload settings, removing downloaded settings in constructor from the server.
        EntropyClient.getInstance().loadSettings();
    }
}
