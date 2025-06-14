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
import net.minecraft.server.MinecraftServer;

public class Teleport0Event extends AbstractInstantEvent {

    public static final EventType<Teleport0Event> TYPE = EventType.builder(Teleport0Event::new).build();
    MinecraftServer server;
    int count = 0;

    @Override
    public void init() {
        server = Entropy.getInstance().eventHandler.server;
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            EntropyUtils.teleportPlayer(serverPlayerEntity, serverPlayerEntity.level().getSharedSpawnPos().getCenter());
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
    public EventType<Teleport0Event> getType() {
        return TYPE;
    }
}
