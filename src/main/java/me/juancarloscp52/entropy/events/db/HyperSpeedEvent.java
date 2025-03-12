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

import me.juancarloscp52.entropy.events.AbstractAttributeEvent;
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

public class HyperSpeedEvent extends AbstractAttributeEvent {
    public static final EventType<HyperSpeedEvent> TYPE = EventType.builder(HyperSpeedEvent::new).category(EventCategory.SPEED).build();

    @Override
    public List<ActiveModifier> getModifiers() {
        return List.of(new ActiveModifier(Attributes.MOVEMENT_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath("entropy", "hyperspeed"), 5d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)));
    }

    @Override
    public short getDuration() {
        return (short) (super.getDuration() * 1.5);
    }

    @Override
    public EventType<HyperSpeedEvent> getType() {
        return TYPE;
    }
}
