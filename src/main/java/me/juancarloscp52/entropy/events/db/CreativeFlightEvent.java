package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class CreativeFlightEvent extends AbstractTimedEvent {
    @Override
    @Environment(EnvType.CLIENT)
    public void initClient() {
        MinecraftClient.getInstance().player.getAbilities().allowFlying = true;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void endClient() {
        super.endClient();
        MinecraftClient.getInstance().player.getAbilities().allowFlying = false;
    }

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> player.getAbilities().allowFlying = true);
    }

    @Override
    public void end() {
        super.end();
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> player.getAbilities().allowFlying = false);
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
