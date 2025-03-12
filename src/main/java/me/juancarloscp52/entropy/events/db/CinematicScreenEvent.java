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

import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class CinematicScreenEvent extends AbstractTimedEvent {

    public static final EventType<CinematicScreenEvent> TYPE = EventType.builder(CinematicScreenEvent::new).category(EventCategory.SCREEN_ASPECT).build();
    Minecraft client;

    @Override
    public void initClient() {
        client = Minecraft.getInstance();
        client.options.smoothCamera = true;
        Variables.forcedFov = true;
        Variables.fov = 60;
    }

    @Override
    public void tickClient() {
        client = Minecraft.getInstance();
        client.options.smoothCamera = true;
    }

    @Override
    public void endClient() {
        super.endClient();
        client = Minecraft.getInstance();
        client.options.smoothCamera = false;
        Variables.forcedFov = false;
        Variables.fov = 0;
    }

    @Override
    public void render(GuiGraphics drawContext, DeltaTracker tickCounter) {
        client = Minecraft.getInstance();
        int borderHeight = Mth.floor(client.getWindow().getGuiScaledHeight() * 0.12f);
        drawContext.fill(0, 0, client.getWindow().getGuiScaledWidth(), borderHeight, 255 << 24);
        drawContext.fill(0, client.getWindow().getGuiScaledHeight() - borderHeight, client.getWindow().getGuiScaledWidth(), client.getWindow().getGuiScaledHeight(), 255 << 24);
    }

    @Override
    public EventType<CinematicScreenEvent> getType() {
        return TYPE;
    }
}
