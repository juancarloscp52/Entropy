package me.juancarloscp52.entropy.server;

import com.google.common.collect.Lists;
import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.NetworkingConstants;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
