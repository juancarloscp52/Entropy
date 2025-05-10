package me.juancarloscp52.entropy.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class EntropyClientUtils {
    public static void renderOverlay(GuiGraphics guiGraphics, ResourceLocation texture, int color) {
        guiGraphics.blit(
            RenderType::guiTexturedOverlay,
            texture,
            0,
            0,
            0.0F,
            0.0F,
            guiGraphics.guiWidth(),
            guiGraphics.guiHeight(),
            guiGraphics.guiWidth(),
            guiGraphics.guiHeight(),
            color
        );
    }
}
