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
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.Random;

public class RandomCreeperEvent extends AbstractTimedEvent {

    @Override
    public void init() {

    }

    @Override
    public void end() {
        this.hasEnded = true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public void tick() {
        if(tickCount%70==0){
            PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> {
                if(new Random().nextInt(10)>=6)
                    EntityType.CREEPER.spawn(serverPlayerEntity.getServerWorld(), null, null, null, serverPlayerEntity.getBlockPos().north(), SpawnReason.SPAWN_EGG, true, false);
                serverPlayerEntity.networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.HOSTILE, serverPlayerEntity.getX(), serverPlayerEntity.getY(), serverPlayerEntity.getZ(), 1f, 0.5f));
            });
        }
        super.tick();
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
