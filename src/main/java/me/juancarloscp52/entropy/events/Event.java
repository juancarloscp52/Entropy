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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ARGB;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;


public interface Event {
    static <T extends Event> StreamCodec<RegistryFriendlyByteBuf, T> streamCodec(EventType.EventSupplier<T> eventSupplier) {
        return new StreamCodec<>() {
            @Override
            public T decode(RegistryFriendlyByteBuf buf) {
                return eventSupplier.create();
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, T event) {}
        };
    }

    default void init() {
    }

    @Environment(EnvType.CLIENT)
    default void initClient() {
    }

    void end();

    @Environment(EnvType.CLIENT)
    void endClient();

    default void endPlayer(ServerPlayer player) {
    }

    @Environment(EnvType.CLIENT)
    default void render(GuiGraphics drawContext, DeltaTracker tickCounter) {
    }

    @Environment(EnvType.CLIENT)
    default void renderQueueItem(GuiGraphics drawContext, int y) {
        Minecraft client = Minecraft.getInstance();
        Component eventName = getDescription();

        int size = client.font.width(eventName);
        drawContext.drawString(client.font, eventName, client.getWindow().getGuiScaledWidth() - size - 40, y, CommonColors.WHITE);
        if (!this.hasEnded()) {
            drawContext.fill(client.getWindow().getGuiScaledWidth() - 35, y + 1, client.getWindow().getGuiScaledWidth() - 5, y + 8, ARGB.color(150,70, 70, 70));
            drawContext.fill(client.getWindow().getGuiScaledWidth() - 35, y + 1, client.getWindow().getGuiScaledWidth() - 35 + Mth.floor(30 * (getTickCount() / (double) getDuration())), y + 8, ARGB.color(200,255, 255, 255));
        }
    }

    default boolean alwaysShowDescription() {
        return false;
    }

    default Component getDescription() {
        final MutableComponent description = Component.translatable(getType().getLanguageKey());
        if (!getType().isEnabled()) {
            return description.withStyle(ChatFormatting.STRIKETHROUGH);
        }

        return description;
    }

    void tick();

    @Environment(EnvType.CLIENT)
    void tickClient();

    short getTickCount();

    void setTickCount(short index);

    short getDuration();

    boolean hasEnded();

    void setEnded(boolean ended);

    EventType<? extends Event> getType();
}
