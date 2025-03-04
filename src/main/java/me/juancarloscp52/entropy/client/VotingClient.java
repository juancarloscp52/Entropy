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

import me.juancarloscp52.entropy.client.integrations.Integrations;
import me.juancarloscp52.entropy.client.websocket.OverlayServer;
import me.juancarloscp52.entropy.networking.ServerboundVotes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import java.util.HashMap;
import java.util.List;

public class VotingClient {

    private final int size = 4;
    List<String> events;
    int[] votes;
    int[] totalVotes;
    int voteID = -1;
    int pollWidth = 195;
    int totalVotesCount = 0;
    boolean enabled = false;
    Integrations integrations;
    Minecraft client = Minecraft.getInstance();

    OverlayServer overlayServer;
    boolean firstVote=true;

    // key is user identifier and value is last vote index
    private HashMap<String, Integer> voteMap = new HashMap<>();

    public void enable() {
        enabled = true;
        this.overlayServer = new OverlayServer();
    }

    public void disable() {
        enabled = false;
        integrations.stop();
        overlayServer.stop();
    }
    public void removeVote(int index, String userId) {
        if (index >= 0 && index < 4) {
            if(voteMap.containsKey(userId) && voteMap.get(userId) == index) {
                votes[voteMap.get(userId)] -=1;
                totalVotes[voteMap.get(userId)] -=1;
                totalVotesCount -=  1;
                voteMap.remove(userId);
            }
        }
    }
    public void processVote(int index, String userId) {
        if (index >= 0 && index < 4) {

            if(voteMap.containsKey(userId)) {
                votes[voteMap.get(userId)] -= 1;
                totalVotes[voteMap.get(userId)] -= 1;
                totalVotesCount -= 1;
            }

            if(userId != null) {
                voteMap.put(userId, index);
            }

            votes[index] += 1;
            totalVotes[index] += 1;
            totalVotesCount += 1;
        }
    }

    public void processVote(String string, String userId) {
        if (enabled && string.trim().length() == 1) {
            int voteIndex = Integer.parseInt(string.trim()) + (voteID % 2 == 0 ? -4 : 0);
            if (voteIndex > 0 && voteIndex < 5) {
                processVote(voteIndex - 1, userId);
            }
        }
    }

    public void newPoll(int voteID, List<String> events) {
        voteMap.clear();
        if(firstVote){
            firstVote=false;
        } else {
            this.overlayServer.onVoteEnd();
        }
        if (this.size == events.size()) {
            this.voteID = voteID;
            this.events = events;
            this.events.add("entropy.voting.randomEvent");
            totalVotesCount = 0;
            votes = new int[4];
            totalVotes = new int[4];
            sendPoll(voteID,events);
            int max = 200;
            for (String key : events){
                int width = client.font.width(Component.literal((5) + ": ").append(Component.translatable(key)));
                if(width > max)
                    max = width;
            }
            pollWidth = max;
        }
    }

    public void updatePollStatus(int voteID, int[] totalVotes, int totalVotesCount) {
        if (this.voteID == voteID) {
            this.totalVotes = totalVotes;
            this.totalVotesCount = totalVotesCount;
        }
    }

    public void setIntegrations(Integrations integration) {
        this.integrations = integration;
    }

    public void render(GuiGraphics drawContext) {
        if(EntropyClient.getInstance().integrationsSettings.showUpcomingEvents) {
            drawContext.drawString(client.font, Component.translatable("entropy.voting.total", this.totalVotesCount), 10, 20, FastColor.ARGB32.color(255,255, 255, 255));

            for (int i = 0; i < 4; i++) {
                renderPollElement(drawContext, i);
            }
        }
    }

    public void renderPollElement(GuiGraphics drawContext, int i) {

        if (this.events == null)
            return;

        double ratio = this.totalVotesCount > 0 ? (double) this.totalVotes[i] / this.totalVotesCount : 0;
        int altOffset = (this.voteID % 2) == 0 && (EntropyClient.getInstance().integrationsSettings.integrationType!=2) ? 4 : 0;
        drawContext.fill(10, 31 + (i * 18), pollWidth+45+ 10 , 35 + (i * 18) + 10, FastColor.ARGB32.color(150,0, 0, 0));
        if(EntropyClient.getInstance().integrationsSettings.showCurrentPercentage)
            drawContext.fill(10, 31 + (i * 18), 10 + Mth.floor((pollWidth+45) * ratio), (35 + (i * 18) + 10), this.getColor(150));
        drawContext.drawString(client.font, Component.literal((1 + i + altOffset) + ": ").append(Component.translatable(this.events.get(i))), 15, 34 + (i * 18), FastColor.ARGB32.color(255,255, 255, 255));

        if(EntropyClient.getInstance().integrationsSettings.showCurrentPercentage){
            Component percentage = Component.literal(Mth.floor(ratio * 100) + " %");
            drawContext.drawString(client.font, percentage, pollWidth + 10 + 42 - client.font.width(percentage), 34 + (i * 18), FastColor.ARGB32.color(255,255, 255, 255));
        }

    }

    public void sendPoll(int voteID, List<String> events) {
        if (EntropyClient.getInstance().integrationsSettings.sendChatMessages)
            integrations.sendPoll(voteID,events);
        this.overlayServer.onNewVote(voteID,events);
    }
    public void sendMessage(String message) {
        if (EntropyClient.getInstance().integrationsSettings.sendChatMessages)
            integrations.sendMessage(message);
    }
    public void sendVotes() {
        if (voteID == -1)
            return;
        ClientPlayNetworking.send(new ServerboundVotes(voteID, votes));
        votes = new int[4];
        this.overlayServer.updateVote(voteID,events,totalVotes);
    }

    public int getColor(int alpha) {
        return integrations.getColor(alpha);
    }
}
