package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;

import java.util.ArrayList;
import java.util.List;

public class ForceHorseRidingEvent extends AbstractTimedEvent {
    private List<HorseEntity> spawnedHorses = new ArrayList<>();

    @Override
    public void initClient() {
        Variables.forceRiding = true;
    }

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            spawnedHorses.add(EntityType.HORSE.spawn(player.getServerWorld(), horse -> {
                horse.bondWithPlayer(player);
                horse.saddle(Items.SADDLE.getDefaultStack(), SoundCategory.NEUTRAL);
                horse.setInvulnerable(true);
                player.startRiding(horse);
            }, player.getBlockPos(), SpawnReason.SPAWN_EGG, false, false));
        });
        Variables.forceRiding = true;
    }

    @Override
    public void endClient() {
        super.endClient();
        Variables.forceRiding = false;
    }

    @Override
    public void end() {
        super.end();
        Variables.forceRiding = false;
        spawnedHorses.forEach(Entity::discard);
    }
}
