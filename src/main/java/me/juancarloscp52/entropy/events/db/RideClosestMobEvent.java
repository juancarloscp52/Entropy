/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;

public class RideClosestMobEvent extends AbstractInstantEvent {
    public static final EventType<RideClosestMobEvent> TYPE = EventType.builder(RideClosestMobEvent::new).build();

    @Override
    public void init() {
        for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
            var playerPos = serverPlayerEntity.position();
            var box = new AABB(playerPos.add(64, 64, 64), playerPos.add(-64, -64, -64));
            var mob = serverPlayerEntity.level().getNearestEntity(LivingEntity.class,
                    TargetingConditions.forCombat(), serverPlayerEntity,
                    playerPos.x(), playerPos.y(), playerPos.z(), box);
            if (mob != null)
                serverPlayerEntity.startRiding(mob, true, true);
        }
    }

    @Override
    public EventType<RideClosestMobEvent> getType() {
        return TYPE;
    }
}
