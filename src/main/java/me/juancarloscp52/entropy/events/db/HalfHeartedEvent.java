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
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashMap;
import java.util.Map;

public class HalfHeartedEvent extends AbstractTimedEvent {
    public static final EventType<HalfHeartedEvent> TYPE = EventType.builder(HalfHeartedEvent::new).category(EventCategory.HEALTH).build();
    private Map<ServerPlayer,Float> previousHealth = new HashMap<>();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            serverPlayerEntity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1);
            previousHealth.put(serverPlayerEntity, serverPlayerEntity.getHealth());

            if (serverPlayerEntity.getHealth() > 1)
                serverPlayerEntity.setHealth(1);
        });
    }

    @Override
    public void end() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            serverPlayerEntity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20);
            serverPlayerEntity.setHealth(previousHealth.get(serverPlayerEntity));
        });
        super.end();
    }

    @Override
    public void endPlayer(ServerPlayer player) {
        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20);
        player.setHealth(previousHealth.get(player));
    }

    @Override
    public void tick() {
        if (this.getTickCount() % 20 == 0) {
            Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                serverPlayerEntity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1);

                if(!previousHealth.containsKey(serverPlayerEntity))
                    previousHealth.put(serverPlayerEntity, serverPlayerEntity.getHealth());

                if (serverPlayerEntity.getHealth() > 1)
                    serverPlayerEntity.setHealth(1);
            });
        }
        super.tick();
    }

    @Override
    public short getDuration() {
        return (short) (super.getDuration() * 1.25);
    }

    @Override
    public EventType<HalfHeartedEvent> getType() {
        return TYPE;
    }
}
