package me.juancarloscp52.entropy.events.db;

import java.util.ArrayList;
import java.util.List;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.sound.SoundCategory;

public class ForceHorseRidingEvent extends AbstractTimedEvent {
    private List<HorseEntity> spawnedHorses = new ArrayList<>();

    @Override
    public void initClient() {
        Variables.forceRiding = true;
    }

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            spawnedHorses.add(EntityType.HORSE.spawn(player.getWorld(), null, horse -> {
                horse.bondWithPlayer(player);
                horse.saddle(SoundCategory.NEUTRAL);
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

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
