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
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.level.GameType;

public class AdventureEvent extends AbstractTimedEvent {
    public static final EventType<AdventureEvent> TYPE = EventType.builder(AdventureEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> serverPlayerEntity.setGameMode(GameType.ADVENTURE));
    }

    @Override
    public void end() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> serverPlayerEntity.setGameMode(GameType.SURVIVAL));
        super.end();
    }

    @Override
    public EventType<AdventureEvent> getType() {
        return TYPE;
    }
}
