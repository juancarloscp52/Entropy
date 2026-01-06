package me.juancarloscp52.entropy.client;

import me.juancarloscp52.entropy.Variables;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.color.ColorLerper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.jetbrains.annotations.Nullable;

public class RainbowFogEnvironment extends FogEnvironment {
    @Override
    public void setupFog(FogData fogData, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker) {
        fogData.environmentalStart = 10.0F;
        fogData.environmentalEnd = 150.0F;
    }

    @Override
    public int getBaseColor(ClientLevel clientLevel, Camera camera, int i, float f) {
        return ColorLerper.getLerpedColor(ColorLerper.Type.SHEEP, clientLevel.getGameTime());
    }

    @Override
    public boolean isApplicable(@Nullable FogType fogType, Entity entity) {
        return fogType == FogType.ATMOSPHERIC && Variables.rainbowFog;
    }
}
