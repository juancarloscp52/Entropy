/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.world.Difficulty;

public class VexAttackEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
            var world = serverPlayerEntity.getWorld();
            var random = serverPlayerEntity.getRandom();
            var difficulty = world.getDifficulty();
            var vexAmount = random.nextInt(difficulty == Difficulty.HARD ? 4 : difficulty == Difficulty.NORMAL ? 3 : 2) + 1;
            for (int i = 0; i < vexAmount; i++) {
                var spawnPos = serverPlayerEntity.getBlockPos().add(random.nextInt(25) - 12, random.nextInt(5) - 2, random.nextInt(25) - 12);
                EntityType.VEX.spawn(world, spawnPos, SpawnReason.SPAWN_EGG);
            }
        }
    }
}
