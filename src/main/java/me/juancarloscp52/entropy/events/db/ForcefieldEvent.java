package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.EntityTypeTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.function.Predicate;

public class ForcefieldEvent extends AbstractTimedEvent {
    private static final Predicate<Entity> ALLOWED_ENTITY = EntitySelector.ENTITY_STILL_ALIVE.and(entity -> !entity.getType().is(EntityTypeTags.IGNORED_BY_FORCEFIELD_AND_ENTITY_MAGNET));

    @Override
    public void tick() {
        super.tick();

        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            player.level().getEntitiesOfClass(Entity.class, new AABB(player.blockPosition()).inflate(5.0D), ALLOWED_ENTITY).forEach(entity -> {
                entity.setDeltaMovement(getVelocity(player.blockPosition().subtract(entity.blockPosition())));
            });
        });
    }

    public Vec3 getVelocity(BlockPos relativePos) {
        return new Vec3(-relativePos.getX(), -relativePos.getY(), -relativePos.getZ()).scale(0.25D);
    }
}
