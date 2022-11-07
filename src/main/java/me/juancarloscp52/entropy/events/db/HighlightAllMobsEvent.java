package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class HighlightAllMobsEvent extends AbstractTimedEvent {

    @Override
    public void init() {
    }

    @Override
    public void tick() {
        PlayerLookup
                .all(Entropy.getInstance().eventHandler.server)
                .forEach(serverPlayerEntity -> serverPlayerEntity.getEntityWorld()
                        .getEntitiesByClass(MobEntity.class,
                                new Box(serverPlayerEntity.getBlockPos().add(64, 64, 64),
                                        serverPlayerEntity.getBlockPos().add(-64, -64, -64)),
                                entity -> true)
                        .forEach(entity -> entity
                                .addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 2, 1))));
        super.tick();
    }

    @Override
    public void end() {
        this.hasEnded = true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
