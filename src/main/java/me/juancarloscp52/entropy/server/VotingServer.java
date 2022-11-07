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
import me.juancarloscp52.entropy.NetworkingConstants;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

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
            Event newEvent = EventRegistry.getRandomDifferentEvent(currentEvents);
            newEvents.add(newEvent);
            currentEvents.add(newEvent);
        }
        return newEvents;
    }

    public void sendNewPoll() {
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity ->
                ServerPlayNetworking.send(serverPlayerEntity, NetworkingConstants.NEW_POLL, getNewPollPacket()));
    }

    public void sendNewPollToPlayer(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, NetworkingConstants.NEW_POLL, getNewPollPacket());
    }

    public PacketByteBuf getNewPollPacket() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(voteID);
        buf.writeInt(size);
        for (int i = 0; i < size - 1; i++) {
            buf.writeString(EventRegistry.getTranslationKey(events.get(i)));

        }
        return buf;
    }

    public void sendPollStatusToPlayers() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(voteID);
        buf.writeIntArray(totalVotes);
        buf.writeInt(totalVoteCount);
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity ->
                ServerPlayNetworking.send(serverPlayerEntity, NetworkingConstants.POLL_STATUS, buf));
    }

}
