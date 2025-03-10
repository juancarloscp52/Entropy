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
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerPlayer;
import java.util.Optional;


public interface Event {

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
    void renderQueueItem(GuiGraphics drawContext, float tickdelta, int x, int y);

    void tick();

    @Environment(EnvType.CLIENT)
    void tickClient();

    short getTickCount();

    void setTickCount(short index);

    short getDuration();

    boolean hasEnded();

    void setEnded(boolean ended);

    default String type() {
        return "none";
    }

    default boolean isDisabledByAccessibilityMode() {
        return false;
    }

    default Optional<String> getExtraData() {
        return Optional.empty();
    }

    @Environment(EnvType.CLIENT)
    default void readExtraData(String subEventId) {}
}
