package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.EntityTypeTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

public class ForcefieldEvent extends AbstractTimedEvent {
    private static final Predicate<Entity> ALLOWED_ENTITY = EntityPredicates.VALID_ENTITY.and(entity -> !entity.getType().isIn(EntityTypeTags.IGNORED_BY_FORCEFIELD_AND_ENTITY_MAGNET));

    @Override
    public void tick() {
        super.tick();

        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            player.getWorld().getEntitiesByClass(Entity.class, new Box(player.getBlockPos()).expand(5.0D), ALLOWED_ENTITY).forEach(entity -> {
                entity.setVelocity(getVelocity(player.getBlockPos().subtract(entity.getBlockPos())));
            });
        });
    }

    public Vec3d getVelocity(BlockPos relativePos) {
        return new Vec3d(-relativePos.getX(), -relativePos.getY(), -relativePos.getZ()).multiply(0.25D);
    }
}
