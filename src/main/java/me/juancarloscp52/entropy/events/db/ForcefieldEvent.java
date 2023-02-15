package me.juancarloscp52.entropy.events.db;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class ForcefieldEvent extends AbstractTimedEvent {
    private static final List<EntityType<?>> DISALLOWED_ENTITY_TYPES = Arrays.asList(EntityType.AREA_EFFECT_CLOUD,
            EntityType.END_CRYSTAL,
            EntityType.GLOW_ITEM_FRAME,
            EntityType.ITEM_FRAME,
            EntityType.LEASH_KNOT,
            EntityType.LIGHTNING_BOLT,
            EntityType.MARKER,
            EntityType.PAINTING,
            EntityType.PLAYER);
    private static final Predicate<Entity> ALLOWED_ENTITY = EntityPredicates.VALID_ENTITY.and(entity -> !DISALLOWED_ENTITY_TYPES.contains(entity.getType()));

    @Override
    public void tick() {
        super.tick();

        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            player.world.getEntitiesByClass(Entity.class, new Box(player.getBlockPos()).expand(5.0D), ALLOWED_ENTITY).forEach(entity -> {
                entity.setVelocity(getVelocity(player.getBlockPos().subtract(entity.getBlockPos())));
            });
        });
    }

    public Vec3d getVelocity(BlockPos relativePos) {
        return new Vec3d(-relativePos.getX(), -relativePos.getY(), -relativePos.getZ()).multiply(0.25D);
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
