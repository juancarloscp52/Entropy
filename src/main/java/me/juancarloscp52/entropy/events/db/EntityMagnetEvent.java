package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class EntityMagnetEvent extends ForcefieldEvent {
    public static final EventType<EntityMagnetEvent> TYPE = EventType.builder(EntityMagnetEvent::new).build();

    @Override
    public Vec3 getVelocity(BlockPos relativePos) {
        return new Vec3(relativePos.getX(), relativePos.getY(), relativePos.getZ());
    }

    @Override
    public EventType<EntityMagnetEvent> getType() {
        return TYPE;
    }
}
