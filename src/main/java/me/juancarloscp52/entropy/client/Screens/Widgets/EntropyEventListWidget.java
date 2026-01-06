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

package me.juancarloscp52.entropy.client.Screens.Widgets;

import com.google.common.collect.ImmutableList;
import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.EventRegistry;
import me.juancarloscp52.entropy.events.EventType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class EntropyEventListWidget extends ContainerObjectSelectionList<EntropyEventListWidget.ButtonEntry> {
    private final List<EventInfo> eventInfos;
    private final Font textRenderer;

    public EntropyEventListWidget(Minecraft minecraftClient, int width, int height, int x, int y, int itemHeight) {
        super(minecraftClient, width, height, y, itemHeight);
        this.setX(x);
        this.centerListVertically = false;
        this.textRenderer = minecraftClient.font;
        eventInfos = EventRegistry.EVENTS
            .listElements()
            .map(typeReference -> new EventInfo(Component.translatable(typeReference.value().getLanguageKey()).getString(), typeReference))
            .sorted(Comparator.comparing(EventInfo::name))
            .toList();
        getEntries().forEach(this::addEntry);
    }

    public Stream<ButtonEntry> getEntries() {
        return eventInfos.stream().map(eventInfo -> EntropyEventListWidget.ButtonEntry.create(eventInfo, textRenderer));
    }

    @Override
    public int getRowWidth() {
        return 400;
    }

    @Override
    protected int scrollBarX() {
        return super.scrollBarX() + 32;
    }

    public void updateVisibleEntries(String searchText, FilterMode filterMode) {
        String lowerCasedNewText = searchText.toLowerCase(Locale.ROOT);
        Stream<ButtonEntry> buttonEntries = getEntries().filter(filterMode::allowsVisibility);

        if (!searchText.isBlank()) {
            buttonEntries = buttonEntries.filter(buttonEntry -> buttonEntry.eventInfo.name.toLowerCase(Locale.ROOT).contains(lowerCasedNewText) || buttonEntry.eventInfo.typeReference.key().identifier().toString().contains(lowerCasedNewText));
        }

        setScrollAmount(0.0D);
        clearEntries();
        buttonEntries.forEach(this::addEntry);
    }

    @Environment(EnvType.CLIENT)
    public static class ButtonEntry extends ContainerObjectSelectionList.Entry<EntropyEventListWidget.ButtonEntry> {
        private static final Identifier ICON_OVERLAY_LOCATION = Identifier.withDefaultNamespace("world_list/warning_highlighted");
        private static final Tooltip ACCESSIBILITY_TOOLTIP = Tooltip.create(Component.translatable("entropy.options.accessibilityMode.eventDisabled"));
        public final Checkbox checkbox;
        public final EventInfo eventInfo;

        private ButtonEntry(EventInfo eventInfo, Checkbox checkbox) {
            this.checkbox = checkbox;
            this.eventInfo = eventInfo;
        }

        public static EntropyEventListWidget.ButtonEntry create(EventInfo eventInfo, Font textRenderer) {
            Holder.Reference<EventType<?>> typeReference = eventInfo.typeReference;
            EventType<?> type = typeReference.value();
            boolean isEnabled = type.isEnabled();
            boolean enableCheckbox = !isEventDisabledInSettings(typeReference) && isEnabled;
            final Checkbox checkbox = Checkbox.builder(Component.translatable(type.getLanguageKey()), textRenderer).pos(0, 0).selected(enableCheckbox).build();
            if (!isEnabled) {
                checkbox.setTooltip(ACCESSIBILITY_TOOLTIP);
                checkbox.active = false;
            }

            return new EntropyEventListWidget.ButtonEntry(eventInfo, checkbox);
        }

        private static boolean isEventDisabledInSettings(Holder.Reference<EventType<?>> typeReference) {
            return Entropy.getInstance().settings.disabledEventTypes
                .stream()
                .map(ResourceKey::identifier)
                .anyMatch(key -> typeReference.key().identifier().equals(key));
        }

        @Override
        public void renderContent(GuiGraphics drawContext, int mouseX, int mouseY, boolean isHovering, float tickDelta) {
            checkbox.setPosition(getX() + 32, getY());
            checkbox.render(drawContext, mouseX, mouseY, tickDelta);

            if(!eventInfo.typeReference.value().isEnabled()) {
                drawContext.blitSprite(RenderPipelines.GUI_TEXTURED, ICON_OVERLAY_LOCATION, getX(), getY() - 6, 32, 32);

                if(mouseX >= getX() && mouseX <= getX() + 32 && mouseY >= getY() && mouseY <= getY() + getHeight()) {
                    Minecraft minecraft = Minecraft.getInstance();
                    drawContext.setTooltipForNextFrame(minecraft.font, ACCESSIBILITY_TOOLTIP.toCharSequence(minecraft), new MenuTooltipPositioner(checkbox.getRectangle()), mouseX, mouseY, isHovering);
                }
            }
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            if(checkbox.active) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                this.checkbox.onPress(event);
            }

            return true;
        }

        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.checkbox);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(checkbox);
        }
    }

    public static enum FilterMode {
        ALL("entropy.options.all"),
        ENABLED("entropy.options.enabled"),
        DISABLED("entropy.options.disabled");

        public final Component text;

        private FilterMode(String text) {
            this.text = Component.translatable(text);
        }

        public boolean allowsVisibility(ButtonEntry buttonEntry) {
            return this == ALL || this == ENABLED && buttonEntry.checkbox.selected() || this == DISABLED && !buttonEntry.checkbox.selected();
        }
    }

    public record EventInfo(String name, Holder.Reference<EventType<?>> typeReference) {}
}