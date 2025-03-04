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
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import me.juancarloscp52.entropy.networking.NetworkingConstants;
import me.juancarloscp52.entropy.networking.S2CJoinConfirm;
import me.juancarloscp52.entropy.networking.S2CJoinSync;
import me.juancarloscp52.entropy.networking.S2CRemoveEnded;
import me.juancarloscp52.entropy.server.ConstantColorDustParticleEffect;
import me.juancarloscp52.entropy.server.ServerEventHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.CommandSource;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Entropy implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static Entropy instance;
    public ServerEventHandler eventHandler;
    public EntropySettings settings;
    public static final ParticleType<ConstantColorDustParticleEffect> CONSTANT_COLOR_DUST = FabricParticleTypes.complex(false, ConstantColorDustParticleEffect.CODEC, ConstantColorDustParticleEffect.PACKET_CODEC);

    private static final DynamicCommandExceptionType ERROR_INVALID_ON_CLIENT = new DynamicCommandExceptionType(eventId -> Text.translatable("entropy.command.invalidClientSide", eventId));
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_EVENT = new DynamicCommandExceptionType(eventId -> Text.translatable("entropy.command.unknownEvent", eventId));

    public static Entropy getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {
        instance = this;
        loadSettings();
        LOGGER.info("Entropy Started");
        EventRegistry.register();
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of("entropy", "constant_color_dust"), CONSTANT_COLOR_DUST);

        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.JOIN_HANDSHAKE, (handshake, context) -> {
            String version = FabricLoader.getInstance().getModContainer("entropy").get().getMetadata().getVersion().getFriendlyString();
            if (version.equals(handshake.clientVersion())) {
                final S2CJoinConfirm confirm = new S2CJoinConfirm(settings.timerDuration, settings.baseEventDuration, settings.integrations);
                final ServerPlayerEntity player = context.player();
                ServerPlayNetworking.send(player, confirm);
                if (PlayerLookup.all(context.server()).size() == 1) {
                    eventHandler = new ServerEventHandler();
                    eventHandler.init(context.server());
                }

                List<Event> currentEvents = eventHandler.currentEvents;
                if (!currentEvents.isEmpty()) {
                    S2CJoinSync sync = new S2CJoinSync(currentEvents.stream().map(currentEvent -> new S2CJoinSync.EventData(
                        EventRegistry.getEventId(currentEvent),
                        currentEvent.hasEnded(),
                        currentEvent.getTickCount()
                    )).toList());
                    ServerPlayNetworking.send(player, sync);
                }

                if (settings.integrations && eventHandler.voting != null) {
                    eventHandler.voting.sendNewPollToPlayer(player);
                }
            } else {
                LOGGER.warn(String.format("Player %s (%s) entropy version (%s) does not match server entropy version (%s). Kicking...", context.player().getName(), context.player().getUuidAsString(), handshake.clientVersion(), version));
                context.player().networkHandler.disconnect(Text.literal(String.format("Client entropy version (%s) does not match server version (%s).", handshake.clientVersion(), version)));
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

        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.VOTES, (votes, context) -> {
            if (eventHandler == null || eventHandler.voting == null)
                return;
            context.server().execute(() -> eventHandler.voting.receiveVotes(votes.voteId(), votes.votes()));
        });

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            if (eventHandler != null)
                eventHandler.tick(false);
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (eventHandler != null)
                eventHandler.endChaos();
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("entropy")
                    .requires(source -> source.hasPermissionLevel(3))
                    .then(CommandManager.literal("clearPastEvents")
                            .executes(source -> {
                                ServerEventHandler eventHandler = Entropy.getInstance().eventHandler;

                                eventHandler.currentEvents.removeIf(Event::hasEnded);
                                PlayerLookup.all(eventHandler.server).forEach(player -> ServerPlayNetworking.send(player, S2CRemoveEnded.INSTANCE));
                                return 0;
                            }))
                    .then(CommandManager.literal("run")
                            .then(CommandManager.argument("event", StringArgumentType.word())
                                    .suggests((context, builder) -> {
                                        Set<String> events = new TreeSet<>(EventRegistry.entropyEvents.keySet());

                                        events.removeIf(event -> !EventRegistry.doesWorldHaveRequiredFeatures(event, context.getSource().getWorld()));
                                        return CommandSource.suggestMatching(events, builder);
                                    })
                                    .executes(source -> {
                                        ServerEventHandler eventHandler = Entropy.getInstance().eventHandler;

                                        if(eventHandler != null) {
                                            String eventId = source.getArgument("event", String.class);

                                            // If running on integrated server, prevent running Stuttering event.
                                            if(FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER && eventId.equals("StutteringEvent")){
                                                throw ERROR_INVALID_ON_CLIENT.create(eventId);
                                            }

                                            if(eventHandler.runEvent(EventRegistry.get(eventId)))
                                                Entropy.LOGGER.warn("New event run via command: " + EventRegistry.getTranslationKey(eventId));
                                            else
                                                throw ERROR_UNKNOWN_EVENT.create(eventId);
                                        }

                                        return 0;
                                    }))));
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
