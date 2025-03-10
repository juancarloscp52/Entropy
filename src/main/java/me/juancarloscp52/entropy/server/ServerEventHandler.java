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
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import me.juancarloscp52.entropy.events.TypedEvent;
import me.juancarloscp52.entropy.networking.ClientboundAddEvent;
import me.juancarloscp52.entropy.networking.ClientboundRemoveFirst;
import me.juancarloscp52.entropy.networking.ClientboundTick;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServerEventHandler {

    private final EntropySettings settings = Entropy.getInstance().settings;
    public List<TypedEvent<?>> currentEvents = new ArrayList<>();
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

                if (currentEvents.get(0).event().hasEnded()) {
                    PlayerLookup.all(server).forEach(serverPlayerEntity ->
                            ServerPlayNetworking.send(serverPlayerEntity, ClientboundRemoveFirst.INSTANCE));

                    currentEvents.remove(0);
                }
            }


            if (!noNewEvents) {
                // Get next Event from chat votes (if enabled) or randomly
                TypedEvent<?> typedEvent;
                if (settings.integrations) {
                    if (voting.events.isEmpty()) {
                        Entropy.LOGGER.info("[Chat Integrations] No random event available");
                        typedEvent=null;
                    }else{
                        int winner = voting.getWinner();
                        if (winner == -1 || winner == 3)    // -1 - no winner, 3 - Random Event : Get Random Event.
                            typedEvent = EventRegistry.getRandomDifferentEvent(currentEvents);
                        else    // Get winner
                            typedEvent = voting.events.get(winner);
                        Entropy.LOGGER.info("[Chat Integrations] Winner event: {}", EventRegistry.getTranslationKey(typedEvent.type()));
                    }
                } else
                    typedEvent = EventRegistry.getRandomDifferentEvent(currentEvents);

                runEvent(typedEvent);
                if (settings.integrations)
                    voting.newPoll();

                // Reset timer.
                resetTimer();
                if(typedEvent != null)
                    Entropy.LOGGER.info("New Event: {} total duration: {}", EventRegistry.getTranslationKey(typedEvent.type()), typedEvent.event().getDuration());
                else
                    Entropy.LOGGER.info("New Event not found");
            }
        }

        // Tick all events.
        for (TypedEvent<?> typedEvent : currentEvents) {
            Event event = typedEvent.event();
            if (!event.hasEnded())
                event.tick();
        }

        // Send tick to clients.
        final ClientboundTick tick = new ClientboundTick(eventCountDown);
        PlayerLookup.all(server).forEach(serverPlayerEntity ->
                ServerPlayNetworking.send(serverPlayerEntity, tick));


        eventCountDown--;
    }

    public boolean runEvent(TypedEvent<?> typedEvent) {
        if(typedEvent != null && EventRegistry.doesWorldHaveRequiredFeatures(typedEvent.type(), server.overworld())) {
            // Start the event and add it to the list.
            typedEvent.event().init();
            currentEvents.add(typedEvent);

            sendEventToPlayers(typedEvent);
            return true;
        }

        return false;
    }

    private void sendEventToPlayers(TypedEvent<?> typedEvent) {
        PlayerLookup.all(server).forEach(serverPlayerEntity ->
                ServerPlayNetworking.send(serverPlayerEntity, new ClientboundAddEvent(typedEvent)));
    }

    public void endChaos() {
        if (voting != null)
            voting.disable();

        currentEvents.stream().map(TypedEvent::event).forEach(Event::end);
    }

    public void endChaosPlayer(ServerPlayer player) {
        currentEvents.stream().map(TypedEvent::event).forEach(event -> {
            if (!event.hasEnded())
                event.endPlayer(player);
        });
    }

    public void resetTimer(){
        eventCountDown = (short) (settings.timerDuration/Variables.timerMultiplier);
    }


    public List<ServerPlayer> getActivePlayers(){
        return PlayerLookup.all(Entropy.getInstance().eventHandler.server).stream().filter(serverPlayerEntity -> !serverPlayerEntity.isSpectator()).collect(Collectors.toList());
    }
}
