/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.BlockTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;

public class VoidSightEvent extends AbstractTimedEvent {
    public static final EventType<VoidSightEvent> TYPE = EventType.builder(VoidSightEvent::new).category(EventCategory.SIGHT).build();

    @Override
    public void tick() {
        if(tickCount%2==0){
            for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
                var hitRes = serverPlayerEntity.pick(64, 1, true);
                if (hitRes.getType() == Type.BLOCK) {
                    var blockHitRes = (BlockHitResult) hitRes;
                    var currentBlock = serverPlayerEntity.level().getBlockState(blockHitRes.getBlockPos());
                    if (currentBlock.is(BlockTags.NOT_REPLACED_BY_EVENTS))
                        continue;
                    if(currentBlock.is(BlockTags.VOID_SIGHT_BREAKS)){
                        serverPlayerEntity.level().destroyBlock(blockHitRes.getBlockPos(), true, serverPlayerEntity);
                    }else{
                        serverPlayerEntity.level().setBlockAndUpdate(blockHitRes.getBlockPos(), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }

        super.tick();
    }

    @Override
    public EventType<VoidSightEvent> getType() {
        return TYPE;
    }
}
