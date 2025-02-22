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
import me.juancarloscp52.entropy.client.integrations.youtube.YoutubeApi;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EntropyIntegrationsScreen extends Screen {
    public static final Logger LOGGER = LogManager.getLogger();
    private static final Identifier LOGO = new Identifier("entropy", "textures/logo-with-text.png");
    EntropySettings settings = Entropy.getInstance().settings;
    EntropyIntegrationsSettings integrationsSettings = EntropyClient.getInstance().integrationsSettings;

    ButtonWidget platformIntegration;
    ButtonWidget help;
    int platformIntegrationValue = 0;

    TextFieldWidget twitchToken;
    TextFieldWidget twitchChannel;
    TextFieldWidget discordToken;
    TextFieldWidget discordChannel;
    TextFieldWidget youtubeClientId;
    TextFieldWidget youtubeSecret;
    Text youtubeAuthStatus;
    int youtubeAuthState = 0;
    ButtonWidget youtubeAuth;
    Text tokenTranslatable;
    Text channelTranslatable;
    CheckboxWidget sendChatMessages;
    CheckboxWidget showPollStatus;
    CheckboxWidget showUpcomingEvents;

    ButtonWidget done;

    private ExecutorService executor;

    Screen parent;

    public EntropyIntegrationsScreen(Screen parent) {
        super(Text.translatable("entropy.options.integrations.title"));
        this.parent = parent;
    }

    protected void init() {
        platformIntegrationValue=integrationsSettings.integrationType;
        platformIntegration = ButtonWidget.builder(Text.translatable("entropy.options.integrations.integrationSelector", getPlatform()), button -> {
            platformIntegrationValue++;
            if(platformIntegrationValue>=4)
                platformIntegrationValue=0;
            changeContent();
            button.setMessage(Text.translatable("entropy.options.integrations.integrationSelector", getPlatform()));
        }).position(this.width/2-100,30).width(200).build();
        this.addDrawableChild(platformIntegration);

        twitchToken = new TextFieldWidget(this.textRenderer, this.width / 2 + 10, 60, 125, 20, Text.translatable("entropy.options.integrations.twitch.OAuthToken"));
        twitchToken.setMaxLength(64);
        twitchToken.setText(integrationsSettings.authToken);
        twitchToken.setRenderTextProvider((s, integer) -> OrderedText.styledForwardsVisitedString("*".repeat(s.length()), Style.EMPTY));
        this.addDrawableChild(twitchToken);
        twitchChannel = new TextFieldWidget(this.textRenderer, this.width / 2 + 10, 90, 125, 20, Text.translatable("entropy.options.integrations.twitch.channelName"));
        twitchChannel.setText(integrationsSettings.channel);
        this.addDrawableChild(twitchChannel);


        discordToken = new TextFieldWidget(this.textRenderer, this.width / 2 + 10, 60, 125, 20, Text.translatable("entropy.options.integrations.discord.token"));
        discordToken.setMaxLength(128);
        discordToken.setText(integrationsSettings.discordToken);
        discordToken.setRenderTextProvider((s, integer) -> OrderedText.styledForwardsVisitedString("*".repeat(s.length()), Style.EMPTY));
        this.addDrawableChild(discordToken);
        discordChannel = new TextFieldWidget(this.textRenderer, this.width / 2 + 10, 90, 125, 20, Text.translatable("entropy.options.integrations.discord.channelId"));
        discordChannel.setText(String.valueOf(integrationsSettings.discordChannel));
        discordChannel.setMaxLength(128);
        this.addDrawableChild(discordChannel);


        youtubeClientId = new TextFieldWidget(this.textRenderer, this.width / 2 + 10, 60, 125, 20, Text.translatable("entropy.options.integrations.youtube.clientId"));
        youtubeClientId.setMaxLength(128);
        youtubeClientId.setText(integrationsSettings.youtubeClientId);
        youtubeSecret = new TextFieldWidget(this.textRenderer, this.width / 2 + 10, 90, 125, 20, Text.translatable("entropy.options.integrations.youtube.secret"));
        this.addDrawableChild(youtubeClientId);
        youtubeSecret.setMaxLength(64);
        youtubeSecret.setText(integrationsSettings.youtubeSecret);
        youtubeSecret.setRenderTextProvider((s, integer) -> OrderedText.styledForwardsVisitedString("*".repeat(s.length()), Style.EMPTY));
        this.addDrawableChild(youtubeSecret);
        youtubeAuthStatus = Text.translatable("entropy.options.integrations.youtube.checkingAccessToken");
        youtubeAuth = ButtonWidget.builder(Text.translatable("entropy.options.integrations.youtube.authorize"), onPress -> {
            youtubeAuth.setMessage(Text.translatable("entropy.options.integrations.youtube.authorizing"));
            youtubeAuthState = 0;
            youtubeAuthStatus = Text.translatable("entropy.options.integrations.youtube.authorizing");
            changeContent();

            YoutubeApi.authorize(youtubeClientId.getText(), youtubeSecret.getText(), (isSuccessful, message) -> {
                youtubeAuth.setMessage(Text.translatable("entropy.options.integrations.youtube.authorize"));

                if(isSuccessful) {
                    // Checking if we can get broadcasts, if not, then we may have exceeded the quota
                    var broadcasts = YoutubeApi.getLiveBroadcasts(integrationsSettings.youtubeAccessToken);
                    if(broadcasts == null) {
                        youtubeAuthStatus = Text.translatable("entropy.options.integrations.youtube.quotaExceeded");
                        youtubeAuthState = 2;
                    }
                    else {
                        youtubeAuthStatus = Text.translatable("entropy.options.integrations.youtube.accessTokenValid");
                        youtubeAuthState = 1;
                    }
                }
                else {
                    youtubeAuthStatus = Text.translatable("entropy.options.integrations.youtube.accessTokenInvalid");
                    youtubeAuthState = 2;
                }
                changeContent();
            });
        }).position(this.width / 2 - 100, 130).width(200).build();
        this.addDrawableChild(youtubeAuth);

        executor = Executors.newCachedThreadPool();
        executor.execute(() -> {
            var isAccessTokenValid = YoutubeApi.validateAccessToken(integrationsSettings.youtubeAccessToken);
            if(!isAccessTokenValid)
                isAccessTokenValid = YoutubeApi.refreshAccessToken(youtubeClientId.getText(), youtubeSecret.getText(), integrationsSettings.youtubeRefreshToken);

            if(isAccessTokenValid) {
                // Checking if we can get broadcasts, if not, then we may have exceeded the quota
                var broadcasts = YoutubeApi.getLiveBroadcasts(integrationsSettings.youtubeAccessToken);
                if(broadcasts == null) {
                    youtubeAuthStatus = Text.translatable("entropy.options.integrations.youtube.quotaExceeded");
                    youtubeAuthState = 2;
                }
                else {
                    youtubeAuthStatus = Text.translatable("entropy.options.integrations.youtube.accessTokenValid");
                    youtubeAuthState = 1;
                }
            }
            else {
                youtubeAuthStatus = Text.translatable("entropy.options.integrations.youtube.accessTokenInvalid");
                youtubeAuthState = 2;
            }
            changeContent();
        });

        Text showPollStatusText = Text.translatable("entropy.options.integrations.showPollStatus");
        showPollStatus = new CheckboxWidget(this.width / 2 - ((textRenderer.getWidth(showPollStatusText))+20), 140, 150, 20, showPollStatusText, integrationsSettings.showCurrentPercentage);
        this.addDrawableChild(showPollStatus);

        Text showUpcomingEventsText = Text.translatable("entropy.options.integrations.showUpcomingEvents");
        showUpcomingEvents = new CheckboxWidget(this.width / 2 + (20), 140, 150, 20, showUpcomingEventsText, integrationsSettings.showUpcomingEvents);
        this.addDrawableChild(showUpcomingEvents);

        Text sendChatMessagesText = Text.translatable("entropy.options.integrations.twitch.sendChatFeedBack");
        sendChatMessages = new CheckboxWidget(this.width / 2 - ((textRenderer.getWidth(sendChatMessagesText) / 2) + 11), 145, 150, 20, sendChatMessagesText, integrationsSettings.sendChatMessages);
        this.addDrawableChild(sendChatMessages);


        this.done = ButtonWidget.builder(ScreenTexts.DONE, button -> onDone()).position(this.width / 2 - 100, this.height - 30).width(200).build();
        this.addDrawableChild(done);
        this.addDrawableChild(
                help = ButtonWidget.builder(Text.translatable("entropy.options.questionMark"), (button -> {
                })).position((this.width / 2) + 110, 30).width(20).tooltip(Tooltip.of(
                        Text.translatable(
                            switch(platformIntegrationValue) {
                                case 1 -> "entropy.options.integrations.twitch.help";
                                case 2 -> "entropy.options.integrations.discord.help";
                                default -> "entropy.options.integrations.youtube.help"; }))).build());

        changeContent();
    }

    public void changeContent(){
        switch (platformIntegrationValue) {
            case 1 -> {
                twitchChannel.setVisible(true);
                twitchToken.setVisible(true);
                sendChatMessages.visible = true;
                showPollStatus.visible = true;
                showUpcomingEvents.visible = true;
                discordChannel.setVisible(false);
                discordToken.setVisible(false);
                youtubeClientId.setVisible(false);
                youtubeSecret.setVisible(false);
                youtubeAuth.visible = false;
                help.visible=true;
                tokenTranslatable = Text.translatable("entropy.options.integrations.twitch.OAuthToken");
                channelTranslatable = Text.translatable("entropy.options.integrations.twitch.channelName");
                showPollStatus.setY(140);
                showUpcomingEvents.setY(140);
                sendChatMessages.setY(165);
                sendChatMessages.setMessage(Text.translatable("entropy.options.integrations.twitch.sendChatFeedBack"));
            }
            case 2 -> {
                twitchChannel.setVisible(false);
                twitchToken.setVisible(false);
                sendChatMessages.visible = false;
                showPollStatus.visible = true;
                showUpcomingEvents.visible = true;
                discordChannel.setVisible(true);
                discordToken.setVisible(true);
                youtubeClientId.setVisible(false);
                youtubeSecret.setVisible(false);
                youtubeAuth.visible = false;
                help.visible=true;
                tokenTranslatable = Text.translatable("entropy.options.integrations.discord.token");
                channelTranslatable = Text.translatable("entropy.options.integrations.discord.channelId");
                showPollStatus.setY(140);
                showUpcomingEvents.setY(140);
                sendChatMessages.setY(165);
            }
            case 3 -> {
                twitchChannel.setVisible(false);
                twitchToken.setVisible(false);
                sendChatMessages.visible = true;
                showPollStatus.visible = true;
                showUpcomingEvents.visible = true;
                discordChannel.setVisible(false);
                discordToken.setVisible(false);
                youtubeClientId.setVisible(true);
                youtubeSecret.setVisible(true);
                youtubeAuth.visible = true;
                help.visible=true;
                tokenTranslatable = Text.translatable("entropy.options.integrations.youtube.clientId");
                channelTranslatable = Text.translatable("entropy.options.integrations.youtube.secret");
                showPollStatus.setY(160);
                showUpcomingEvents.setY(160);
                sendChatMessages.setY(185);
                sendChatMessages.setMessage(Text.translatable("entropy.options.integrations.youtube.sendChatFeedBack"));
            }
            default -> {
                twitchChannel.setVisible(false);
                twitchToken.setVisible(false);
                sendChatMessages.visible = false;
                showPollStatus.visible = false;
                showUpcomingEvents.visible = false;
                discordChannel.setVisible(false);
                discordToken.setVisible(false);
                youtubeClientId.setVisible(false);
                youtubeSecret.setVisible(false);
                youtubeAuth.visible = false;
                help.visible=false;
                tokenTranslatable = Text.translatable("");
                channelTranslatable = Text.translatable("");
                showPollStatus.setY(140);
                showUpcomingEvents.setY(140);
                sendChatMessages.setY(165);
            }
        }
        help.setTooltip(Tooltip.of(
                Text.translatable(
                    switch(platformIntegrationValue) {
                        case 1 -> "entropy.options.integrations.twitch.help";
                        case 2 -> "entropy.options.integrations.discord.help";
                        default -> "entropy.options.integrations.youtube.help"; })));
    }

    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        EntropyConfigurationScreen.drawLogo(drawContext);
        RenderSystem.disableBlend();
        if(platformIntegrationValue !=0){
            drawContext.drawTextWithShadow(this.textRenderer, tokenTranslatable, this.width / 2 - 10 - textRenderer.getWidth(tokenTranslatable), 66, 16777215);
            drawContext.drawTextWithShadow(this.textRenderer, channelTranslatable, this.width / 2 - 10 - textRenderer.getWidth(channelTranslatable), 96, 16777215);
        }
        if(platformIntegrationValue == 3) {
            var color = youtubeAuthState == 0 ? 0xFFAA00 : youtubeAuthState == 1 ? 0x00AA00 : 0xAA0000;
            drawContext.drawTextWithShadow(this.textRenderer, youtubeAuthStatus, this.width / 2 - textRenderer.getWidth(youtubeAuthStatus) / 2, 116, color);
        }
    }

    private void onDone() {
        YoutubeApi.stopHttpServer();

        settings.integrations = platformIntegrationValue > 0;
        integrationsSettings.integrationType = platformIntegrationValue;
        integrationsSettings.authToken = twitchToken.getText();
        integrationsSettings.channel = twitchChannel.getText();
        integrationsSettings.youtubeClientId = youtubeClientId.getText();
        integrationsSettings.youtubeSecret = youtubeSecret.getText();
        integrationsSettings.discordChannel = Long.parseLong(discordChannel.getText());
        integrationsSettings.discordToken = discordToken.getText();

        integrationsSettings.sendChatMessages = sendChatMessages.isChecked();
        integrationsSettings.showCurrentPercentage = showPollStatus.isChecked();
        integrationsSettings.showUpcomingEvents = showUpcomingEvents.isChecked();

        EntropyClient.getInstance().saveSettings();
        Entropy.getInstance().saveSettings();
        close();
    }

    private String getPlatform(){
        return switch (platformIntegrationValue) {
            case 1 -> I18n.translate("entropy.options.integrations.twitch");
            case 2 -> I18n.translate("entropy.options.integrations.discord");
            case 3 -> I18n.translate("entropy.options.integrations.youtube");
            default -> I18n.translate("entropy.options.off");
        };
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}
