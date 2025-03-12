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

import me.juancarloscp52.entropy.client.Screens.EntropyCreditsScreen;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class RollCreditsEvent extends AbstractTimedEvent {

    public static final EventType<RollCreditsEvent> TYPE = EventType.builder(RollCreditsEvent::new).build();
    Minecraft client;

    @Override
    @Environment(EnvType.CLIENT)
    public void initClient() {
        client = Minecraft.getInstance();
        client.setScreen(new EntropyCreditsScreen(this));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void endClient() {
        client = Minecraft.getInstance();
        if (client.screen instanceof EntropyCreditsScreen) {
            client.screen.onClose();
            this.client.mouseHandler.grabMouse();
        }
        super.endClient();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(GuiGraphics drawContext, DeltaTracker tickCounter) {
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void tickClient() {
        client = Minecraft.getInstance();
        if (getTickCount() % 20 == 0 && client.screen == null) {
            client.setScreen(new EntropyCreditsScreen(this));
        }
        super.tickClient();
    }
    @Override
    public short getDuration() {
        return (short)(super.getDuration()*0.75);
    }

    @Override
    public EventType<RollCreditsEvent> getType() {
        return TYPE;
    }
}
