package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Rabbit.Variant;

public class SpawnKillerBunnyEvent extends AbstractInstantEvent {
    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            RandomSource random = player.getRandom();
            int bunnyAmount = random.nextIntBetweenInclusive(1, 6);

            for(int i = 0; i < bunnyAmount; i++) {
                EntityType.RABBIT.spawn(player.serverLevel(), rabbit -> rabbit.setVariant(Variant.EVIL), player.blockPosition().offset(random.nextIntBetweenInclusive(-4, 4), random.nextInt(2), random.nextIntBetweenInclusive(-4, 4)), EntitySpawnReason.EVENT, false, false);
            }
        });
    }
}
