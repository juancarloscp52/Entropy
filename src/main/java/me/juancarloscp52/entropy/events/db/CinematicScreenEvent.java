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
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.math.MathHelper;

public class CinematicScreenEvent extends AbstractTimedEvent {

    MinecraftClient client;

    @Override
    public void initClient() {
        client = MinecraftClient.getInstance();
        client.options.smoothCameraEnabled = true;
        Variables.forcedFov = true;
        Variables.fov = 60;
    }

    @Override
    public void tickClient() {
        client = MinecraftClient.getInstance();
        client.options.smoothCameraEnabled = true;
    }

    @Override
    public void endClient() {
        super.endClient();
        client = MinecraftClient.getInstance();
        client.options.smoothCameraEnabled = false;
        Variables.forcedFov = false;
        Variables.fov = 0;
    }

    @Override
    public void render(DrawContext drawContext, RenderTickCounter tickCounter) {
        client = MinecraftClient.getInstance();
        int borderHeight = MathHelper.floor(client.getWindow().getScaledHeight() * 0.12f);
        drawContext.fill(0, 0, client.getWindow().getScaledWidth(), borderHeight, 255 << 24);
        drawContext.fill(0, client.getWindow().getScaledHeight() - borderHeight, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight(), 255 << 24);
    }

    @Override
    public String type() {
        return "screenAspect";
    }
}
