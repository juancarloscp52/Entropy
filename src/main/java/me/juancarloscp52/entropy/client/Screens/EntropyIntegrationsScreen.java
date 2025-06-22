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
import me.juancarloscp52.entropy.client.EntropyClient;
import me.juancarloscp52.entropy.client.EntropyIntegrationsSettings;
import me.juancarloscp52.entropy.client.integrations.youtube.YoutubeApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.CommonLayouts;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
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
    EntropyIntegrationsSettings settings = EntropyClient.getInstance().integrationsSettings;

    private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
    private TabNavigationBar tabNavigationBar;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    private GeneralTab general;
    private TwitchTab twitch;
    private DiscordTab discord;
    private YouTubeTab youtube;

    Screen parent;

    public EntropyIntegrationsScreen(Screen parent) {
        super(Component.translatable("entropy.options.integrations.title"));
        this.parent = parent;
    }

    protected void init() {
        general = new GeneralTab();
        twitch = new TwitchTab();
        discord = new DiscordTab();
        youtube = new YouTubeTab();
        tabNavigationBar = TabNavigationBar.builder(this.tabManager, this.width).addTabs(general, twitch, discord, youtube).build();
        addRenderableWidget(tabNavigationBar);

        LinearLayout linearLayout = layout.addToFooter(LinearLayout.horizontal().spacing(8));
        linearLayout.addChild(Button.builder(CommonComponents.GUI_DONE, (button) -> onDone()).build());
        linearLayout.addChild(Button.builder(CommonComponents.GUI_CANCEL, (button) -> onClose()).build());

        layout.visitWidgets((abstractWidget) -> {
            abstractWidget.setTabOrderGroup(1);
            addRenderableWidget(abstractWidget);
        });

        tabNavigationBar.selectTab(0, false);
        repositionElements();
    }

    public void repositionElements() {
        if (tabNavigationBar != null) {
            tabNavigationBar.setWidth(width);
            tabNavigationBar.arrangeElements();
            final int bottom = tabNavigationBar.getRectangle().bottom();
            ScreenRectangle screenRectangle = new ScreenRectangle(0, bottom, width, height - layout.getFooterHeight() - bottom);
            tabManager.setTabArea(screenRectangle);
            layout.setHeaderHeight(bottom);
            layout.arrangeElements();
        }
    }

    private void onDone() {
        YoutubeApi.stopHttpServer();

        settings.twitch.enabled = twitch.enabled.selected();
        settings.twitch.token = twitch.token.getValue();
        settings.twitch.channel = twitch.channel.getValue();

        settings.discord.enabled = discord.enabled.selected();
        settings.discord.token = discord.token.getValue();
        settings.discord.channel = Long.parseLong(discord.channel.getValue());

        settings.youtube.enabled = youtube.enabled.selected();
        settings.youtube.clientId = youtube.clientId.getValue();
        settings.youtube.secret = youtube.secret.getValue();

        settings.sendChatMessages = general.sendChatMessages.selected();
        settings.showCurrentPercentage = general.showPollStatus.selected();
        settings.showUpcomingEvents = general.showUpcomingEvents.selected();

        EntropyClient.getInstance().saveSettings();

        Entropy.getInstance().settings.integrations = settings.twitch.enabled || settings.discord.enabled || settings.youtube.enabled;
        Entropy.getInstance().saveSettings();

        onClose();
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }

    @Environment(EnvType.CLIENT)
    class GeneralTab extends GridLayoutTab {
        private final Checkbox sendChatMessages;
        private final Checkbox showPollStatus;
        private final Checkbox showUpcomingEvents;

        public GeneralTab() {
            super(Component.translatable("entropy.options.integrations.general"));

            GridLayout.RowHelper rowHelper = layout.columnSpacing(10).rowSpacing(8).createRowHelper(1);

            showPollStatus = Checkbox.builder(Component.translatable("entropy.options.integrations.showPollStatus"), font)
                .selected(settings.showCurrentPercentage)
                .build();
            rowHelper.addChild(showPollStatus);

            showUpcomingEvents = Checkbox.builder(Component.translatable("entropy.options.integrations.showUpcomingEvents"), font)
                .selected(settings.showUpcomingEvents)
                .build();
            rowHelper.addChild(showUpcomingEvents);

            sendChatMessages = Checkbox.builder(Component.translatable("entropy.options.integrations.sendChatFeedBack"), font)
                .selected(settings.sendChatMessages)
                .build();
            rowHelper.addChild(sendChatMessages);
        }
    }

    @Environment(EnvType.CLIENT)
    class TwitchTab extends GridLayoutTab {
        private final Checkbox enabled;
        private final EditBox token;
        private final EditBox channel;

        public TwitchTab() {
            super(Component.translatable("entropy.options.integrations.twitch"));

            GridLayout.RowHelper rowHelper = layout.columnSpacing(10).rowSpacing(8).createRowHelper(1);

            enabled = Checkbox.builder(Component.translatable("entropy.options.enabled"), font)
                .tooltip(Tooltip.create(Component.translatable("entropy.options.integrations.twitch.help")))
                .selected(settings.twitch.enabled)
                .build();
            rowHelper.addChild(enabled);

            token = new EditBox(font, 200, 20, Component.translatable("entropy.options.integrations.twitch.OAuthToken"));
            token.setMaxLength(64);
            token.setValue(settings.twitch.token);
            token.setFormatter((s, integer) -> FormattedCharSequence.forward("*".repeat(s.length()), Style.EMPTY));
            rowHelper.addChild(
                CommonLayouts.labeledElement(font, token, Component.translatable("entropy.options.integrations.twitch.OAuthToken")),
                rowHelper.newCellSettings().alignHorizontallyCenter()
            );

            channel = new EditBox(font, 200, 20, Component.translatable("entropy.options.integrations.twitch.channelName"));
            channel.setValue(settings.twitch.channel);
            rowHelper.addChild(
                CommonLayouts.labeledElement(font, channel, Component.translatable("entropy.options.integrations.twitch.channelName")),
                rowHelper.newCellSettings().alignHorizontallyCenter()
            );
        }
    }

    @Environment(EnvType.CLIENT)
    class DiscordTab extends GridLayoutTab {
        private final Checkbox enabled;
        private final EditBox token;
        private final EditBox channel;

        public DiscordTab() {
            super(Component.translatable("entropy.options.integrations.discord"));

            GridLayout.RowHelper rowHelper = layout.columnSpacing(10).rowSpacing(8).createRowHelper(1);

            enabled = Checkbox.builder(Component.translatable("entropy.options.enabled"), font)
                .tooltip(Tooltip.create(Component.translatable("entropy.options.integrations.discord.help")))
                .selected(settings.discord.enabled)
                .build();
            rowHelper.addChild(enabled);

            token = new EditBox(font, 200, 20, Component.translatable("entropy.options.integrations.discord.token"));
            token.setMaxLength(128);
            token.setValue(settings.discord.token);
            token.setFormatter((s, integer) -> FormattedCharSequence.forward("*".repeat(s.length()), Style.EMPTY));
            rowHelper.addChild(
                CommonLayouts.labeledElement(font, token, Component.translatable("entropy.options.integrations.discord.token")),
                rowHelper.newCellSettings().alignHorizontallyCenter()
            );
            channel = new EditBox(font, 200, 20, Component.translatable("entropy.options.integrations.discord.channelId"));
            channel.setValue(String.valueOf(settings.discord.channel));
            channel.setMaxLength(128);
            rowHelper.addChild(
                CommonLayouts.labeledElement(font, channel, Component.translatable("entropy.options.integrations.discord.channelId")),
                rowHelper.newCellSettings().alignHorizontallyCenter()
            );
        }
    }

    @Environment(EnvType.CLIENT)
    class YouTubeTab extends GridLayoutTab {
        private final Checkbox enabled;
        private final EditBox clientId;
        private final EditBox secret;
        private final StringWidget authStatus;
        private Button youtubeAuth = null;
        private final ExecutorService executor;

        public YouTubeTab() {
            super(Component.translatable("entropy.options.integrations.youtube"));

            GridLayout.RowHelper rowHelper = layout.columnSpacing(10).rowSpacing(4).createRowHelper(1);

            enabled = Checkbox.builder(Component.translatable("entropy.options.enabled"), font)
                .tooltip(Tooltip.create(Component.translatable("entropy.options.integrations.youtube.help")))
                .selected(settings.youtube.enabled)
                .build();
            rowHelper.addChild(enabled);

            clientId = new EditBox(font, 200, 20, Component.translatable("entropy.options.integrations.youtube.clientId"));
            clientId.setMaxLength(128);
            clientId.setValue(settings.youtube.clientId);
            rowHelper.addChild(
                CommonLayouts.labeledElement(font, clientId, Component.translatable("entropy.options.integrations.youtube.clientId")),
                rowHelper.newCellSettings().alignHorizontallyCenter()
            );

            secret = new EditBox(font, 200, 20, Component.translatable("entropy.options.integrations.youtube.secret"));
            secret.setMaxLength(64);
            secret.setValue(settings.youtube.secret);
            secret.setFormatter((s, integer) -> FormattedCharSequence.forward("*".repeat(s.length()), Style.EMPTY));
            rowHelper.addChild(
                CommonLayouts.labeledElement(font, secret, Component.translatable("entropy.options.integrations.youtube.secret")),
                rowHelper.newCellSettings().alignHorizontallyCenter()
            );

            authStatus = new StringWidget(200, 20, Component.translatable("entropy.options.integrations.youtube.checkingAccessToken").withStyle(ChatFormatting.GOLD), font);
            rowHelper.addChild(authStatus);

            youtubeAuth = Button.builder(Component.translatable("entropy.options.integrations.youtube.authorize"), onPress -> {
                youtubeAuth.setMessage(Component.translatable("entropy.options.integrations.youtube.authorizing"));
                authStatus.setMessage(Component.translatable("entropy.options.integrations.youtube.authorizing"));

                YoutubeApi.authorize(clientId.getValue(), secret.getValue(), (isSuccessful, message) -> {
                    youtubeAuth.setMessage(Component.translatable("entropy.options.integrations.youtube.authorize"));

                    if (isSuccessful) {
                        // Checking if we can get broadcasts, if not, then we may have exceeded the quota
                        var broadcasts = YoutubeApi.getLiveBroadcasts(settings.youtube.accessToken);
                        if (broadcasts == null) {
                            authStatus.setMessage(Component.translatable("entropy.options.integrations.youtube.quotaExceeded").withStyle(ChatFormatting.DARK_RED));
                        }
                        else {
                            authStatus.setMessage(Component.translatable("entropy.options.integrations.youtube.accessTokenValid").withStyle(ChatFormatting.DARK_GREEN));
                        }
                    }
                    else {
                        authStatus.setMessage(Component.translatable("entropy.options.integrations.youtube.accessTokenInvalid").withStyle(ChatFormatting.DARK_RED));
                    }
                });
            }).width(200).build();
            rowHelper.addChild(youtubeAuth);

            executor = Executors.newCachedThreadPool();
            executor.execute(() -> {
                boolean isAccessTokenValid = YoutubeApi.validateAccessToken(settings.youtube.accessToken);
                if (!isAccessTokenValid)
                    isAccessTokenValid = YoutubeApi.refreshAccessToken(clientId.getValue(), secret.getValue(), settings.youtube.refreshToken);

                if (isAccessTokenValid) {
                    // Checking if we can get broadcasts, if not, then we may have exceeded the quota
                    var broadcasts = YoutubeApi.getLiveBroadcasts(settings.youtube.accessToken);
                    if (broadcasts == null) {
                        authStatus.setMessage(Component.translatable("entropy.options.integrations.youtube.quotaExceeded").withStyle(ChatFormatting.DARK_RED));
                    }
                    else {
                        authStatus.setMessage(Component.translatable("entropy.options.integrations.youtube.accessTokenValid").withStyle(ChatFormatting.DARK_GREEN));
                    }
                }
                else {
                    authStatus.setMessage(Component.translatable("entropy.options.integrations.youtube.accessTokenInvalid").withStyle(ChatFormatting.DARK_RED));
                }
            });
        }
    }
}
