package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.util.math.Box;

public class RideClosestMobEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for (var serverPlayerEntity : PlayerLookup.all(Entropy.getInstance().eventHandler.server)) {
            var playerPos = serverPlayerEntity.getBlockPos();
            var box = new Box(playerPos.add(32, 32, 32), playerPos.add(-32, -32, -32));
            var mob = serverPlayerEntity.getWorld().getClosestEntity(LivingEntity.class,
                    TargetPredicate.createAttackable(), serverPlayerEntity,
                    playerPos.getX(), playerPos.getY(), playerPos.getZ(), box);
            if (mob != null)
                serverPlayerEntity.startRiding(mob, true);
        }
    }

}
