package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.Entropy;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;

public class HyperSlowEvent extends AbstractTimedEvent {
    EntityAttributeModifier modifier;

    public HyperSlowEvent() {
        this.translationKey="entropy.events.hyperSlowEvent";
    }

    @Override
    public void init() {
        modifier = new EntityAttributeModifier("hyperSpeed",-0.8d, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> serverPlayerEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addTemporaryModifier(modifier));
    }

    @Override
    public void end() {
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> serverPlayerEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).removeModifier(modifier.getId()));
        this.hasEnded=true;
    }
    @Override
    public void endPlayer(ServerPlayerEntity player) {
        player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).removeModifier(modifier.getId());
    }
    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}

    @Override
    public void tick() {
        if(getTickCount()%20==0){
            PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> {
                if(serverPlayerEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getModifier(modifier.getId())==null)
                    serverPlayerEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addTemporaryModifier(modifier);
            });
        }
        super.tick();
    }

    @Override
    public String type() {
        return "speed";
    }

    @Override
    public short getDuration() {
        return (short)(Entropy.getInstance().settings.baseEventDuration *2);
    }
}
