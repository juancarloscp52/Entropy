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
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;

public class SinkingEvent extends AbstractTimedEvent {
    public static final EventType<SinkingEvent> TYPE = EventType.builder(SinkingEvent::new).build();

    @Override
    public void tick() {
        if(tickCount%30==0){
            Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                ServerLevel world = serverPlayerEntity.level();
                int x = serverPlayerEntity.getBlockX(), y =serverPlayerEntity.getBlockY(), z = serverPlayerEntity.getBlockZ();
                for (int j = -1;j<2;j++){
                    for (int k = -1;k<2;k++){
                        BlockPos pos = new BlockPos(x+j,y-1,z+k);
                        if(world.getBlockState(pos).is(BlockTags.NOT_REPLACED_BY_EVENTS))
                            continue;
                        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    }
                }
            });
        }
        super.tick();
    }

    @Override
    public short getDuration() {
        return (short)(super.getDuration()*0.75);
    }

    @Override
    public EventType<SinkingEvent> getType() {
        return TYPE;
    }
}
