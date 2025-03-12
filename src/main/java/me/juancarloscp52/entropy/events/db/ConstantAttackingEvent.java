package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;
import me.juancarloscp52.entropy.mixin.MinecraftAccessor;
import me.juancarloscp52.entropy.mixin.MultiPlayerGameModeAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;

public class ConstantAttackingEvent extends AbstractTimedEvent {
    public static final EventType<ConstantAttackingEvent> TYPE = EventType.builder(ConstantAttackingEvent::new).category(EventCategory.ATTACK).build();

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
    public EventType<ConstantAttackingEvent> getType() {
        return TYPE;
    }
}
