/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.BlockTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.world.level.block.Blocks;

public class PlaceCobwebBlockEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for(var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
            if(serverPlayerEntity.level().getBlockState(serverPlayerEntity.blockPosition()).is(BlockTags.NOT_REPLACED_BY_EVENTS))
                continue;
            serverPlayerEntity.level().setBlockAndUpdate(serverPlayerEntity.blockPosition(), Blocks.COBWEB.defaultBlockState());
        }
    }
}
