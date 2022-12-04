package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;

public class TeleportToEndEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            var end = player.server.getWorld(ServerWorld.END);


            // set spawnpoint if there is any
            if(player.getSpawnPointPosition()==null){
                player.setSpawnPoint(ServerWorld.OVERWORLD, player.getBlockPos(),0,true,false);
            }
            // place an enderchest in the overworld player's spawnpoint
            player.getWorld().setBlockState(player.getSpawnPointPosition(), Blocks.ENDER_CHEST.getDefaultState());
            // teleport to end
            player.moveToWorld(end);
            // place an enderchest in the end
            end.setBlockState(player.getBlockPos(), Blocks.ENDER_CHEST.getDefaultState());

        });
    }
}
