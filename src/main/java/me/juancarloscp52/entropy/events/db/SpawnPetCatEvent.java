package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.registry.Registries;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.random.Random;

public class SpawnPetCatEvent extends AbstractInstantEvent {
    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            Random random = player.getRandom();

            EntityType.CAT.spawn(player.getServerWorld(), null, cat -> {
                cat.setOwner(player);
                cat.setCollarColor(DyeColor.values()[random.nextInt(DyeColor.values().length)]);
                cat.setVariant(Registries.CAT_VARIANT.getRandom(random).get().value());
            }, player.getBlockPos().add(random.nextBetween(-4, 4), random.nextInt(2), random.nextBetween(-4, 4)), SpawnReason.SPAWN_EGG, false, false);
        });
    }
}
