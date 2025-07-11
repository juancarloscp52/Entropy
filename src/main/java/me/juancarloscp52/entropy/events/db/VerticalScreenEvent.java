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

import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;

public class VerticalScreenEvent extends AbstractTimedEvent {

    public static final EventType<VerticalScreenEvent> TYPE = EventType.builder(VerticalScreenEvent::new).category(EventCategory.SCREEN_ASPECT).build();
    Minecraft client;

    @Override
    public void render(GuiGraphics drawContext, DeltaTracker tickCounter) {
        client = Minecraft.getInstance();
        int borderWidth = Mth.floor(client.getWindow().getGuiScaledWidth() * 0.341f);
        drawContext.fill(0, 0, borderWidth, client.getWindow().getGuiScaledHeight(), CommonColors.BLACK);
        drawContext.fill(client.getWindow().getGuiScaledWidth(), 0, client.getWindow().getGuiScaledWidth() - borderWidth, client.getWindow().getGuiScaledHeight(), CommonColors.BLACK);
    }

    @Override
    public EventType<VerticalScreenEvent> getType() {
        return TYPE;
    }
}
