package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.Entropy;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class CinematicScreenEvent extends AbstractTimedEvent {

    MinecraftClient client;
    public CinematicScreenEvent() {
        this.translationKey="entropy.events.cinematicScreen";
    }

    @Override
    public void initClient() {
        client=MinecraftClient.getInstance();
        client.options.smoothCameraEnabled=true;
        Variables.forcedFov=true;
        Variables.fov=60;
    }

    @Override
    public void endClient() {
        this.hasEnded=true;
        client.options.smoothCameraEnabled=false;
        Variables.forcedFov=false;
        Variables.fov=0;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
        client=MinecraftClient.getInstance();
        int borderHeight = MathHelper.floor(client.getWindow().getScaledHeight()*0.12f);
        DrawableHelper.fill(matrixStack,0,0, client.getWindow().getScaledWidth(),borderHeight,255<<24);
        DrawableHelper.fill(matrixStack,0,client.getWindow().getScaledHeight()-borderHeight,client.getWindow().getScaledWidth(),client.getWindow().getScaledHeight(),255<<24);
    }

    @Override
    public String type() {
        return "screenAspect";
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
