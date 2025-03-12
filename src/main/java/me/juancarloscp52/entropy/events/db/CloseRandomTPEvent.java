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
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.server.MinecraftServer;

public class CloseRandomTPEvent extends AbstractInstantEvent {

    public static final EventType<CloseRandomTPEvent> TYPE = EventType.builder(CloseRandomTPEvent::new).build();
    int count = 0;
    MinecraftServer server;

    @Override
    public void init() {
        server = Entropy.getInstance().eventHandler.server;
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            serverPlayerEntity.stopRiding();
            server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "spreadplayers " + serverPlayerEntity.getX() + " " + serverPlayerEntity.getZ() + " 0 50 false " + serverPlayerEntity.getName().getString());
        });

    }

    @Override
    public void tick() {
        if (count <= 2) {
            if (count == 2) {
                Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                    serverPlayerEntity.getCommandSenderWorld().destroyBlock(serverPlayerEntity.blockPosition(), false);
                    serverPlayerEntity.getCommandSenderWorld().destroyBlock(serverPlayerEntity.blockPosition().above(), false);
                });
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
    public EventType<CloseRandomTPEvent> getType() {
        return TYPE;
    }
}
