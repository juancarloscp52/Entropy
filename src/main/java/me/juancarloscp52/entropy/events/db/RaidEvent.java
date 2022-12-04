package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.VillagerEntity;

public class RaidEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            var villager = new VillagerEntity(EntityType.VILLAGER, player.getWorld());
            villager.setPosition(player.getPos());
            player.getWorld().setBlockState(player.getBlockPos().add(0,-1, 0), Blocks.LECTERN.getDefaultState());
            player.getWorld().spawnEntity(villager);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BAD_OMEN, 10000, 5));
        });
    }
}
