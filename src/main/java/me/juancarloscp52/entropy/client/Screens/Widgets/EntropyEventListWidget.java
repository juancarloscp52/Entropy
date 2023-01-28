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
import com.mojang.blaze3d.systems.RenderSystem;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropySettings;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import me.juancarloscp52.entropy.mixin.EntryListWidgetAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class EntropyEventListWidget extends ElementListWidget<EntropyEventListWidget.ButtonEntry> {
    public final List<ButtonEntry> visibleEntries = new ArrayList<>();

    public EntropyEventListWidget(MinecraftClient minecraftClient, int i, int j, int k, int l, int m) {
        super(minecraftClient, i, j, k, l, m);
        this.centerListVertically = false;
    }

    public void addAllFromRegistry() {
        var list = new ArrayList<EventInfo>();
        for(var event : EventRegistry.entropyEvents.entrySet()) {
            String eventId = event.getKey();

            list.add(new EventInfo(Text.translatable(EventRegistry.getTranslationKey(eventId)).getString(), eventId, event.getValue().get()));
        }

        Collections.sort(list, (a, b) -> a.name.compareTo(b.name));
        list.forEach(this::addEvent);
    }

    public int addEvent(EventInfo eventInfo) {
        return this.addEntry(EntropyEventListWidget.ButtonEntry.create(eventInfo));
    }

    @Override
    protected int addEntry(ButtonEntry entry) {
        this.visibleEntries.add(entry);
        return super.addEntry(entry);
    }

    public int getRowWidth() {
        return 400;
    }

    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 32;
    }

    @Override
    protected int getEntryCount() {
        return this.visibleEntries.size();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.updateScrollingState(mouseX, mouseY, button);
        if (!this.isMouseOver(mouseX, mouseY)) {
            return false;
        } else {
            ButtonEntry entry = this.getEntryAtPositionRespectingSearch(mouseX, mouseY);
            if (entry != null) {
                if (entry.mouseClicked(mouseX, mouseY, button)) {
                    this.setFocused(entry);
                    this.setDragging(true);
                    return true;
                }
            } else if (button == 0) {
                this.clickedHeader((int)(mouseX - (double)(this.left + this.width / 2 - this.getRowWidth() / 2)), (int)(mouseY - (double)this.top) + (int)this.getScrollAmount() - 4);
                return true;
            }

            return ((EntryListWidgetAccessor) this).getScrolling();
        }
    }

    protected ButtonEntry getEntryAtPositionRespectingSearch(double x, double y) {
        int i = this.getRowWidth() / 2;
        int j = this.left + this.width / 2;
        int k = j - i;
        int l = j + i;
        int m = MathHelper.floor(y - (double)this.top) - this.headerHeight + (int)this.getScrollAmount() - 4;
        int n = m / this.itemHeight;
        return x < (double)this.getScrollbarPositionX() && x >= (double)k && x <= (double)l && n >= 0 && m >= 0 && n < this.getEntryCount() ? this.visibleEntries.get(n) : null;
    }

    @Override
    protected void renderList(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int rowLeft = this.getRowLeft();
        int rowWidth = this.getRowWidth();
        int entryHeight = this.itemHeight - 4;
        int entryCount = this.children().size();
        int drawIndex = 0;

        for (int index = 0; index < entryCount; ++index) {
            int rowTop = this.getRowTop(drawIndex);
            int rowBottom = rowTop + this.itemHeight;

            if (this.getEntry(index).checkbox.visible) {
                drawIndex++;

                if (rowBottom >= this.top && rowTop <= this.bottom)
                    this.renderEntry(matrices, mouseX, mouseY, delta, index, rowLeft, rowTop, rowWidth, entryHeight);
            }
        }
    }

    public void updateVisibleEntries(String searchText, FilterMode filterMode) {
        String lowerCasedNewText = searchText.toLowerCase(Locale.ROOT);
        visibleEntries.clear();

        if (searchText.isBlank())
            children().stream().forEach(buttonEntry -> {
                buttonEntry.checkbox.visible = filterMode.allowsVisibility(buttonEntry);

                if(buttonEntry.checkbox.visible)
                    visibleEntries.add(buttonEntry);
            });
        else {
            children().stream().forEach(buttonEntry -> {
                buttonEntry.checkbox.visible = filterMode.allowsVisibility(buttonEntry) && (buttonEntry.eventInfo.name.toLowerCase(Locale.ROOT).contains(lowerCasedNewText) || buttonEntry.eventInfo.id.toLowerCase(Locale.ROOT).contains(lowerCasedNewText));

                if(buttonEntry.checkbox.visible)
                    visibleEntries.add(buttonEntry);
            });
        }

        setScrollAmount(0.0D);
    }

    @Environment(EnvType.CLIENT)
    public static class ButtonEntry extends ElementListWidget.Entry<EntropyEventListWidget.ButtonEntry> {
        private static final Identifier ICON_OVERLAY_LOCATION = new Identifier("textures/gui/world_selection.png");
        private static final Text ACCESSIBILITY_TOOLTIP = Text.translatable("entropy.options.accessibilityMode.eventDisabled");
        public final CheckboxWidget checkbox;
        public final EventInfo eventInfo;

        private ButtonEntry(EventInfo eventInfo, CheckboxWidget checkbox) {
            this.checkbox = checkbox;
            this.eventInfo = eventInfo;
        }

        public static EntropyEventListWidget.ButtonEntry create(EventInfo eventInfo) {
            EntropySettings settings = Entropy.getInstance().settings;
            String eventID = eventInfo.id;
            boolean isDisabledByAccessibilityMode = eventInfo.event.isDisabledByAccessibilityMode() && Entropy.getInstance().settings.accessibilityMode;
            boolean enableCheckbox = !settings.disabledEvents.contains(eventID) && !isDisabledByAccessibilityMode;
            CheckboxWidget checkbox = new CheckboxWidget(0, 0, MinecraftClient.getInstance().getWindow().getScaledWidth(), 20, Text.translatable(EventRegistry.getTranslationKey(eventID)), enableCheckbox);
            checkbox.active = !isDisabledByAccessibilityMode;
            return new EntropyEventListWidget.ButtonEntry(eventInfo, checkbox);
        }

        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            checkbox.setPos(x + 32, y);
            checkbox.render(matrices, mouseX, mouseY, tickDelta);

            if(Entropy.getInstance().settings.accessibilityMode && eventInfo.event.isDisabledByAccessibilityMode()) {
                RenderSystem.setShaderTexture(0, ICON_OVERLAY_LOCATION);
                DrawableHelper.drawTexture(matrices, x, y - 6, 64, 32, 32, 32, 256, 256);

                if(mouseX >= x && mouseX <= x + 350 && mouseY >= y && mouseY <= y + entryHeight) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    List<OrderedText> lines = client.textRenderer.wrapLines(ACCESSIBILITY_TOOLTIP, 200);
                    int tooltipHeight = client.textRenderer.fontHeight * lines.size();
                    int tooltipYPosition = (int)(y + tooltipHeight * 1.5D);

                    //if the tooltip would render further down than the list reaches, render it above the entry
                    if(tooltipYPosition + tooltipHeight > client.currentScreen.height - 32)
                        tooltipYPosition = y - 12 - tooltipHeight / 2;

                    client.currentScreen.renderOrderedTooltip(matrices, lines, x, tooltipYPosition);
                }
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if(checkbox.active) {
                MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                this.checkbox.onPress();
            }

            return true;
        }

        public List<? extends Element> children() {
            return ImmutableList.of(this.checkbox);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(checkbox);
        }
    }

    public static enum FilterMode {
        ALL("entropy.options.all"),
        ENABLED("entropy.options.enabled"),
        DISABLED("entropy.options.disabled");

        public final Text text;

        private FilterMode(String text) {
            this.text = Text.translatable(text);
        }

        public boolean allowsVisibility(ButtonEntry buttonEntry) {
            return this == ALL || this == ENABLED && buttonEntry.checkbox.isChecked() || this == DISABLED && !buttonEntry.checkbox.isChecked();
        }
    }

    public record EventInfo(String name, String id, Event event) {}
}