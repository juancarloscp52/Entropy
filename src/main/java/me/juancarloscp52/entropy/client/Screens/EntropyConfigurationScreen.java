/*
 * Copyright (c) 2021 juancarloscp52
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package me.juancarloscp52.entropy.client.Screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropySettings;
import me.juancarloscp52.entropy.client.EntropyClient;
import me.juancarloscp52.entropy.client.Screens.Widgets.EntropySliderWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;


public class EntropyConfigurationScreen extends Screen {
    private static final Identifier LOGO = new Identifier("entropy", "textures/logo-with-text.png");
    EntropySettings settings = Entropy.getInstance().settings;

    SliderWidget eventDurationWidget;
    SliderWidget timerDurationWidget;
    ButtonWidget done;

    Screen parent;

    public EntropyConfigurationScreen(Screen parent) {
        super(new TranslatableText("entropy.title"));
        this.parent = parent;
    }

    protected void init() {

        eventDurationWidget = new EntropySliderWidget(this.width / 2 - 160, 50, 150, 20,"entropy.options.eventDuration",(settings.baseEventDuration-300)/1200d,(slider, translationKey, value) -> new TranslatableText("entropy.options.eventDuration", MathHelper.floor(value*60+15)), value -> settings.baseEventDuration = (short) ((1200*value)+300));
        this.addDrawableChild(eventDurationWidget);

        timerDurationWidget = new EntropySliderWidget(this.width / 2 + 10, 50, 150, 20, "entropy.options.timerDuration", (settings.timerDuration-300)/1200d,(slider, translationKey, value) -> new TranslatableText("entropy.options.timerDuration", MathHelper.floor(value*60+15)),value -> settings.timerDuration = (short) ((1200*value)+300));
        this.addDrawableChild(timerDurationWidget);

        ButtonWidget eventSettings = new ButtonWidget(this.width / 2 - 85, 75, 170, 20, new TranslatableText("entropy.options.disableEvents"), button -> this.client.setScreen(new EntropyEventConfigurationScreen(this)));
        this.addDrawableChild(eventSettings);

        ButtonWidget integrationSettings = new ButtonWidget(this.width / 2 - 85, 100, 170, 20, new TranslatableText("entropy.options.integrations.title"), button -> this.client.setScreen(new EntropyIntegrationsScreen(this)));
        this.addDrawableChild(integrationSettings);

        this.done = new ButtonWidget(this.width / 2 - 100, this.height - 30, 200, 20, ScreenTexts.DONE, button -> onDone());
        this.addDrawableChild(done);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        matrices.push();
        matrices.translate(5, 0, 0);
        matrices.scale(0.2f, 0.2f, 0.2f);
        RenderSystem.setShaderTexture(0,LOGO);
        this.drawTexture(matrices, 0, 0, 0, 0, 188, 187);
        matrices.pop();
        RenderSystem.disableBlend();
        TranslatableText title = new TranslatableText("entropy.options.title");
        drawTextWithShadow(matrices, this.textRenderer, title, this.width / 2 - textRenderer.getWidth(title)/2, 10, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private void onDone() {
        EntropyClient.getInstance().saveSettings();
        Entropy.getInstance().saveSettings();
        close();
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}
