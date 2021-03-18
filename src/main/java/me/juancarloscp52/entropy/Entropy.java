package me.juancarloscp52.entropy;

import com.google.gson.Gson;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.server.ServerEventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Entropy implements ModInitializer {
    public static Entropy instance;

    public static final Logger LOGGER = LogManager.getLogger();
    public ServerEventHandler eventHandler;
    public EntropySettings settings;


    public static Entropy getInstance() {
        return instance;
    }
    @Override
    public void onInitialize() {
        instance = this;
        loadSettings();
        LOGGER.info("Entropy Started");

        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.JOIN_HANDSHAKE,(server, player, handler, buf, responseSender) -> {
            String clientVersion = buf.readString(32767);
            String version = FabricLoader.getInstance().getModContainer("entropy").get().getMetadata().getVersion().getFriendlyString();
            if(version.equals(clientVersion)){
                PacketByteBuf buf1 = PacketByteBufs.create();
                buf1.writeShort(settings.timerDuration);
                buf1.writeShort(settings.baseEventDuration);
                buf1.writeBoolean(settings.integrations);
                ServerPlayNetworking.send(player,NetworkingConstants.JOIN_CONFIRM,buf1);
                if(PlayerLookup.all(server).size()==1){
                    eventHandler = new ServerEventHandler();
                    eventHandler.init(server);
                }

                List<Pair<Event,Short>> currentEvents = eventHandler.currentEvents;
                if(currentEvents.size()>0){
                    PacketByteBuf packet = PacketByteBufs.create();
                    packet.writeInt(currentEvents.size());
                    currentEvents.forEach(eventIntegerPair -> {
                        packet.writeShort(eventIntegerPair.getRight());
                        packet.writeBoolean(eventIntegerPair.getLeft().hasEnded());
                        packet.writeShort(eventIntegerPair.getLeft().getTickCount());
                    });
                    ServerPlayNetworking.send(handler.player, NetworkingConstants.JOIN_SYNC,packet);
                }

                if(settings.integrations && eventHandler.voting!=null){
                    eventHandler.voting.sendNewPollToPlayer(handler.player);
                }
            }else{
                LOGGER.warn(String.format("Player %s (%s) entropy version (%s) does not match server entropy version (%s). Kicking...",player.getEntityName(),player.getUuidAsString(),clientVersion,version));
                player.networkHandler.disconnect(new LiteralText(String.format("Client entropy version (%s) does not match server version (%s).",clientVersion,version)));
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            if(eventHandler==null)
                return;
            eventHandler.endChaosPlayer(handler.player);
            if(PlayerLookup.all(server).size()<=1){
                eventHandler.endChaos();
                eventHandler = null;
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.POLL_STATUS,(server, player, handler, buf, responseSender) -> {
            if(eventHandler==null || eventHandler.voting==null)
                return;
            int voteID=buf.readInt();
            int [] votes = buf.readIntArray();
            server.execute(() -> eventHandler.voting.receiveVotes(voteID,votes));
        });

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            if(eventHandler!=null)
                eventHandler.tick(false);
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if(eventHandler!=null)
                eventHandler.endChaos();
        });
    }


    public void loadSettings() {
        File file = new File("./config/entropy/entropy.json");
        Gson gson = new Gson();
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                settings = gson.fromJson(fileReader, EntropySettings.class);
                fileReader.close();
            } catch (IOException e) {
                LOGGER.warn("Could not load entropy settings: " + e.getLocalizedMessage());
            }
        } else {
            settings = new EntropySettings();
        }
    }

    public void saveSettings() {
        Gson gson = new Gson();
        File file = new File("./config/entropy/entropy.json");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(settings));
            fileWriter.close();
        } catch (IOException e) {
            LOGGER.warn("Could not save entropy settings: " + e.getLocalizedMessage());
        }
    }
}
