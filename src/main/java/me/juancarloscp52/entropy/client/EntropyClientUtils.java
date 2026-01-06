package me.juancarloscp52.entropy.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class EntropyClientUtils {
    public static void renderOverlay(GuiGraphics guiGraphics, Identifier texture, int color) {
        guiGraphics.blit(
            RenderPipelines.GUI_TEXTURED,
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
