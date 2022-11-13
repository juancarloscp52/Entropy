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

public class GlassSightEvent extends AbstractTimedEvent {

    @Override
    public void tick() {
        if(tickCount%5==0){
            for (var serverPlayerEntity : PlayerLookup.all(Entropy.getInstance().eventHandler.server)) {
                var hitRes = serverPlayerEntity.raycast(64, 1, true);
                if (hitRes.getType() == Type.BLOCK) {
                    var blockHitRes = (BlockHitResult) hitRes;
                    serverPlayerEntity.getWorld().setBlockState(blockHitRes.getBlockPos(), Blocks.GLASS.getDefaultState());
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
        return Entropy.getInstance().settings.baseEventDuration;
    }

    @Override
    public String type() {
        return "sight";
    }

}
