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
import me.juancarloscp52.entropy.networking.ClientboundEndEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.util.List;

public abstract class AbstractTimedEvent implements Event {

    protected short tickCount = 0;
    private boolean hasEnded = false;

    public void tick() {
        tickCount++;
        if (tickCount >= this.getDuration()) {
            List<Event> currentEvents = Entropy.getInstance().eventHandler.currentEvents;
            for (byte i = 0; i < currentEvents.size(); i++) {
                if (currentEvents.get(i).equals(this)) {
                    final ClientboundEndEvent endEvent = new ClientboundEndEvent(i);
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

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
