package me.juancarloscp52.entropy.client;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.client.integrations.TwitchIntegrations;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class ClientEventHandler {


    short eventCountDown;
    public List<Pair<Event,Short>> currentEvents = new ArrayList<>();
    public VotingClient votingClient;
    public MinecraftClient client;
    short timerDuration;
    boolean serverIntegrations;

    public ClientEventHandler(short timerDuration, short baseEventDuration,boolean integrations){
        this.client=MinecraftClient.getInstance();
        this.timerDuration=timerDuration;
        this.eventCountDown=timerDuration;
        this.serverIntegrations=integrations;

        Entropy.getInstance().settings.baseEventDuration=baseEventDuration;

        if(Entropy.getInstance().settings.integrations && integrations){
            votingClient=new VotingClient();
            votingClient.setIntegrations(new TwitchIntegrations());
            votingClient.enable();
        }
    }

    public void tick(short eventCountDown){

        this.eventCountDown=eventCountDown;

        if(eventCountDown%10==0 && votingClient!=null){
            votingClient.sendVotes();
        }
        for(Pair<Event,Short> event : currentEvents){
            if(!event.getLeft().hasEnded())
                event.getLeft().tickClient();
        }

    }
    public void render(MatrixStack matrixStack, float tickdelta){

        // Render active event effects
        currentEvents.forEach(event -> {
            if(!event.getLeft().hasEnded())
                event.getLeft().render(matrixStack, tickdelta);
        });

        // Render top bar
        double time = timerDuration - eventCountDown;
        int width = MinecraftClient.getInstance().getWindow().getScaledWidth();
        DrawableHelper.fill(matrixStack,0,0,width,10, 150<<24);
        DrawableHelper.fill(matrixStack,0,0, MathHelper.floor(width*(time/ timerDuration)),10, (votingClient!=null? votingClient.getColor():MathHelper.packRgb(70,150,70)) + (255 << 24));

        // Render Event Queue...
        for (int i=0; i<currentEvents.size();i++){
            currentEvents.get(i).getLeft().renderQueueItem(matrixStack,tickdelta, width-200, 20+(i*13));
        }

        // Render Poll...
        if(Entropy.getInstance().settings.integrations && serverIntegrations && votingClient !=null && votingClient.enabled){
            votingClient.render(matrixStack);
        }

    }

    public void remove(byte index){
        currentEvents.remove(index);
    }

    public void addEvent(short registryIndex){
        Pair<Event,Short> event = EventRegistry.get(registryIndex);
        event.getLeft().initClient();
        currentEvents.add(event);
    }

    public void endChaos(){

        EntropyClient.LOGGER.info("Ending events...");
        currentEvents.forEach(eventIntegerPair -> {
            if(!eventIntegerPair.getLeft().hasEnded())
                eventIntegerPair.getLeft().endClient();
        });

        if(votingClient!=null && votingClient.enabled)
            votingClient.disable();

        // Reload settings, removing downloaded settings in constructor from the server.
        EntropyClient.getInstance().loadSettings();
    }
}
