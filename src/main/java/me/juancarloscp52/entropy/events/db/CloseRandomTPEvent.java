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
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.MinecraftServer;

public class CloseRandomTPEvent extends AbstractInstantEvent {

    int count = 0;
    MinecraftServer server;

    @Override
    public void init() {
        server = Entropy.getInstance().eventHandler.server;
        PlayerLookup.all(server).forEach(serverPlayerEntity -> {
            serverPlayerEntity.stopRiding();
            server.getCommandManager().execute(server.getCommandSource(), "/spreadplayers " + serverPlayerEntity.getX() + " " + serverPlayerEntity.getZ() + " 0 50 false " + serverPlayerEntity.getEntityName());
        });

    }

    @Override
    public void tick() {
        if (count <= 2) {
            if (count == 2) {
                PlayerLookup.all(server).forEach(serverPlayerEntity -> {
                    serverPlayerEntity.getEntityWorld().breakBlock(serverPlayerEntity.getBlockPos(), false);
                    serverPlayerEntity.getEntityWorld().breakBlock(serverPlayerEntity.getBlockPos().up(), false);
                });
            }
            count++;
        }
        super.tick();
    }

    @Override
    public boolean hasEnded() {
        return count > 2;
    }
}
