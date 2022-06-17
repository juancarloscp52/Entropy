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

package me.juancarloscp52.entropy;

import com.google.gson.Gson;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
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

import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Entropy implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static Entropy instance;
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
        EventRegistry.register();

        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.JOIN_HANDSHAKE, (server, player, handler, buf, responseSender) -> {
            String clientVersion = buf.readString(32767);
            String version = FabricLoader.getInstance().getModContainer("entropy").get().getMetadata().getVersion().getFriendlyString();
            if (version.equals(clientVersion)) {
                PacketByteBuf buf1 = PacketByteBufs.create();
                buf1.writeShort(settings.timerDuration);
                buf1.writeShort(settings.baseEventDuration);
                buf1.writeBoolean(settings.integrations);
                ServerPlayNetworking.send(player, NetworkingConstants.JOIN_CONFIRM, buf1);
                if (PlayerLookup.all(server).size() == 1) {
                    eventHandler = new ServerEventHandler();
                    eventHandler.init(server);
                }

                List<Event> currentEvents = eventHandler.currentEvents;
                if (currentEvents.size() > 0) {
                    PacketByteBuf packet = PacketByteBufs.create();
                    packet.writeInt(currentEvents.size());
                    currentEvents.forEach(currentEvent -> {
                        packet.writeString(EventRegistry.getEventId(currentEvent));
                        packet.writeBoolean(currentEvent.hasEnded());
                        packet.writeShort(currentEvent.getTickCount());
                    });
                    ServerPlayNetworking.send(handler.player, NetworkingConstants.JOIN_SYNC, packet);
                }

                if (settings.integrations && eventHandler.voting != null) {
                    eventHandler.voting.sendNewPollToPlayer(handler.player);
                }
            } else {
                LOGGER.warn(String.format("Player %s (%s) entropy version (%s) does not match server entropy version (%s). Kicking...", player.getEntityName(), player.getUuidAsString(), clientVersion, version));
                player.networkHandler.disconnect(Text.literal(String.format("Client entropy version (%s) does not match server version (%s).", clientVersion, version)));
            }
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            if (eventHandler == null)
                return;
            eventHandler.endChaosPlayer(handler.player);
            if (PlayerLookup.all(server).size() <= 1) {
                eventHandler.endChaos();
                eventHandler = null;
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.POLL_STATUS, (server, player, handler, buf, responseSender) -> {
            if (eventHandler == null || eventHandler.voting == null)
                return;
            int voteID = buf.readInt();
            int[] votes = buf.readIntArray();
            server.execute(() -> eventHandler.voting.receiveVotes(voteID, votes));
        });

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            if (eventHandler != null)
                eventHandler.tick(false);
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (eventHandler != null)
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
            saveSettings();
        }
    }

    public void saveSettings() {
        Gson gson = new Gson();
        File file = new File("./config/entropy/entropy.json");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(settings));
            fileWriter.close();
        } catch (IOException e) {
            LOGGER.warn("Could not save entropy settings: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
