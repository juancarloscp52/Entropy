package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class LowGravityEvent extends AbstractTimedEvent {

    @Override
    public void init() {
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> {
            serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 31, 2, true, false, false));
            serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 31, 1, true, false, false));
        });
    }

    @Override
    public void end() {
        this.hasEnded = true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public void tick() {
        if (getTickCount() % 30 == 0) {
            PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> {
                serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 31, 2, true, false, false));
                serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 31, 1, true, false, false));
            });
        }
        super.tick();
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
