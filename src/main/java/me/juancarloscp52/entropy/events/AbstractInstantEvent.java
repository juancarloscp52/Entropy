package me.juancarloscp52.entropy.events;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;

public abstract class AbstractInstantEvent implements Event{

    public String translationKey;

    public void end(){}
    @Override
    @Environment(EnvType.CLIENT)
    public void endClient() {}

    @Environment(EnvType.CLIENT)
    public void render(MatrixStack matrixStack, float tickdelta){}
    @Environment(EnvType.CLIENT)
    public void renderQueueItem(MatrixStack matrixStack, float tickdelta, int x, int y) {
        MinecraftClient client = MinecraftClient.getInstance();
        int size = client.textRenderer.getWidth(new TranslatableText(translationKey));
        DrawableHelper.drawTextWithShadow(matrixStack,MinecraftClient.getInstance().textRenderer, new TranslatableText(translationKey),client.getWindow().getScaledWidth()-size-40,y, MathHelper.packRgb(255,255,255));
    }
    @Override
    public String getTranslationKey() {
        return translationKey;
    }

    public void tick(){}
    @Environment(EnvType.CLIENT)
    public void tickClient(){}
    public short getTickCount(){
        return 0;
    }

    @Override
    public void setTickCount(short index) {
    }

    public short getDuration(){
        return 0;
    }
    public boolean hasEnded() {
        return true;
    }

    @Override
    public void setEnded(boolean ended) {}
}
