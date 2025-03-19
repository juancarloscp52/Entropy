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

public abstract class AbstractInstantEvent implements Event {

    public void end() {
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void endClient() {
    }

    public void tick() {
    }

    @Environment(EnvType.CLIENT)
    public void tickClient() {
    }

    public short getTickCount() {
        return 0;
    }

    @Override
    public void setTickCount(short index) {
    }

    public short getDuration() {
        return 0;
    }

    public boolean hasEnded() {
        return true;
    }

    @Override
    public void setEnded(boolean ended) {
    }
}
