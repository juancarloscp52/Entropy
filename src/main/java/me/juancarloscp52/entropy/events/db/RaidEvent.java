package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.block.Blocks;

import java.util.Random;


public class RaidEvent extends AbstractInstantEvent {
    public static final EventType<RaidEvent> TYPE = EventType.builder(RaidEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            var villager = new Villager(EntityType.VILLAGER, player.level());
            villager.setPos(player.position());
            player.level().setBlockAndUpdate(player.blockPosition().offset(0,-1, 0), Blocks.LECTERN.defaultBlockState());
            player.level().addFreshEntity(villager);
            player.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 10000, 1+(new Random()).nextInt(5)));
        });
    }

    @Override
    public EventType<RaidEvent> getType() {
        return TYPE;
    }
}
