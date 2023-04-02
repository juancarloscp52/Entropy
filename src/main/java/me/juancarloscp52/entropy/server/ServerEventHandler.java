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
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServerEventHandler {

    private final EntropySettings settings = Entropy.getInstance().settings;
    public List<Event> currentEvents = new ArrayList<>();
    public MinecraftServer server;
    public VotingServer voting;
    private boolean started = false;
    private short eventCountDown;


    public void init(MinecraftServer server) {
        resetTimer();
        this.server = server;

        if (settings.integrations) {
            voting = new VotingServer();
            voting.enable();
        }

        this.started = true;
    }

    public void tick(boolean noNewEvents) {

        if (!this.started)
            return;

        //Reset timer if countdown is larger than timer duration. This prevents errors while manually executing timer speed events.
        if(eventCountDown>settings.timerDuration/Variables.timerMultiplier)
            resetTimer();


        if (eventCountDown == 0) {

            if (currentEvents.size() > 3) {

                if (currentEvents.get(0).hasEnded()) {
                    PlayerLookup.all(server).forEach(serverPlayerEntity ->
                        ServerPlayNetworking.send(serverPlayerEntity,
                                NetworkingConstants.REMOVE_FIRST,
                                PacketByteBufs.empty()));

                    currentEvents.remove(0);
                }
            }


            if (!noNewEvents) {
                // Get next Event from chat votes (if enabled) or randomly
                Event event;
                if (settings.integrations) {
                    if (voting.events.isEmpty()) {
                        Entropy.LOGGER.info("[Chat Integrations] No random event available");
                        event=null;
                    }else{
                        int winner = voting.getWinner();
                        if (winner == -1 || winner == 3)    // -1 - no winner, 3 - Random Event : Get Random Event.
                            event = getRandomEvent(currentEvents);
                        else    // Get winner
                            event = voting.events.get(winner);
                        Entropy.LOGGER.info("[Chat Integrations] Winner event: " + EventRegistry.getTranslationKey(event));
                    }
                } else
                    event = getRandomEvent(currentEvents);

                runEvent(event);
                if (settings.integrations)
                    voting.newPoll();

                // Reset timer.
                resetTimer();
                if(Event.isValidEvent(event))
                    Entropy.LOGGER.info("New Event: " + EventRegistry.getTranslationKey(event) + " total duration: " + event.getDuration());
                else
                    Entropy.LOGGER.info("New Event not found");
            }
        }

        // Tick all events.
        for (Event event : currentEvents) {
            if (!event.hasEnded())
                event.tick();
        }

        // Send tick to clients.
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeShort(eventCountDown);
        PlayerLookup.all(server).forEach(serverPlayerEntity ->
            ServerPlayNetworking.send(serverPlayerEntity, NetworkingConstants.TICK, buf));


        eventCountDown--;
    }

    public boolean runEvent(Event event) {
        if(Event.isValidEvent(event)) {
            // Start the event and add it to the list.
            currentEvents.add(event);
            event.init();

            sendEventToPlayers(event);
            return true;
        }

        return false;
    }

    private void sendEventToPlayers(Event event) {
        String eventName = EventRegistry.getEventId(event);

        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeString(eventName);
        event.writeExtraData(packetByteBuf);
        PlayerLookup.all(server).forEach(serverPlayerEntity ->
            ServerPlayNetworking.send(serverPlayerEntity, NetworkingConstants.ADD_EVENT, packetByteBuf));

    }

    private Event getRandomEvent(List<Event> eventArray) {
        //return EventRegistry.get("ExplosivePickaxeEvent");
        //return EventRegistry.getNextEventOrdered();
        return EventRegistry.getRandomDifferentEvent(eventArray);
    }

    public void endChaos() {
        if (voting != null)
            voting.disable();

        currentEvents.forEach(Event::end);
    }

    public void endChaosPlayer(ServerPlayerEntity player) {
        currentEvents.forEach(event -> {
            if (!event.hasEnded())
                event.endPlayer(player);
        });
    }

    public void resetTimer(){
        eventCountDown = (short) (settings.timerDuration/Variables.timerMultiplier);
    }


    public List<ServerPlayerEntity> getActivePlayers(){
        return PlayerLookup.all(Entropy.getInstance().eventHandler.server).stream().filter(serverPlayerEntity -> !serverPlayerEntity.isSpectator()).collect(Collectors.toList());
    }
}
