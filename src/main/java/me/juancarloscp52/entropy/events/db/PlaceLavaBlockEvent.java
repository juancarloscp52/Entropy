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
import me.juancarloscp52.entropy.EntropyTags.BlockTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.level.block.Blocks;

public class PlaceLavaBlockEvent extends AbstractInstantEvent {
    public static final EventType<PlaceLavaBlockEvent> TYPE = EventType.builder(PlaceLavaBlockEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            if(serverPlayerEntity.level().getBlockState(serverPlayerEntity.blockPosition()).is(BlockTags.NOT_REPLACED_BY_EVENTS))
                return;
            serverPlayerEntity.level().setBlockAndUpdate(serverPlayerEntity.blockPosition(), Blocks.LAVA.defaultBlockState());
        });
    }


    @Override
    public EventType<PlaceLavaBlockEvent> getType() {
        return TYPE;
    }
}
