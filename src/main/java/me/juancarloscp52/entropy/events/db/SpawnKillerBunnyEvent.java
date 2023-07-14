package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.RabbitEntity.RabbitType;
import net.minecraft.util.math.random.Random;

public class SpawnKillerBunnyEvent extends AbstractInstantEvent {
    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            Random random = player.getRandom();
            int bunnyAmount = random.nextBetween(1, 6);

            for(int i = 0; i < bunnyAmount; i++) {
                EntityType.RABBIT.spawn(player.getServerWorld(), null, rabbit -> rabbit.setVariant(RabbitType.EVIL), player.getBlockPos().add(random.nextBetween(-4, 4), random.nextInt(2), random.nextBetween(-4, 4)), SpawnReason.SPAWN_EGG, false, false);
            }
        });
    }
}
