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
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class ServerEventHandler {

    private final EntropySettings settings = Entropy.getInstance().settings;
    public List<Event> currentEvents = new ArrayList<>();
    public MinecraftServer server;
    public VotingServer voting;
    private boolean started = false;
    private short eventCountDown = settings.timerDuration;


    public void init(MinecraftServer server) {
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

                    int winner = voting.getWinner();
                    if (winner == -1 || winner == 3)    // -1 - no winner, 3 - Random Event : Get Random Event.
                        event = getRandomEvent(currentEvents);
                    else    // Get winner
                        event = voting.events.get(winner);

                    Entropy.LOGGER.info("[Chat Integrations] Winner event: " + EventRegistry.getTranslationKey(event));
                    voting.newPoll();


                } else
                    event = getRandomEvent(currentEvents);

                // Start the event and add it to the list.
                event.init();
                currentEvents.add(event);

                sendEventToPlayers(event);

                // Reset timer.
                eventCountDown = settings.timerDuration;

                Entropy.LOGGER.info("New Event: " + EventRegistry.getTranslationKey(event) + " total duration: " + event.getDuration());
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

    private void sendEventToPlayers(Event event) {
        String eventName = EventRegistry.getEventId(event);

        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeString(eventName);
        PlayerLookup.all(server).forEach(serverPlayerEntity ->
                ServerPlayNetworking.send(serverPlayerEntity, NetworkingConstants.ADD_EVENT, packetByteBuf));

    }

    private Event getRandomEvent(List<Event> eventArray) {
        //return EventRegistry.get("HideEventsEvent");
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
}
