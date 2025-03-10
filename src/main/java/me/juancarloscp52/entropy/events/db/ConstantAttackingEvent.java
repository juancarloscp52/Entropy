package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.mixin.MultiPlayerGameModeAccessor;
import me.juancarloscp52.entropy.mixin.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;

public class ConstantAttackingEvent extends AbstractTimedEvent {
    @Override
    public void tickClient() {
        super.tickClient();

        Minecraft mc = Minecraft.getInstance();

        switch(mc.hitResult.getType()) {
            case BLOCK:
                ((MinecraftAccessor) mc).setMissTime(0);
                ((MinecraftAccessor) mc).callContinueAttack(true);
                return;
            case ENTITY:
                if(mc.player.getAttackStrengthScale(0.0F) >= 1.0F)
                    ((MinecraftAccessor) mc).callStartAttack();
                break;
            case MISS:
                mc.player.swing(InteractionHand.MAIN_HAND);
                break;
        }

        cancelBlockBreaking(mc);
    }

    @Override
    public void endClient() {
        cancelBlockBreaking(Minecraft.getInstance());
        super.endClient();
    }

    private void cancelBlockBreaking(Minecraft mc) {
        ((MultiPlayerGameModeAccessor) mc.gameMode).setIsDestroying(true);
        ((MinecraftAccessor) mc).callContinueAttack(false);
    }

    @Override
    public String type() {
        return "attack";
    }
}
