/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;

public class VexAttackEvent extends AbstractInstantEvent {
    public static final EventType<VexAttackEvent> TYPE = EventType.builder(VexAttackEvent::new).build();

    @Override
    public void init() {
        for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
            var world = serverPlayerEntity.serverLevel();
            var random = serverPlayerEntity.getRandom();
            var difficulty = world.getDifficulty();
            var vexAmount = random.nextInt(difficulty == Difficulty.HARD ? 4 : difficulty == Difficulty.NORMAL ? 3 : 2) + 1;
            for (int i = 0; i < vexAmount; i++) {
                var spawnPos = serverPlayerEntity.blockPosition().offset(random.nextInt(25) - 12, random.nextInt(5) - 2, random.nextInt(25) - 12);
                EntityType.VEX.spawn(world, spawnPos, EntitySpawnReason.EVENT);
            }
        }
    }

    @Override
    public EventType<VexAttackEvent> getType() {
        return TYPE;
    }
}
