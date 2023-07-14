package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;

public class CreativeFlightEvent extends AbstractTimedEvent {
    @Override
    @Environment(EnvType.CLIENT)
    public void initClient() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(null != player) {
            if (!(player.isCreative() || player.isSpectator())) {
                player.getAbilities().allowFlying = true;
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void endClient() {
        super.endClient();
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(null != player){
            if(!(player.isCreative() || player.isSpectator())){
                player.getAbilities().allowFlying = false;
                player.getAbilities().flying=false;
                player.sendAbilitiesUpdate();
            }
        }
    }

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            if(!(player.isCreative() || player.isSpectator())) {
                player.getAbilities().allowFlying = true;
            }
        });
    }

    @Override
    public void end() {
        super.end();
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            if(!(player.isCreative() || player.isSpectator())){
                player.getAbilities().allowFlying = false;
                player.getAbilities().flying=false;
                player.sendAbilitiesUpdate();
            }
        });
    }

    @Override
    public void render(DrawContext drawContext, float tickdelta) {}

    @Override
    public short getDuration() {
        return (short)(Entropy.getInstance().settings.baseEventDuration*0.75);
    }
}
