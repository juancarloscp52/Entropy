/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;

public class VoidSightEvent extends AbstractTimedEvent {

    @Override
    public void tick() {
        if(tickCount%2==0){
            for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
                var hitRes = serverPlayerEntity.raycast(64, 1, true);
                if (hitRes.getType() == Type.BLOCK) {
                    var blockHitRes = (BlockHitResult) hitRes;
                    var currentBlock = serverPlayerEntity.getWorld().getBlockState(blockHitRes.getBlockPos());
                    if (currentBlock.getBlock() == Blocks.BEDROCK ||
                            currentBlock.getBlock() == Blocks.END_PORTAL_FRAME ||
                            currentBlock.getBlock() == Blocks.END_PORTAL)
                        continue;
                    if(currentBlock.isOf(Blocks.CHEST) || currentBlock.isOf(Blocks.TRAPPED_CHEST) || currentBlock.isOf(Blocks.BARREL) || currentBlock.isOf(Blocks.FURNACE) || currentBlock.isOf(Blocks.BLAST_FURNACE) || currentBlock.isOf(Blocks.SMOKER)){
                        serverPlayerEntity.getWorld().breakBlock(blockHitRes.getBlockPos(), true, serverPlayerEntity);
                    }else{
                        serverPlayerEntity.getWorld().setBlockState(blockHitRes.getBlockPos(), Blocks.AIR.getDefaultState());
                    }
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
        return (short) (Entropy.getInstance().settings.baseEventDuration*1);
    }

    @Override
    public String type() {
        return "sight";
    }

}
