package me.juancarloscp52.entropy.client.Screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropySettings;
import me.juancarloscp52.entropy.client.Screens.Widgets.EntropyEventListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class EntropyEventConfigurationScreen extends Screen {
    private static final Identifier LOGO = new Identifier("entropy", "textures/logo-with-text.png");
    EntropySettings settings = Entropy.getInstance().settings;

    EntropyEventListWidget list;

    Screen parent;

    public EntropyEventConfigurationScreen(Screen parent) {
        super(new TranslatableText("entropy.options.disableEvents"));
        this.parent = parent;
    }

    protected void init() {
        list = new EntropyEventListWidget(MinecraftClient.getInstance(), this.width, this.height, 32, this.height - 32, 25);
        list.addAllFromRegistry();
        this.children.add(list);
        ButtonWidget done = new ButtonWidget(this.width / 2 - 100, this.height - 26, 200, 20, ScreenTexts.DONE, button -> onDone());
        this.addButton(done);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        matrices.push();
        matrices.translate(this.width / 2f - 18.8f, 0, 0);
        matrices.scale(0.2f, 0.2f, 0.2f);
        client.getTextureManager().bindTexture(LOGO);
        this.drawTexture(matrices, 0, 0, 0, 0, 188, 187);
        matrices.pop();
        RenderSystem.disableBlend();
        this.list.render(matrices, mouseX, mouseY, delta);
        this.textRenderer.drawWithShadow(matrices, this.title, this.width / 2f - textRenderer.getWidth(this.title) / 2f, 12, 14737632);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (super.mouseReleased(mouseX, mouseY, button)) {
            return true;
        } else return this.list.mouseReleased(mouseX, mouseY, button);
    }

    private void onDone() {
        settings.disabledEvents = new ArrayList<>();
        this.list.children().forEach(buttonEntry -> {
            if (!buttonEntry.button.isChecked())
                settings.disabledEvents.add(buttonEntry.eventID);
        });
        Entropy.getInstance().saveSettings();
        onClose();
    }

    @Override
    public void onClose() {
        this.client.openScreen(this.parent);
    }
}
