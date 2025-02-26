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

package me.juancarloscp52.entropy.client;

import com.google.gson.Gson;
import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.NetworkingConstants;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class EntropyClient implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final Identifier herobrineAmbienceID = new Identifier("entropy", "ambient.herobrine");
    public static EntropyClient instance;
    public static SoundEvent herobrineAmbience = SoundEvent.of(herobrineAmbienceID);
    public ClientEventHandler clientEventHandler;
    public EntropyIntegrationsSettings integrationsSettings;
    private PostEffectProcessor shader_blur;
    private PostEffectProcessor shader_wobble;
    private PostEffectProcessor shader_invertedColor;
    private PostEffectProcessor shader_monitor;
    private PostEffectProcessor shader_black_and_white;
    public static EntropyClient getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Entropy Client Mod");
        instance = this;
        loadSettings();
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.JOIN_CONFIRM, (client, handler, buf, responseSender) -> {
            short timerDuration = buf.readShort();
            short baseEventDuration = buf.readShort();
            boolean serverIntegrationsEnabled = buf.readBoolean();
            clientEventHandler = new ClientEventHandler(timerDuration, baseEventDuration, serverIntegrationsEnabled);
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.JOIN_SYNC, (client, handler, buf, responseSender) -> {
            if (clientEventHandler == null)
                return;
            int size = buf.readInt();
            if (size == clientEventHandler.currentEvents.size())
                return;
            for (int i = 0; i < size; i++) {
                String eventName = buf.readString();
                boolean ended = buf.readBoolean();
                short tickCount = buf.readShort();
                Event event = EventRegistry.get(eventName);
                if(event==null)
                    continue;
                event.setEnded(ended);
                event.setTickCount(tickCount);
                if (tickCount > 0 && !ended && !(event.isDisabledByAccessibilityMode() && Entropy.getInstance().settings.accessibilityMode))
                    event.initClient();
                client.execute(() -> clientEventHandler.currentEvents.add(event));
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.TICK, (client, handler, buf, responseSender) -> {
            if (clientEventHandler == null)
                return;
            short eventCountDown = buf.readShort();
            client.execute(() -> clientEventHandler.tick(eventCountDown));
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.REMOVE_FIRST, (client, handler, buf, responseSender) -> {
            if (clientEventHandler == null)
                return;
            client.execute(() -> {
                if(clientEventHandler.currentEvents.size()!=0)
                    clientEventHandler.remove((byte) 0);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.REMOVE_ENDED, (client, handler, buf, responseSender) -> {
            if (clientEventHandler == null)
                return;
            client.execute(() -> {
                if(clientEventHandler.currentEvents.size() != 0)
                    clientEventHandler.currentEvents.removeIf(event -> event.hasEnded());
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.ADD_EVENT, (client, handler, buf, responseSender) -> {
            if (clientEventHandler == null)
                return;
            String index = buf.readString();
            Event event = EventRegistry.get(index);
            event.readExtraData(buf);
            client.execute(() -> {
                clientEventHandler.addEvent(event);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.END_EVENT, (client, handler, buf, responseSender) -> {
            if (clientEventHandler == null)
                return;
            byte index = buf.readByte();
            client.execute(() -> {
                Event event = clientEventHandler.currentEvents.get(index);
                if(event != null)
                    event.endClient();
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.NEW_POLL, (client, handler, buf, responseSender) -> {
            if (clientEventHandler == null || clientEventHandler.votingClient == null)
                return;
            int voteID = buf.readInt();
            int size = buf.readInt();
            List<String> events = new ArrayList<>();
            for (int i = 0; i < size - 1; i++) {
                events.add(buf.readString());
            }
            client.execute(() -> clientEventHandler.votingClient.newPoll(voteID, size, events));
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.POLL_STATUS, (client, handler, buf, responseSender) -> {
            if (clientEventHandler == null || clientEventHandler.votingClient == null)
                return;
            int voteID = buf.readInt();
            int[] totalVotes = buf.readIntArray();
            int totalVotesCount = buf.readInt();
            client.execute(() -> clientEventHandler.votingClient.updatePollStatus(voteID, totalVotes, totalVotesCount));
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (clientEventHandler == null)
                return;
            clientEventHandler.endChaos();
            clientEventHandler = null;
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (FabricLoader.getInstance().getModContainer("entropy").isPresent()) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeString(FabricLoader.getInstance().getModContainer("entropy").get().getMetadata().getVersion().getFriendlyString());
                ClientPlayNetworking.send(NetworkingConstants.JOIN_HANDSHAKE, buf);
            }
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (clientEventHandler != null)
                clientEventHandler.render(drawContext, tickDelta);

        });

        //Registry.registerReference()
        Registry.register(Registries.SOUND_EVENT, herobrineAmbienceID, herobrineAmbience);
        ParticleFactoryRegistry.getInstance().register(Entropy.CONSTANT_COLOR_DUST, ConstantColorDustParticle.Factory::new);
    }


    public void renderShaders(float tickDelta) {
        if (Variables.blur) {
            if(shader_blur==null){
                shader_blur=ShaderManager.register(new Identifier("entropy","shaders/post/blur.json"));
            }
            assert shader_blur != null : "Blur shader is null";
            ShaderManager.render(shader_blur,tickDelta);
        } else if (Variables.invertedShader) {
            if(shader_invertedColor==null){
                shader_invertedColor=ShaderManager.register(new Identifier("shaders/post/invert.json"));
            }
            assert shader_invertedColor != null : "Inverted Color shader is null";
            ShaderManager.render(shader_invertedColor,tickDelta);
        } else if (Variables.wobble) {
            if(shader_wobble==null){
                shader_wobble=ShaderManager.register(new Identifier("shaders/post/wobble.json"));
            }
            assert shader_wobble != null : "Wobble shader is null";
            ShaderManager.render(shader_wobble,tickDelta);
        } else if (Variables.monitor) {
            if(shader_monitor==null){
                shader_monitor=ShaderManager.register(new Identifier("entropy", "shaders/post/crt.json"));
            }
            assert shader_monitor != null : "Monitor shader is null";
            ShaderManager.render(shader_monitor,tickDelta);
        }
    }

    public void renderBlackAndWhite(float tickDelta) {
        if (Variables.blackAndWhite) {
            if(shader_black_and_white==null){
                shader_black_and_white=ShaderManager.register(new Identifier("entropy", "shaders/post/black_and_white.json"));
            }
            assert shader_black_and_white != null : "Black & White shader is null";
            ShaderManager.render(shader_black_and_white,tickDelta);
        }
    }

    public void loadSettings() {
        File file = new File("./config/entropy/entropyIntegrationSettings.json");
        Gson gson = new Gson();
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                integrationsSettings = gson.fromJson(fileReader, EntropyIntegrationsSettings.class);
                fileReader.close();
            } catch (IOException e) {
                LOGGER.warn("Could not load entropy integration settings: " + e.getLocalizedMessage());
            }
        } else {
            integrationsSettings = new EntropyIntegrationsSettings();
            saveSettings();
        }
    }

    public void saveSettings() {
        Gson gson = new Gson();
        File file = new File("./config/entropy/entropyIntegrationSettings.json");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(integrationsSettings));
            fileWriter.close();
        } catch (IOException e) {
            LOGGER.warn("Could not save entropy integration settings: " + e.getLocalizedMessage());
        }
    }

}
