package me.juancarloscp52.entropy.events;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.NetworkingConstants;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.db.HideEventsEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public abstract class AbstractTimedEvent implements Event {

    protected short tickCount = 0;
    protected boolean hasEnded = false;

    @Environment(EnvType.CLIENT)
    public void renderQueueItem(MatrixStack matrixStack, float tickdelta, int x, int y) {
        if(Variables.doNotShowEvents && !(this instanceof HideEventsEvent))
            return;
        if(Variables.doNotShowEvents)
            y=20;
        MinecraftClient client = MinecraftClient.getInstance();
        int size = client.textRenderer.getWidth(new TranslatableText(EventRegistry.getTranslationKey(this)));
        DrawableHelper.drawTextWithShadow(matrixStack, client.textRenderer, new TranslatableText(EventRegistry.getTranslationKey(this)), client.getWindow().getScaledWidth() - size - 40, y, MathHelper.packRgb(255, 255, 255));
        if (!this.hasEnded()) {
            DrawableHelper.fill(matrixStack, client.getWindow().getScaledWidth() - 35, y + 1, client.getWindow().getScaledWidth() - 5, y + 8, MathHelper.packRgb(70, 70, 70) + (150 << 24));
            DrawableHelper.fill(matrixStack, client.getWindow().getScaledWidth() - 35, y + 1, client.getWindow().getScaledWidth() - 35 + MathHelper.floor(30 * (getTickCount() / (double) getDuration())), y + 8, MathHelper.packRgb(255, 255, 255) + (200 << 24));
        }
    }

    public void tick() {
        tickCount++;
        if (tickCount >= this.getDuration()) {
            List<Event> currentEvents = Entropy.getInstance().eventHandler.currentEvents;
            for (byte i = 0; i < currentEvents.size(); i++) {
                if (currentEvents.get(i).equals(this)) {
                    byte finalI = i;
                    PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> {
                        PacketByteBuf packetByteBuf = PacketByteBufs.create();
                        packetByteBuf.writeByte(finalI);
                        ServerPlayNetworking.send(serverPlayerEntity, NetworkingConstants.END_EVENT, packetByteBuf);
                    });
                }
            }
            this.end();
        }
    }

    public void end() {
        this.hasEnded = true;
    }

    @Environment(EnvType.CLIENT)
    public void tickClient() {
        tickCount++;
        if (tickCount > this.getDuration()) {
            this.endClient();
        }
    }

    @Override
    public short getTickCount() {
        return tickCount;
    }

    @Override
    public void setTickCount(short tickCount) {
        this.tickCount = tickCount;
    }

    @Override
    public boolean hasEnded() {
        return hasEnded;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void endClient() {
        this.hasEnded = true;
    }

    @Override
    public void setEnded(boolean ended) {
        this.hasEnded = ended;
    }
}
