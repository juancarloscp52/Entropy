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
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashMap;
import java.util.Map;

public class HalfHeartedEvent extends AbstractTimedEvent {
    public static final EventType<HalfHeartedEvent> TYPE = EventType.builder(HalfHeartedEvent::new).category(EventCategory.HEALTH).build();
    private Map<ServerPlayer,Health> previousHealth = new HashMap<>();

    private record Health(float current, double max) {}

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(this::adjustPlayerHealth);
    }

    private void adjustPlayerHealth(ServerPlayer serverPlayerEntity) {
        AttributeInstance maxHealthAttribute = serverPlayerEntity.getAttribute(Attributes.MAX_HEALTH);
        previousHealth.computeIfAbsent(serverPlayerEntity, player -> new Health(player.getHealth(), maxHealthAttribute.getBaseValue()));
        maxHealthAttribute.setBaseValue(1);

        if (serverPlayerEntity.getHealth() > 1)
            serverPlayerEntity.setHealth(1);
    }

    @Override
    public void end() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(this::endPlayer);
        super.end();
    }

    @Override
    public void endPlayer(ServerPlayer player) {
        Health health = previousHealth.get(player);
        if (health != null) {
            player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health.max());
            player.setHealth(health.current());
        }
    }

    @Override
    public void tick() {
        if (this.getTickCount() % 20 == 0) {
            Entropy.getInstance().eventHandler.getActivePlayers().forEach(this::adjustPlayerHealth);
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
