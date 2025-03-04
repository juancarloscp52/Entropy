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
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import me.juancarloscp52.entropy.networking.C2SJoinHandshake;
import me.juancarloscp52.entropy.networking.NetworkingConstants;
import me.juancarloscp52.entropy.networking.S2CJoinSync;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gl.PostEffectProcessor;
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
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class EntropyClient implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final Identifier herobrineAmbienceID = Identifier.of("entropy", "ambient.herobrine");
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
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.JOIN_CONFIRM, (confirm, context) -> {
            clientEventHandler = new ClientEventHandler(confirm.timerDuration(), confirm.baseEventDuration(), confirm.integrations());
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.JOIN_SYNC, (sync, context) -> {
            if (clientEventHandler == null)
                return;

            if (sync.events().size() == clientEventHandler.currentEvents.size())
                return;
            for (final S2CJoinSync.EventData data : sync.events()) {
                Event event = EventRegistry.get(data.id());
                if(event==null)
                    continue;
                event.setEnded(data.ended());
                event.setTickCount(data.tickCount());
                if (data.tickCount() > 0 && !data.ended() && !(event.isDisabledByAccessibilityMode() && Entropy.getInstance().settings.accessibilityMode))
                    event.initClient();
                context.client().execute(() -> clientEventHandler.currentEvents.add(event));
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.TICK, (tick, context) -> {
            if (clientEventHandler == null)
                return;
            context.client().execute(() -> clientEventHandler.tick(tick.eventCountDown()));
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.REMOVE_FIRST, (removeFirst, context) -> {
            if (clientEventHandler == null)
                return;
            context.client().execute(() -> {
                if (!clientEventHandler.currentEvents.isEmpty())
                    clientEventHandler.remove((byte) 0);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.REMOVE_ENDED, (removeEnded, context) -> {
            if (clientEventHandler == null)
                return;
            context.client().execute(() -> {
                if (!clientEventHandler.currentEvents.isEmpty())
                    clientEventHandler.currentEvents.removeIf(Event::hasEnded);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.ADD_EVENT, (addEvent, context) -> {
            if (clientEventHandler == null)
                return;
            Event event = EventRegistry.get(addEvent.id());
            addEvent.subEventId().ifPresent(event::readExtraData);
            context.client().execute(() -> {
                clientEventHandler.addEvent(event);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.END_EVENT, (endEvent, context) -> {
            if (clientEventHandler == null)
                return;
            context.client().execute(() -> {
                Event event = clientEventHandler.currentEvents.get(endEvent.index());
                if(event != null)
                    event.endClient();
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.NEW_POLL, (newPoll, context) -> {
            if (clientEventHandler == null || clientEventHandler.votingClient == null)
                return;
            context.client().execute(() -> clientEventHandler.votingClient.newPoll(newPoll.voteId(), newPoll.events()));
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.POLL_STATUS, (pollStatus, context) -> {
            if (clientEventHandler == null || clientEventHandler.votingClient == null)
                return;
            context.client().execute(() -> clientEventHandler.votingClient.updatePollStatus(pollStatus.voteId(), pollStatus.totalVotes(), pollStatus.totalVotesCount()));
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (clientEventHandler == null)
                return;
            clientEventHandler.endChaos();
            clientEventHandler = null;
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            final Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer("entropy");
            if (modContainer.isPresent()) {
                final String version = modContainer.get().getMetadata().getVersion().getFriendlyString();
                ClientPlayNetworking.send(new C2SJoinHandshake(version));
            }
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            if (clientEventHandler != null)
                clientEventHandler.render(drawContext, tickCounter);

        });

        //Registry.registerReference()
        Registry.register(Registries.SOUND_EVENT, herobrineAmbienceID, herobrineAmbience);
        ParticleFactoryRegistry.getInstance().register(Entropy.CONSTANT_COLOR_DUST, ConstantColorDustParticle.Factory::new);
    }


    public void renderShaders(float tickDelta) {
        if (Variables.blur) {
            if(shader_blur==null){
                shader_blur=ShaderManager.register(Identifier.of("entropy", "shaders/post/blur.json"));
            }
            assert shader_blur != null : "Blur shader is null";
            ShaderManager.render(shader_blur,tickDelta);
        } else if (Variables.invertedShader) {
            if(shader_invertedColor==null){
                shader_invertedColor=ShaderManager.register(Identifier.ofVanilla("shaders/post/invert.json"));
            }
            assert shader_invertedColor != null : "Inverted Color shader is null";
            ShaderManager.render(shader_invertedColor,tickDelta);
        } else if (Variables.wobble) {
            if(shader_wobble==null){
                shader_wobble=ShaderManager.register(Identifier.of("entropy","shaders/post/wobble.json"));
            }
            assert shader_wobble != null : "Wobble shader is null";
            ShaderManager.render(shader_wobble,tickDelta);
        } else if (Variables.monitor) {
            if(shader_monitor==null){
                shader_monitor=ShaderManager.register(Identifier.of("entropy", "shaders/post/crt.json"));
            }
            assert shader_monitor != null : "Monitor shader is null";
            ShaderManager.render(shader_monitor,tickDelta);
        }
    }

    public void renderBlackAndWhite(float tickDelta) {
        if (Variables.blackAndWhite) {
            if(shader_black_and_white==null){
                shader_black_and_white=ShaderManager.register(Identifier.of("entropy", "shaders/post/black_and_white.json"));
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
