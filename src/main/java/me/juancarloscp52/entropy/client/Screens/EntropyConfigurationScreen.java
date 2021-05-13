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
import me.juancarloscp52.entropy.client.EntropyIntegrationsSettings;
import me.juancarloscp52.entropy.client.Screens.Widgets.EntropySliderWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;


public class EntropyConfigurationScreen extends Screen {
    private static final Identifier LOGO = new Identifier("entropy", "textures/logo-with-text.png");
    EntropySettings settings = Entropy.getInstance().settings;
    EntropyIntegrationsSettings integrationsSettings = EntropyClient.getInstance().integrationsSettings;

    TextFieldWidget authToken;
    TextFieldWidget channelName;
    CheckboxWidget integrationsCheckbox;
    CheckboxWidget sendChatMessages;
    CheckboxWidget showPollStatus;

    SliderWidget eventDurationWidget;
    SliderWidget timerDurationWidget;
    ButtonWidget done;

    Screen parent;

    public EntropyConfigurationScreen(Screen parent) {
        super(new TranslatableText("entropy.title"));
        this.parent = parent;
    }

    protected void init() {

        eventDurationWidget = new EntropySliderWidget(this.width / 2 - 160, 5, 150, 20,"entropy.options.eventDuration",(settings.baseEventDuration-300)/1200d,(slider, translationKey, value) -> new TranslatableText("entropy.options.eventDuration", MathHelper.floor(value*60+15)), value -> settings.baseEventDuration = (short) ((1200*value)+300));
        this.addButton(eventDurationWidget);


        timerDurationWidget = new EntropySliderWidget(this.width / 2 + 10, 5, 150, 20, "entropy.options.timerDuration", (settings.timerDuration-300)/1200d,(slider, translationKey, value) -> new TranslatableText("entropy.options.timerDuration", MathHelper.floor(value*60+15)),value -> settings.timerDuration = (short) ((1200*value)+300));
        this.addButton(timerDurationWidget);

        ButtonWidget eventSettings = new ButtonWidget(this.width / 2 - 75, 30, 150, 20, new TranslatableText("entropy.options.disableEvents"), button -> this.client.openScreen(new EntropyEventConfigurationScreen(this)));
        this.addButton(eventSettings);

        TranslatableText twitchIntegrationsTranslatable = new TranslatableText("entropy.options.twitchIntegrations");
        integrationsCheckbox = new CheckboxWidget(this.width / 2 - ((textRenderer.getWidth(twitchIntegrationsTranslatable) / 2) + 22), 55, 150, 20, twitchIntegrationsTranslatable, settings.integrations);
        this.addButton(integrationsCheckbox);


        authToken = new TextFieldWidget(this.textRenderer, this.width / 2 + 10, 80, 125, 20, new TranslatableText("entropy.options.twitchIntegrations.OAuthToken"));
        authToken.setMaxLength(64);
        authToken.setText(integrationsSettings.authToken);
        authToken.setRenderTextProvider((s, integer) -> {
            StringBuilder hiddenString = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                hiddenString.append("*");
            }
            return OrderedText.styledString(hiddenString.toString(), Style.EMPTY);
        });
        this.addButton(authToken);


        channelName = new TextFieldWidget(this.textRenderer, this.width / 2 + 10, 110, 125, 20, new TranslatableText("entropy.options.twitchIntegrations.channelName"));
        channelName.setText(integrationsSettings.channel);
        this.addButton(channelName);


        Text showPollStatusText = new TranslatableText("entropy.options.twitchIntegrations.showPollStatus");
        showPollStatus = new CheckboxWidget(this.width / 2 - ((textRenderer.getWidth(showPollStatusText) / 2) + 11), 140, 150, 20, showPollStatusText, integrationsSettings.showCurrentPercentage);
        this.addButton(showPollStatus);

        Text sendChatMessagesText = new TranslatableText("entropy.options.twitchIntegrations.sendChatFeedBack");
        sendChatMessages = new CheckboxWidget(this.width / 2 - ((textRenderer.getWidth(sendChatMessagesText) / 2) + 11), 165, 150, 20, sendChatMessagesText, integrationsSettings.sendChatMessages);
        this.addButton(sendChatMessages);


        this.done = new ButtonWidget(this.width / 2 - 100, this.height - 30, 200, 20, ScreenTexts.DONE, button -> onDone());
        this.addButton(done);

        this.addButton(
                new ButtonWidget(this.width / 2 + ((textRenderer.getWidth(twitchIntegrationsTranslatable) / 2) + 6), 55, 20, 20, new TranslatableText("entropy.options.questionMark"), (button -> {
                }), (buttonWidget, matrixStack, i, j) ->
                        this.renderOrderedTooltip(matrixStack, textRenderer.wrapLines(new TranslatableText("entropy.options.twitchIntegrations.help"), this.width / 2), i, j)));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        matrices.push();
        matrices.translate(5, 0, 0);
        matrices.scale(0.2f, 0.2f, 0.2f);
        client.getTextureManager().bindTexture(LOGO);
        this.drawTexture(matrices, 0, 0, 0, 0, 188, 187);
        matrices.pop();
        RenderSystem.disableBlend();

        TranslatableText oAuthTranslation = new TranslatableText("entropy.options.twitchIntegrations.OAuthToken");
        drawTextWithShadow(matrices, this.textRenderer, oAuthTranslation, this.width / 2 - 10 - textRenderer.getWidth(oAuthTranslation), 86, 16777215);

        TranslatableText channelName = new TranslatableText("entropy.options.twitchIntegrations.channelName");
        drawTextWithShadow(matrices, this.textRenderer, channelName, this.width / 2 - 10 - textRenderer.getWidth(channelName), 116, 16777215);

        super.render(matrices, mouseX, mouseY, delta);
    }

    private void onDone() {
        settings.integrations = integrationsCheckbox.isChecked();
        integrationsSettings.authToken = authToken.getText();
        integrationsSettings.channel = channelName.getText();
        integrationsSettings.sendChatMessages = sendChatMessages.isChecked();
        integrationsSettings.showCurrentPercentage = showPollStatus.isChecked();

        EntropyClient.getInstance().saveSettings();
        Entropy.getInstance().saveSettings();
        onClose();
    }

    @Override
    public void onClose() {
        this.client.openScreen(this.parent);
    }
}
