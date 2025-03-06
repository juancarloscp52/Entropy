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

package me.juancarloscp52.entropy.client.Screens;

import me.juancarloscp52.entropy.client.EntropyClient;
import me.juancarloscp52.entropy.events.Event;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.WinScreen;

public class EntropyCreditsScreen extends WinScreen {
    Event currentEvent;

    public EntropyCreditsScreen(Event currentEvent) {
        super(true,() -> {});
        this.currentEvent = currentEvent;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);
        EntropyClient.getInstance().clientEventHandler.render(drawContext, minecraft.getDeltaTracker());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
