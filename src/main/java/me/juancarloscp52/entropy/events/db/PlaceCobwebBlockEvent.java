/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Blocks;

public class PlaceCobwebBlockEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for(var serverPlayerEntity : PlayerLookup.all(Entropy.getInstance().eventHandler.server)) {
            serverPlayerEntity.getWorld().setBlockState(serverPlayerEntity.getBlockPos(), Blocks.COBWEB.getDefaultState());
        }
    }
}
