/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.util.math.Box;

public class RideClosestMobEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
            var playerPos = serverPlayerEntity.getPos();
            var box = new Box(playerPos.add(64, 64, 64), playerPos.add(-64, -64, -64));
            var mob = serverPlayerEntity.getWorld().getClosestEntity(LivingEntity.class,
                    TargetPredicate.createAttackable(), serverPlayerEntity,
                    playerPos.getX(), playerPos.getY(), playerPos.getZ(), box);
            if (mob != null)
                serverPlayerEntity.startRiding(mob, true);
        }
    }

}
