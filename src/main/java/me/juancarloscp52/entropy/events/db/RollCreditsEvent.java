package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.client.Screens.EntropyCreditsScreen;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class RollCreditsEvent extends AbstractTimedEvent {

    MinecraftClient client;

    @Override
    @Environment(EnvType.CLIENT)
    public void initClient() {
        client = MinecraftClient.getInstance();
        client.openScreen(new EntropyCreditsScreen(this));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void endClient() {
        if (client.currentScreen instanceof EntropyCreditsScreen) {
            client.currentScreen.onClose();
            this.client.mouse.lockCursor();
        }
        this.hasEnded = true;
    }

    @Override
    public String type() {
        return "credits";
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void tickClient() {
        if (getTickCount() % 20 == 0 && client.currentScreen == null) {
            client.openScreen(new EntropyCreditsScreen(this));
        }
        super.tickClient();
    }

    @Override
    public short getDuration() {
        return (short) MathHelper.ceil(Entropy.getInstance().settings.baseEventDuration * 1.25f);
    }
}
