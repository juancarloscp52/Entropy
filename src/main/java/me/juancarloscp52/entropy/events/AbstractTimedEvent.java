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

package me.juancarloscp52.entropy.events;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.db.HideEventsEvent;
import me.juancarloscp52.entropy.networking.S2CEndEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public abstract class AbstractTimedEvent implements Event {

    protected short tickCount = 0;
    protected boolean hasEnded = false;

    @Environment(EnvType.CLIENT)
    public void renderQueueItem(DrawContext drawContext, float tickdelta, int x, int y) {
        if(Variables.doNotShowEvents && !(this instanceof HideEventsEvent))
            return;
        if(Variables.doNotShowEvents)
            y=20;
        MinecraftClient client = MinecraftClient.getInstance();
        MutableText eventName = Text.translatable(EventRegistry.getTranslationKey(this));

        if(isDisabledByAccessibilityMode() && Entropy.getInstance().settings.accessibilityMode)
            eventName.formatted(Formatting.STRIKETHROUGH);

        int size = client.textRenderer.getWidth(eventName);
        drawContext.drawTextWithShadow(client.textRenderer, eventName, client.getWindow().getScaledWidth() - size - 40, y, ColorHelper.Argb.getArgb(255,255, 255, 255));
        if (!this.hasEnded()) {
            drawContext.fill(client.getWindow().getScaledWidth() - 35, y + 1, client.getWindow().getScaledWidth() - 5, y + 8, ColorHelper.Argb.getArgb(150,70, 70, 70));
            drawContext.fill(client.getWindow().getScaledWidth() - 35, y + 1, client.getWindow().getScaledWidth() - 35 + MathHelper.floor(30 * (getTickCount() / (double) getDuration())), y + 8, ColorHelper.Argb.getArgb(200,255, 255, 255));
        }
    }

    public void tick() {
        tickCount++;
        if (tickCount >= this.getDuration()) {
            List<Event> currentEvents = Entropy.getInstance().eventHandler.currentEvents;
            for (byte i = 0; i < currentEvents.size(); i++) {
                if (currentEvents.get(i).equals(this)) {
                    final S2CEndEvent endEvent = new S2CEndEvent(i);
                    PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity ->
                        ServerPlayNetworking.send(serverPlayerEntity, endEvent)
                    );
                }
            }
            this.end();
        }
    }

    public void end() {
        this.hasEnded = true;
    }

    @Environment(EnvType.CLIENT)
    public void tickClient() {
        tickCount++;
        if (tickCount > this.getDuration()) {
            this.endClient();
        }
    }

    @Override
    public short getTickCount() {
        return tickCount;
    }

    @Override
    public void setTickCount(short tickCount) {
        this.tickCount = tickCount;
    }

    @Override
    public boolean hasEnded() {
        return hasEnded;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void endClient() {
        this.hasEnded = true;
    }

    @Override
    public void setEnded(boolean ended) {
        this.hasEnded = ended;
    }
}
