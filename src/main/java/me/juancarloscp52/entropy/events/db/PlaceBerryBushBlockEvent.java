/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;

public class PlaceBerryBushBlockEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for(var serverPlayerEntity : PlayerLookup.all(Entropy.getInstance().eventHandler.server)) {
            if((serverPlayerEntity.getWorld().getBlockState(serverPlayerEntity.getBlockPos()).getBlock().equals(Blocks.BEDROCK) ||
                    serverPlayerEntity.getWorld().getBlockState(serverPlayerEntity.getBlockPos()).getBlock().equals(Blocks.END_PORTAL_FRAME) ||
                    serverPlayerEntity.getWorld().getBlockState(serverPlayerEntity.getBlockPos()).getBlock().equals(Blocks.END_PORTAL)))
                continue;

            var state = Blocks.SWEET_BERRY_BUSH.getDefaultState().with(Properties.AGE_3, 2);
            serverPlayerEntity.getWorld().setBlockState(serverPlayerEntity.getBlockPos(), state);
        }
    }

}
