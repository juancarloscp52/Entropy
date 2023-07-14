/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.BlockTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.DrawContext;
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
                    if (currentBlock.isIn(BlockTags.NOT_REPLACED_BY_EVENTS))
                        continue;
                    if(currentBlock.isIn(BlockTags.VOID_SIGHT_BREAKS)){
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
    public void render(DrawContext drawContext, float tickdelta) {
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
