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
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;

import java.util.Random;

public class FarRandomTPEvent extends AbstractInstantEvent {
    public static final EventType<FarRandomTPEvent> TYPE = EventType.builder(FarRandomTPEvent::new).build();
    int count = 0;
    MinecraftServer server;

    @Override
    public void init() {
        Random random = new Random();
        BlockPos randomLocation = new BlockPos(random.nextInt(5000) - 2500, 0, random.nextInt(5000) - 2500);
        server = Entropy.getInstance().eventHandler.server;
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            serverPlayerEntity.stopRiding();
            server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "spreadplayers " + randomLocation.getX() + " " + randomLocation.getZ() + " 0 120 false " + serverPlayerEntity.getName().getString());
            EntropyUtils.clearPlayerArea(serverPlayerEntity);
        });

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
    public EventType<FarRandomTPEvent> getType() {
        return TYPE;
    }
}
