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

public class GlassSightEvent extends AbstractTimedEvent {
    public static final EventType<GlassSightEvent> TYPE = EventType.builder(GlassSightEvent::new).category(EventCategory.SIGHT).build();

    @Override
    public void tick() {
        if(tickCount%5==0){
            for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
                var hitRes = serverPlayerEntity.pick(64, 1, true);
                if (hitRes.getType() == Type.BLOCK) {
                    var blockHitRes = (BlockHitResult) hitRes;
                    if(!serverPlayerEntity.level().getBlockState(blockHitRes.getBlockPos()).is(BlockTags.NOT_REPLACED_BY_EVENTS)){
                        serverPlayerEntity.level().setBlockAndUpdate(blockHitRes.getBlockPos(), Blocks.GLASS.defaultBlockState());
                    }
                }
            }
        }

        super.tick();
    }

    @Override
    public EventType<GlassSightEvent> getType() {
        return TYPE;
    }
}
