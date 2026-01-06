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

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropySettings;
import me.juancarloscp52.entropy.EntropySettings.UIStyle;
import me.juancarloscp52.entropy.EntropySettings.VotingMode;
import me.juancarloscp52.entropy.client.EntropyClient;
import me.juancarloscp52.entropy.client.Screens.Widgets.EntropySliderWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import org.joml.Matrix3x2fStack;


public class EntropyConfigurationScreen extends Screen {
    private static final Identifier LOGO = Identifier.fromNamespaceAndPath("entropy", "textures/logo-with-text.png");
    private static final double MAX_DURATION = 3300D; //165 seconds + 15 seconds minimum for a total range of 15s-180s
    EntropySettings settings = Entropy.getInstance().settings;

    AbstractSliderButton eventDurationWidget;
    AbstractSliderButton timerDurationWidget;
    Button done;

    Screen parent;

    public EntropyConfigurationScreen(Screen parent) {
        super(Component.translatable("entropy.title"));
        this.parent = parent;
    }

    protected void init() {
        int buttonX = this.width / 2 - 100;
        int buttonWidth = 200;

        eventDurationWidget = new EntropySliderWidget(this.width / 2 - 160, 50, 150, 20,"entropy.options.eventDuration", settings.baseEventDuration / 20.0, 15, 180, value -> Component.literal("" + Mth.floor(value)), value -> settings.baseEventDuration = (short) (value * 20));
        this.addRenderableWidget(eventDurationWidget);

        timerDurationWidget = new EntropySliderWidget(this.width / 2 + 10, 50, 150, 20, "entropy.options.timerDuration", settings.timerDuration / 20.0, 15, 180, value -> Component.literal("" + Mth.floor(value)), value -> settings.timerDuration = (short) (value * 20));
        this.addRenderableWidget(timerDurationWidget);


        Button eventSettings = Button.builder(Component.translatable("entropy.options.disableEvents"), button -> this.minecraft.setScreen(new EntropyEventConfigurationScreen(this))).pos(buttonX, 75).width(buttonWidth).build();
        this.addRenderableWidget(eventSettings);

        Button integrationSettings = Button.builder(Component.translatable("entropy.options.integrations.title"), button -> this.minecraft.setScreen(new EntropyIntegrationsScreen(this))).width(buttonWidth).pos(buttonX, 100).build();
        this.addRenderableWidget(integrationSettings);

        CycleButton<VotingMode> votingModeButton = CycleButton.<VotingMode>builder(votingMode -> votingMode.text, () -> settings.votingMode)
                .withValues(VotingMode.values())
                .withTooltip(votingMode -> Tooltip.create(votingMode.tooltip))
                .create(buttonX, 125, buttonWidth, 20, Component.translatable("entropy.options.votingMode"), (button, newValue) -> settings.votingMode = newValue);
        this.addRenderableWidget(votingModeButton);

        // UI style button
        CycleButton<UIStyle> uiStyleButton = CycleButton.<UIStyle>builder(uiStyle -> uiStyle.text, () -> settings.UIstyle)
                .withValues(UIStyle.values())
                .withTooltip(uiStyle -> Tooltip.create(uiStyle.tooltip))
                .create(buttonX, 150, buttonWidth, 20, Component.translatable("entropy.options.ui"), (button, newValue) -> settings.UIstyle = newValue);
        this.addRenderableWidget(uiStyleButton);

        this.addRenderableWidget(CycleButton.<Boolean>builder(bool -> Component.translatable("entropy.options." + (bool ? "enabled" : "disabled")), () -> settings.accessibilityMode)
                .withValues(true, false)
                .withTooltip(bool -> Tooltip.create(Component.translatable("entropy.options.accessibilityMode.explanation")))
                .create(buttonX, 175, buttonWidth, 20, Component.translatable("entropy.options.accessibilityMode"), (button, newValue) -> settings.accessibilityMode = newValue));

        this.done = Button.builder(CommonComponents.GUI_DONE, button -> onDone()).width(buttonWidth).pos(this.width / 2 - 100, this.height - 30).build();
        this.addRenderableWidget(done);
    }

    public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);

        drawLogo(drawContext);
        Component title = Component.translatable("entropy.options.title");
        drawContext.drawString(this.font, title, this.width / 2 - font.width(title)/2, 10, CommonColors.WHITE);
    }

    public static void drawLogo(final GuiGraphics drawContext) {
        Matrix3x2fStack matrices = drawContext.pose();
        matrices.pushMatrix();
        matrices.scale(0.2f, 0.2f);
        drawContext.blit(RenderPipelines.GUI_TEXTURED, LOGO, 0, 0, 0, 0, 188, 187, 256, 256);
        matrices.popMatrix();
    }

    private void onDone() {
        EntropyClient.getInstance().saveSettings();
        Entropy.getInstance().saveSettings();
        onClose();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
