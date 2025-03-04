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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EntropyIntegrationsScreen extends Screen {
    public static final Logger LOGGER = LogManager.getLogger();
    EntropySettings settings = Entropy.getInstance().settings;
    EntropyIntegrationsSettings integrationsSettings = EntropyClient.getInstance().integrationsSettings;

    Button platformIntegration;
    Button help;
    int platformIntegrationValue = 0;

    EditBox twitchToken;
    EditBox twitchChannel;
    EditBox discordToken;
    EditBox discordChannel;
    EditBox youtubeClientId;
    EditBox youtubeSecret;
    Component youtubeAuthStatus;
    int youtubeAuthState = 0;
    Button youtubeAuth;
    Component tokenTranslatable;
    Component channelTranslatable;
    Checkbox sendChatMessages;
    Checkbox showPollStatus;
    Checkbox showUpcomingEvents;

    Button done;

    private ExecutorService executor;

    Screen parent;

    public EntropyIntegrationsScreen(Screen parent) {
        super(Component.translatable("entropy.options.integrations.title"));
        this.parent = parent;
    }

    protected void init() {
        platformIntegrationValue=integrationsSettings.integrationType;
        platformIntegration = Button.builder(Component.translatable("entropy.options.integrations.integrationSelector", getPlatform()), button -> {
            platformIntegrationValue++;
            if(platformIntegrationValue>=4)
                platformIntegrationValue=0;
            changeContent();
            button.setMessage(Component.translatable("entropy.options.integrations.integrationSelector", getPlatform()));
        }).pos(this.width/2-100,30).width(200).build();
        this.addRenderableWidget(platformIntegration);

        twitchToken = new EditBox(this.font, this.width / 2 + 10, 60, 125, 20, Component.translatable("entropy.options.integrations.twitch.OAuthToken"));
        twitchToken.setMaxLength(64);
        twitchToken.setValue(integrationsSettings.authToken);
        twitchToken.setFormatter((s, integer) -> FormattedCharSequence.forward("*".repeat(s.length()), Style.EMPTY));
        this.addRenderableWidget(twitchToken);
        twitchChannel = new EditBox(this.font, this.width / 2 + 10, 90, 125, 20, Component.translatable("entropy.options.integrations.twitch.channelName"));
        twitchChannel.setValue(integrationsSettings.channel);
        this.addRenderableWidget(twitchChannel);


        discordToken = new EditBox(this.font, this.width / 2 + 10, 60, 125, 20, Component.translatable("entropy.options.integrations.discord.token"));
        discordToken.setMaxLength(128);
        discordToken.setValue(integrationsSettings.discordToken);
        discordToken.setFormatter((s, integer) -> FormattedCharSequence.forward("*".repeat(s.length()), Style.EMPTY));
        this.addRenderableWidget(discordToken);
        discordChannel = new EditBox(this.font, this.width / 2 + 10, 90, 125, 20, Component.translatable("entropy.options.integrations.discord.channelId"));
        discordChannel.setValue(String.valueOf(integrationsSettings.discordChannel));
        discordChannel.setMaxLength(128);
        this.addRenderableWidget(discordChannel);


        youtubeClientId = new EditBox(this.font, this.width / 2 + 10, 60, 125, 20, Component.translatable("entropy.options.integrations.youtube.clientId"));
        youtubeClientId.setMaxLength(128);
        youtubeClientId.setValue(integrationsSettings.youtubeClientId);
        youtubeSecret = new EditBox(this.font, this.width / 2 + 10, 90, 125, 20, Component.translatable("entropy.options.integrations.youtube.secret"));
        this.addRenderableWidget(youtubeClientId);
        youtubeSecret.setMaxLength(64);
        youtubeSecret.setValue(integrationsSettings.youtubeSecret);
        youtubeSecret.setFormatter((s, integer) -> FormattedCharSequence.forward("*".repeat(s.length()), Style.EMPTY));
        this.addRenderableWidget(youtubeSecret);
        youtubeAuthStatus = Component.translatable("entropy.options.integrations.youtube.checkingAccessToken");
        youtubeAuth = Button.builder(Component.translatable("entropy.options.integrations.youtube.authorize"), onPress -> {
            youtubeAuth.setMessage(Component.translatable("entropy.options.integrations.youtube.authorizing"));
            youtubeAuthState = 0;
            youtubeAuthStatus = Component.translatable("entropy.options.integrations.youtube.authorizing");
            changeContent();

            YoutubeApi.authorize(youtubeClientId.getValue(), youtubeSecret.getValue(), (isSuccessful, message) -> {
                youtubeAuth.setMessage(Component.translatable("entropy.options.integrations.youtube.authorize"));

                if(isSuccessful) {
                    // Checking if we can get broadcasts, if not, then we may have exceeded the quota
                    var broadcasts = YoutubeApi.getLiveBroadcasts(integrationsSettings.youtubeAccessToken);
                    if(broadcasts == null) {
                        youtubeAuthStatus = Component.translatable("entropy.options.integrations.youtube.quotaExceeded");
                        youtubeAuthState = 2;
                    }
                    else {
                        youtubeAuthStatus = Component.translatable("entropy.options.integrations.youtube.accessTokenValid");
                        youtubeAuthState = 1;
                    }
                }
                else {
                    youtubeAuthStatus = Component.translatable("entropy.options.integrations.youtube.accessTokenInvalid");
                    youtubeAuthState = 2;
                }
                changeContent();
            });
        }).pos(this.width / 2 - 100, 130).width(200).build();
        this.addRenderableWidget(youtubeAuth);

        executor = Executors.newCachedThreadPool();
        executor.execute(() -> {
            var isAccessTokenValid = YoutubeApi.validateAccessToken(integrationsSettings.youtubeAccessToken);
            if(!isAccessTokenValid)
                isAccessTokenValid = YoutubeApi.refreshAccessToken(youtubeClientId.getValue(), youtubeSecret.getValue(), integrationsSettings.youtubeRefreshToken);

            if(isAccessTokenValid) {
                // Checking if we can get broadcasts, if not, then we may have exceeded the quota
                var broadcasts = YoutubeApi.getLiveBroadcasts(integrationsSettings.youtubeAccessToken);
                if(broadcasts == null) {
                    youtubeAuthStatus = Component.translatable("entropy.options.integrations.youtube.quotaExceeded");
                    youtubeAuthState = 2;
                }
                else {
                    youtubeAuthStatus = Component.translatable("entropy.options.integrations.youtube.accessTokenValid");
                    youtubeAuthState = 1;
                }
            }
            else {
                youtubeAuthStatus = Component.translatable("entropy.options.integrations.youtube.accessTokenInvalid");
                youtubeAuthState = 2;
            }
            changeContent();
        });

        Component showPollStatusText = Component.translatable("entropy.options.integrations.showPollStatus");
        showPollStatus = Checkbox.builder(showPollStatusText, font).pos(this.width / 2 - ((font.width(showPollStatusText))+20), 140).selected(integrationsSettings.showCurrentPercentage).build();
        this.addRenderableWidget(showPollStatus);

        Component showUpcomingEventsText = Component.translatable("entropy.options.integrations.showUpcomingEvents");
        showUpcomingEvents = Checkbox.builder(showUpcomingEventsText, font).pos(this.width / 2 + (20), 140).selected(integrationsSettings.showUpcomingEvents).build();
        this.addRenderableWidget(showUpcomingEvents);

        Component sendChatMessagesText = Component.translatable("entropy.options.integrations.twitch.sendChatFeedBack");
        sendChatMessages = Checkbox.builder(sendChatMessagesText, font).pos(this.width / 2 - ((font.width(sendChatMessagesText) / 2) + 11), 145).selected(integrationsSettings.sendChatMessages).build();
        this.addRenderableWidget(sendChatMessages);

        this.done = Button.builder(CommonComponents.GUI_DONE, button -> onDone()).pos(this.width / 2 - 100, this.height - 30).width(200).build();
        this.addRenderableWidget(done);
        this.addRenderableWidget(
                help = Button.builder(Component.translatable("entropy.options.questionMark"), (button -> {
                })).pos((this.width / 2) + 110, 30).width(20).tooltip(Tooltip.create(
                        Component.translatable(
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
                tokenTranslatable = Component.translatable("entropy.options.integrations.twitch.OAuthToken");
                channelTranslatable = Component.translatable("entropy.options.integrations.twitch.channelName");
                showPollStatus.setY(140);
                showUpcomingEvents.setY(140);
                sendChatMessages.setY(165);
                sendChatMessages.setMessage(Component.translatable("entropy.options.integrations.twitch.sendChatFeedBack"));
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
                tokenTranslatable = Component.translatable("entropy.options.integrations.discord.token");
                channelTranslatable = Component.translatable("entropy.options.integrations.discord.channelId");
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
                tokenTranslatable = Component.translatable("entropy.options.integrations.youtube.clientId");
                channelTranslatable = Component.translatable("entropy.options.integrations.youtube.secret");
                showPollStatus.setY(160);
                showUpcomingEvents.setY(160);
                sendChatMessages.setY(185);
                sendChatMessages.setMessage(Component.translatable("entropy.options.integrations.youtube.sendChatFeedBack"));
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
                tokenTranslatable = Component.translatable("");
                channelTranslatable = Component.translatable("");
                showPollStatus.setY(140);
                showUpcomingEvents.setY(140);
                sendChatMessages.setY(165);
            }
        }
        help.setTooltip(Tooltip.create(
                Component.translatable(
                    switch(platformIntegrationValue) {
                        case 1 -> "entropy.options.integrations.twitch.help";
                        case 2 -> "entropy.options.integrations.discord.help";
                        default -> "entropy.options.integrations.youtube.help"; })));
    }

    public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        EntropyConfigurationScreen.drawLogo(drawContext);
        RenderSystem.disableBlend();
        if(platformIntegrationValue !=0){
            drawContext.drawString(this.font, tokenTranslatable, this.width / 2 - 10 - font.width(tokenTranslatable), 66, 16777215);
            drawContext.drawString(this.font, channelTranslatable, this.width / 2 - 10 - font.width(channelTranslatable), 96, 16777215);
        }
        if(platformIntegrationValue == 3) {
            var color = youtubeAuthState == 0 ? 0xFFAA00 : youtubeAuthState == 1 ? 0x00AA00 : 0xAA0000;
            drawContext.drawString(this.font, youtubeAuthStatus, this.width / 2 - font.width(youtubeAuthStatus) / 2, 116, color);
        }
    }

    private void onDone() {
        YoutubeApi.stopHttpServer();

        settings.integrations = platformIntegrationValue > 0;
        integrationsSettings.integrationType = platformIntegrationValue;
        integrationsSettings.authToken = twitchToken.getValue();
        integrationsSettings.channel = twitchChannel.getValue();
        integrationsSettings.youtubeClientId = youtubeClientId.getValue();
        integrationsSettings.youtubeSecret = youtubeSecret.getValue();
        integrationsSettings.discordChannel = Long.parseLong(discordChannel.getValue());
        integrationsSettings.discordToken = discordToken.getValue();

        integrationsSettings.sendChatMessages = sendChatMessages.selected();
        integrationsSettings.showCurrentPercentage = showPollStatus.selected();
        integrationsSettings.showUpcomingEvents = showUpcomingEvents.selected();

        EntropyClient.getInstance().saveSettings();
        Entropy.getInstance().saveSettings();
        onClose();
    }

    private String getPlatform(){
        return switch (platformIntegrationValue) {
            case 1 -> I18n.get("entropy.options.integrations.twitch");
            case 2 -> I18n.get("entropy.options.integrations.discord");
            case 3 -> I18n.get("entropy.options.integrations.youtube");
            default -> I18n.get("entropy.options.off");
        };
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
