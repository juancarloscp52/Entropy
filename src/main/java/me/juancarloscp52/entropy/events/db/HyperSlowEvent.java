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
import me.juancarloscp52.entropy.events.AbstractAttributeEvent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;

import java.util.List;

public class HyperSlowEvent extends AbstractAttributeEvent {
    @Override
    public List<ActiveModifier> getModifiers() {
        return List.of(new ActiveModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier("hyperSpeed", -0.8d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)));
    }

    @Override
    public String type() {
        return "speed";
    }

    @Override
    public short getDuration() {
        return (short) (Entropy.getInstance().settings.baseEventDuration * 1.25);
    }
}
