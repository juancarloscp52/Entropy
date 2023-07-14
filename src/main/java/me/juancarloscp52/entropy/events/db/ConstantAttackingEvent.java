package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.mixin.ClientPlayerInteractionManagerAccessor;
import me.juancarloscp52.entropy.mixin.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Hand;

public class ConstantAttackingEvent extends AbstractTimedEvent {
    @Override
    public void tickClient() {
        super.tickClient();

        MinecraftClient mc = MinecraftClient.getInstance();

        switch(mc.crosshairTarget.getType()) {
            case BLOCK:
                ((MinecraftClientAccessor) mc).setAttackCooldown(0);
                ((MinecraftClientAccessor) mc).callHandleBlockBreaking(true);
                return;
            case ENTITY:
                if(mc.player.getAttackCooldownProgress(0.0F) >= 1.0F)
                    ((MinecraftClientAccessor) mc).callDoAttack();
                break;
            case MISS:
                mc.player.swingHand(Hand.MAIN_HAND);
                break;
        }

        cancelBlockBreaking(mc);
    }

    @Override
    public void endClient() {
        cancelBlockBreaking(MinecraftClient.getInstance());
        this.hasEnded = true;
    }

    private void cancelBlockBreaking(MinecraftClient mc) {
        ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).setBreakingBlock(true);
        ((MinecraftClientAccessor) mc).callHandleBlockBreaking(false);
    }

    @Override
    public void render(DrawContext drawContext, float tickdelta) {
    }

    @Override
    public String type() {
        return "attack";
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
