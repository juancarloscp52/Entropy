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
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;

import java.util.Random;

public class RandomCreeperEvent extends AbstractTimedEvent {
    public static final EventType<RandomCreeperEvent> TYPE = EventType.builder(RandomCreeperEvent::new).build();

    @Override
    public void tick() {
        if(tickCount%70==0){
            Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                if(new Random().nextInt(10)>=6)
                    EntityType.CREEPER.spawn(serverPlayerEntity.serverLevel(), serverPlayerEntity.blockPosition().north(), EntitySpawnReason.EVENT);
                serverPlayerEntity.connection.send(new ClientboundSoundPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.CREEPER_PRIMED), SoundSource.HOSTILE, serverPlayerEntity.getX(), serverPlayerEntity.getY(), serverPlayerEntity.getZ(), 1f, 0.5f, net.minecraft.util.RandomSource.create().nextLong()));
            });
        }
        super.tick();
    }

    @Override
    public short getDuration() {
        return (short) (super.getDuration()*0.75);
    }

    @Override
    public EventType<RandomCreeperEvent> getType() {
        return TYPE;
    }
}
