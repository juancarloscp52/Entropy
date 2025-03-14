package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import me.juancarloscp52.entropy.mixin.CatInvoker;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;

public class SpawnPetCatEvent extends AbstractInstantEvent {
    public static final EventType<SpawnPetCatEvent> TYPE = EventType.builder(SpawnPetCatEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            RandomSource random = player.getRandom();

            EntityType.CAT.spawn(player.serverLevel(), cat -> {
                cat.tame(player);
                ((CatInvoker) cat).invokeSetCollarColor(Util.getRandom(DyeColor.values(), random));
                cat.setVariant(BuiltInRegistries.CAT_VARIANT.getRandom(random).get());
            }, player.blockPosition().offset(random.nextIntBetweenInclusive(-4, 4), random.nextInt(2), random.nextIntBetweenInclusive(-4, 4)), EntitySpawnReason.EVENT, false, false);
        });
    }

    @Override
    public EventType<SpawnPetCatEvent> getType() {
        return TYPE;
    }
}
