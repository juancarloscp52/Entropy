package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.mixin.WolfInvoker;
import net.minecraft.Util;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;

public class SpawnPetDogEvent extends AbstractInstantEvent {
    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            RandomSource random = player.getRandom();

            EntityType.WOLF.spawn(player.serverLevel(), wolf -> {
                wolf.tame(player);
                ((WolfInvoker) wolf).invokeSetCollarColor(Util.getRandom(DyeColor.values(), random));
            }, player.blockPosition().offset(random.nextIntBetweenInclusive(-4, 4), random.nextInt(2), random.nextIntBetweenInclusive(-4, 4)), EntitySpawnReason.MOB_SUMMONED, false, false);
        });
    }
}
