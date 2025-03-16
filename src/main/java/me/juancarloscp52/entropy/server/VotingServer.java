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

package me.juancarloscp52.entropy.server;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropySettings;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import me.juancarloscp52.entropy.events.EventType;
import me.juancarloscp52.entropy.networking.ClientboundNewPoll;
import me.juancarloscp52.entropy.networking.ClientboundPollStatus;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VotingServer {
    private final int size = 4;
    public List<Event> events;
    int[] totalVotes;
    int voteID = -1;
    int totalVoteCount = 0;
    boolean enabled = false;


    public void enable() {
        enabled = true;
        newPoll();
    }

    public void disable() {
        enabled = false;
    }

    public void receiveVotes(int voteID, int[] votes) {

        // If the received voteID does not match the current voteID, quit.
        if (this.voteID != voteID) {
            Entropy.LOGGER.warn("Vote Skipped, VoteID does not match ({} != {})", this.voteID, voteID);
            return;
        }

        // Add received votes.
        if (votes.length == this.totalVotes.length) {
            for (int i = 0; i < votes.length; i++) {
                this.totalVotes[i] += votes[i];
                this.totalVoteCount += votes[i];
            }
        }

        this.sendPollStatusToPlayers();

    }

    public int getWinner() {

        if(Entropy.getInstance().settings.votingMode == EntropySettings.VotingMode.MAJORITY){
            return getWinnerByMajority();
        }else{
            return getWinnerByPercentage();
        }

    }
    public int getWinnerByMajority() {

        int bigger = 0, biggerIndex = -1;
        for (int i = 0; i < 4; i++) {
            int current = totalVotes[i];
            if (current > bigger) {
                bigger = current;
                biggerIndex = i;
            }
        }

        if (bigger == 0)
            return -1;

        return biggerIndex;
    }
    public int getWinnerByPercentage() {

        Random random = new Random();
        if(totalVoteCount<=0)
            return -1;


        int vote = random.nextInt(1,totalVoteCount+1);

        int voteRange=0;
        for (int i = 0; i < 4; i++) {
            voteRange+=totalVotes[i];
            if (vote <= voteRange) {
                return i;
            }
        }
        return  -1;
    }

    public void newPoll() {
        this.totalVotes = new int[size];
        this.events = getRandomEvents(size - 1);
        this.totalVoteCount = 0;
        this.voteID++;
        this.sendNewPoll();
    }

    private List<Event> getRandomEvents(int size) {
        List<Event> newEvents = new ArrayList<>();
        List<Event> currentEvents = new ArrayList<>(Entropy.getInstance().eventHandler.currentEvents);
        for (int i = 0; i < size; i++) {
            Holder.Reference<EventType<?>> newEvent = EventRegistry.getRandomDifferentEvent(currentEvents);
            if(newEvent != null)
                newEvents.add(newEvent.value().create());
        }
        return newEvents;
    }

    public void sendNewPoll() {
        final ClientboundNewPoll packet = getNewPollPacket();
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity ->
                ServerPlayNetworking.send(serverPlayerEntity, packet));
    }

    public void sendNewPollToPlayer(ServerPlayer player) {
        ServerPlayNetworking.send(player, getNewPollPacket());
    }

    public ClientboundNewPoll getNewPollPacket() {
        return new ClientboundNewPoll(voteID, events.isEmpty()
            ? List.of(Component.translatable("entropy.queue.no_event"))
            : events.stream().map(Event::getDescription).toList()
        );
    }

    public void sendPollStatusToPlayers() {
        final ClientboundPollStatus pollStatus = new ClientboundPollStatus(voteID, totalVotes, totalVoteCount);
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity ->
                ServerPlayNetworking.send(serverPlayerEntity, pollStatus));
    }

}
