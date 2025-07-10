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

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyUtils;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomTPEvent extends AbstractInstantEvent {
    public static final List<Distance> DISTANCES = Arrays.asList(new Distance(50, "close"), new Distance(2500, "far"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RandomTPEvent> STREAM_CODEC = StreamCodec.composite(
            Distance.STREAM_CODEC, RandomTPEvent::distance,
            RandomTPEvent::new
    );
    public static final EventType<RandomTPEvent> TYPE = EventType.builder(RandomTPEvent::createRandom).streamCodec(STREAM_CODEC).build();
    private final Distance distance;
    int count = 0;

    public RandomTPEvent(Distance distance) {
        this.distance = distance;
    }

    public static RandomTPEvent createRandom() {
        Random random = new Random();
        return new RandomTPEvent(DISTANCES.get(random.nextInt(DISTANCES.size())));
    }

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            String netherSafety = "";
            DimensionType dimensionType = player.level().dimensionType();

            if (dimensionType.hasCeiling())
                netherSafety = " under " + (dimensionType.logicalHeight() - 1);

            player.stopRiding();
            Entropy.getInstance().eventHandler.server.getCommands().performPrefixedCommand(player.createCommandSourceStack(), "spreadplayers " + player.position().x() + " " + player.position().z() + " 0 " + distance.value() + netherSafety + " false " + player.getName().getString());
            EntropyUtils.clearPlayerArea(player);
        });
    }

    @Override
    public Component getDescription() {
        return Component.translatable("events.entropy.random_tp." + distance.name());
    }

    @Override
    public void tick() {
        if (count <= 2) {
            if (count == 2) {
                Entropy.getInstance().eventHandler.getActivePlayers().forEach(EntropyUtils::clearPlayerArea);
            }
            count++;
        }
        super.tick();
    }

    @Override
    public void tickClient() {
        if(count <= 2)
            count++;
    }

    @Override
    public boolean hasEnded() {
        return count > 2;
    }

    @Override
    public EventType<RandomTPEvent> getType() {
        return TYPE;
    }

    public Distance distance() {
        return distance;
    }

    public record Distance(int value, String name) {
        public static final StreamCodec<RegistryFriendlyByteBuf, Distance> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Distance::value,
                ByteBufCodecs.STRING_UTF8, Distance::name,
                Distance::new
        );
    }
}
