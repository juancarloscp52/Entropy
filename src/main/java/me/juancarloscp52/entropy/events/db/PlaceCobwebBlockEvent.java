/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.block.Blocks;

public class PlaceCobwebBlockEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for(var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
            if((serverPlayerEntity.getWorld().getBlockState(serverPlayerEntity.getBlockPos()).getBlock().equals(Blocks.BEDROCK) ||
                    serverPlayerEntity.getWorld().getBlockState(serverPlayerEntity.getBlockPos()).getBlock().equals(Blocks.END_PORTAL_FRAME) ||
                    serverPlayerEntity.getWorld().getBlockState(serverPlayerEntity.getBlockPos()).getBlock().equals(Blocks.END_PORTAL)))
                continue;
            serverPlayerEntity.getWorld().setBlockState(serverPlayerEntity.getBlockPos(), Blocks.COBWEB.getDefaultState());
        }
    }
}
