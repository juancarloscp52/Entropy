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
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.server.MinecraftServer;

public class SpinningMobsEvent extends AbstractTimedEvent {
    int rotation;
    MinecraftServer server;
    @Override
    public void init() {
        server = Entropy.getInstance().eventHandler.server;
        server.getCommandManager().execute(server.getCommandSource(), "/gamerule sendCommandFeedback false");

    }

    @Override
    public void tick() {
        if(tickCount%2!=0){
            super.tick();
            return;
        }
        server.getCommandManager().execute(server.getCommandSource(), "/execute as @e[type=!minecraft:player,type=!minecraft:falling_block] at @s run tp @s ~ ~ ~ "+rotation+" 0");
        rotation+=45;
        if(rotation>360)
            rotation=0;
        super.tick();
    }

    @Override
    public void end() {
        server.getCommandManager().execute(server.getCommandSource(), "/gamerule sendCommandFeedback true");
        this.hasEnded = true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public String type() {
        return "spinning";
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
