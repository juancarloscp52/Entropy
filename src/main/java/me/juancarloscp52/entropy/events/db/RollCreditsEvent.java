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
import me.juancarloscp52.entropy.client.Screens.EntropyCreditsScreen;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class RollCreditsEvent extends AbstractTimedEvent {

    MinecraftClient client;

    @Override
    @Environment(EnvType.CLIENT)
    public void initClient() {
        client = MinecraftClient.getInstance();
        client.setScreen(new EntropyCreditsScreen(this));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void endClient() {
        if (client.currentScreen instanceof EntropyCreditsScreen) {
            client.currentScreen.close();
            this.client.mouse.lockCursor();
        }
        this.hasEnded = true;
    }

    @Override
    public String type() {
        return "credits";
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void tickClient() {
        if (getTickCount() % 20 == 0 && client.currentScreen == null) {
            client.setScreen(new EntropyCreditsScreen(this));
        }
        super.tickClient();
    }
    @Override
    public short getDuration() {
        return (short)(Entropy.getInstance().settings.baseEventDuration*0.75);
    }
}