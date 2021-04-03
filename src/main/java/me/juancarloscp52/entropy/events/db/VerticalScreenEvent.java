package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class VerticalScreenEvent extends AbstractTimedEvent {

    MinecraftClient client;

    @Override
    public void initClient() {
        client = MinecraftClient.getInstance();
    }

    @Override
    public void endClient() {
        this.hasEnded = true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
        int borderWidth = MathHelper.floor(client.getWindow().getScaledWidth() * 0.341f);
        DrawableHelper.fill(matrixStack, 0, 0, borderWidth, client.getWindow().getScaledHeight(), 255 << 24);
        DrawableHelper.fill(matrixStack, client.getWindow().getScaledWidth(), 0, client.getWindow().getScaledWidth() - borderWidth, client.getWindow().getScaledHeight(), 255 << 24);
    }

    @Override
    public String type() {
        return "screenAspect";
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
