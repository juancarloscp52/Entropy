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
import me.juancarloscp52.entropy.EntropySettings.UIStyle;
import me.juancarloscp52.entropy.EntropySettings.VotingMode;
import me.juancarloscp52.entropy.client.EntropyClient;
import me.juancarloscp52.entropy.client.Screens.Widgets.EntropySliderWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;


public class EntropyConfigurationScreen extends Screen {
    private static final Identifier LOGO = new Identifier("entropy", "textures/logo-with-text.png");
    private static final double MAX_DURATION = 3300D; //165 seconds + 15 seconds minimum for a total range of 15s-180s
    EntropySettings settings = Entropy.getInstance().settings;

    SliderWidget eventDurationWidget;
    SliderWidget timerDurationWidget;
    ButtonWidget done;

    Screen parent;

    public EntropyConfigurationScreen(Screen parent) {
        super(Text.translatable("entropy.title"));
        this.parent = parent;
    }

    protected void init() {
        int buttonX = this.width / 2 - 100;
        int buttonWidth = 200;

        eventDurationWidget = new EntropySliderWidget(this.width / 2 - 160, 50, 150, 20,"entropy.options.eventDuration",(settings.baseEventDuration-300)/MAX_DURATION,(slider, translationKey, value) -> Text.translatable("entropy.options.eventDuration", MathHelper.floor((value*(MAX_DURATION/20D))+15)), value -> settings.baseEventDuration = (short) ((MAX_DURATION*value)+300));
        this.addDrawableChild(eventDurationWidget);

        timerDurationWidget = new EntropySliderWidget(this.width / 2 + 10, 50, 150, 20, "entropy.options.timerDuration", (settings.timerDuration-300)/MAX_DURATION,(slider, translationKey, value) -> Text.translatable("entropy.options.timerDuration", MathHelper.floor((value*(MAX_DURATION/20D))+15)),value -> settings.timerDuration = (short) ((MAX_DURATION*value)+300));
        this.addDrawableChild(timerDurationWidget);


        ButtonWidget eventSettings = ButtonWidget.builder(Text.translatable("entropy.options.disableEvents"), button -> this.client.setScreen(new EntropyEventConfigurationScreen(this))).position(buttonX, 75).width(buttonWidth).build();
        this.addDrawableChild(eventSettings);

        ButtonWidget integrationSettings = ButtonWidget.builder(Text.translatable("entropy.options.integrations.title"), button -> this.client.setScreen(new EntropyIntegrationsScreen(this))).width(buttonWidth).position(buttonX, 100).build();
        this.addDrawableChild(integrationSettings);

        CyclingButtonWidget<VotingMode> votingModeButton = CyclingButtonWidget.<VotingMode>builder(votingMode -> votingMode.text)
                .values(VotingMode.values())
                .initially(settings.votingMode)
                .tooltip(votingMode -> Tooltip.of(votingMode.tooltip))
                .build(buttonX, 125, buttonWidth, 20, Text.translatable("entropy.options.votingMode"), (button, newValue) -> settings.votingMode = newValue);
        this.addDrawableChild(votingModeButton);

        // UI style button
        CyclingButtonWidget<UIStyle> uiStyleButton = CyclingButtonWidget.<UIStyle>builder(uiStyle -> uiStyle.text)
                .values(UIStyle.values())
                .initially(settings.UIstyle)
                .tooltip(uiStyle -> Tooltip.of(uiStyle.tooltip))
                .build(buttonX, 150, buttonWidth, 20, Text.translatable("entropy.options.ui"), (button, newValue) -> settings.UIstyle = newValue);
        this.addDrawableChild(uiStyleButton);

        this.addDrawableChild(CyclingButtonWidget.<Boolean>builder(bool -> Text.translatable("entropy.options." + (bool ? "enabled" : "disabled")))
                .values(true, false)
                .initially(settings.accessibilityMode)
                .tooltip(bool -> Tooltip.of(Text.translatable("entropy.options.accessibilityMode.explanation")))
                .build(buttonX, 175, buttonWidth, 20, Text.translatable("entropy.options.accessibilityMode"), (button, newValue) -> settings.accessibilityMode = newValue));

        this.done = ButtonWidget.builder(ScreenTexts.DONE, button -> onDone()).width(buttonWidth).position(this.width / 2 - 100, this.height - 30).build();
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
        Text title = Text.translatable("entropy.options.title");
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
