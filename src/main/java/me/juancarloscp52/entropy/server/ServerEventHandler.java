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
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ServerEventHandler {

    private final EntropySettings settings = Entropy.getInstance().settings;
    public List<Pair<Event,Short>> currentEvents = new ArrayList<>();
    public MinecraftServer server;
    public VotingServer voting;
    private boolean started = false;
    private short eventCountDown = settings.timerDuration;


    public void init(MinecraftServer server){
        this.server=server;

        if(settings.integrations){
            voting = new VotingServer();
            voting.enable();
        }

        this.started=true;
    }

    public void tick(boolean noNewEvents){

        if(!this.started)
            return;

        if(eventCountDown == 0){

            if(currentEvents.size()>3){

                if(currentEvents.get(0).getLeft().hasEnded()){
                    PlayerLookup.all(server).forEach(serverPlayerEntity ->
                            ServerPlayNetworking.send(serverPlayerEntity,
                                                    NetworkingConstants.REMOVE_FIRST,
                                                    PacketByteBufs.empty()));

                    currentEvents.remove(0);
                }
            }


            if(!noNewEvents){
                // Get next Event from chat votes (if enabled) or randomly
                Pair<Event,Short> event;
                if(settings.integrations){

                    int winner = voting.getWinner();
                    if(winner==-1 || winner == 3)    // -1 - no winner, 3 - Random Event : Get Random Event.
                        event = getRandomEvent(currentEvents,false);
                    else    // Get winner
                        event = voting.events.get(winner);

                    Entropy.LOGGER.info("[Chat Integrations] Winner event: "+ event.getLeft().getTranslationKey());
                    voting.newPoll();


                }else
                    event = getRandomEvent(currentEvents,false);

                // Start the event and add it to the list.
                event.getLeft().init();
                currentEvents.add(event);

                sendEventToPlayers(event);

                // Reset timer.
                eventCountDown = settings.timerDuration;

                Entropy.LOGGER.info("New Event: "+event.getLeft().getTranslationKey() + " total duration: " + event.getLeft().getDuration());
            }
        }

        // Tick all events.
        for(Pair<Event,Short> event : currentEvents){
            if(!event.getLeft().hasEnded())
                event.getLeft().tick();
        }

        // Send tick to clients.
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeShort(eventCountDown);
        PlayerLookup.all(server).forEach(serverPlayerEntity ->
                ServerPlayNetworking.send(serverPlayerEntity,NetworkingConstants.TICK,buf));


        eventCountDown--;
    }

    private void sendEventToPlayers(Pair<Event, Short> event) {
        short eventIndex = event.getRight();

        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeShort(eventIndex);
        PlayerLookup.all(server).forEach(serverPlayerEntity ->
                ServerPlayNetworking.send(serverPlayerEntity,NetworkingConstants.ADD_EVENT,packetByteBuf));

    }

    private Pair<Event,Short> getRandomEvent(List<Pair<Event,Short>> eventArray,boolean isVoting){
        //return new Pair<>(new HerobrineEvent(), (short) 16);
        return EventRegistry.getRandomDifferentEvent(eventArray,isVoting);
    }

    public void endChaos(){
        if(voting!=null)
            voting.disable();

        currentEvents.forEach(eventIntegerPair -> eventIntegerPair.getLeft().end());
    }

    public void endChaosPlayer(ServerPlayerEntity player){
        currentEvents.forEach(event -> {
            if(!event.getLeft().hasEnded())
                event.getLeft().endPlayer(player);
        });
    }
}
