/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;

public class MiningSightEvent extends AbstractTimedEvent {

    @Override
    public void tick() {
        if(tickCount%10==0){
            for (var serverPlayerEntity : PlayerLookup.all(Entropy.getInstance().eventHandler.server)) {
                var hitRes = serverPlayerEntity.raycast(64, 1, false);
                if (hitRes.getType() == Type.BLOCK) {
                    var blockHitRes = (BlockHitResult) hitRes;
                    var blockPos = blockHitRes.getBlockPos();
                    var world = serverPlayerEntity.getWorld();
                    if (world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK)
                        world.breakBlock(blockPos, true, serverPlayerEntity);
                }
            }
        }

        super.tick();
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public short getDuration() {
        return (short)(Entropy.getInstance().settings.baseEventDuration*0.75);
    }

    @Override
    public String type() {
        return "sight";
    }

}
