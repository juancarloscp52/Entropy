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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

public class VerticalScreenEvent extends AbstractTimedEvent {

    MinecraftClient client;

    @Override
    public void render(DrawContext drawContext, float tickdelta) {
        client = MinecraftClient.getInstance();
        int borderWidth = MathHelper.floor(client.getWindow().getScaledWidth() * 0.341f);
        drawContext.fill(0, 0, borderWidth, client.getWindow().getScaledHeight(), 255 << 24);
        drawContext.fill(client.getWindow().getScaledWidth(), 0, client.getWindow().getScaledWidth() - borderWidth, client.getWindow().getScaledHeight(), 255 << 24);
    }

    @Override
    public String type() {
        return "screenAspect";
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
