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
import me.juancarloscp52.entropy.EntropySettings;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import me.juancarloscp52.entropy.mixin.AbstractSelectionListAccessor;
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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class EntropyEventListWidget extends ContainerObjectSelectionList<EntropyEventListWidget.ButtonEntry> {
    public final List<ButtonEntry> visibleEntries = new ArrayList<>();
    private final Font textRenderer;

    public EntropyEventListWidget(Minecraft minecraftClient, int width, int height, int x, int y, int itemHeight) {
        super(minecraftClient, width, height, y, itemHeight);
        this.setX(x);
        this.centerListVertically = false;
        this.textRenderer = minecraftClient.font;
    }

    public void addAllFromRegistry() {
        var list = new ArrayList<EventInfo>();
        for(var event : EventRegistry.entropyEvents.entrySet()) {
            String eventId = event.getKey();

            list.add(new EventInfo(Component.translatable(EventRegistry.getTranslationKey(eventId)).getString(), eventId, event.getValue().get()));
        }

        Collections.sort(list, (a, b) -> a.name.compareTo(b.name));
        list.forEach(this::addEvent);
    }

    public int addEvent(EventInfo eventInfo) {
        return this.addEntry(EntropyEventListWidget.ButtonEntry.create(eventInfo, textRenderer));
    }

    @Override
    protected int addEntry(ButtonEntry entry) {
        this.visibleEntries.add(entry);
        return super.addEntry(entry);
    }

    public int getRowWidth() {
        return 400;
    }

    @Override
    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 32;
    }

    @Override
    protected int getItemCount() {
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
                this.clickedHeader((int)(mouseX - (double)(this.getX() + this.width / 2 - this.getRowWidth() / 2)), (int)(mouseY - (double)this.getY()) + (int)this.getScrollAmount() - 4);
                return true;
            }

            return ((AbstractSelectionListAccessor) this).getScrolling();
        }
    }

    protected ButtonEntry getEntryAtPositionRespectingSearch(double x, double y) {
        int i = this.getRowWidth() / 2;
        int j = this.getX() + this.width / 2;
        int k = j - i;
        int l = j + i;
        int m = Mth.floor(y - (double)this.getY()) - this.headerHeight + (int)this.getScrollAmount() - 4;
        int n = m / this.itemHeight;
        return x < (double)this.getScrollbarPosition() && x >= (double)k && x <= (double)l && n >= 0 && m >= 0 && n < this.getItemCount() ? this.visibleEntries.get(n) : null;
    }

    @Override
    protected void renderListItems(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
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

                if (rowBottom >= this.getY() && rowTop <= this.getBottom())
                    this.renderItem(drawContext, mouseX, mouseY, delta, index, rowLeft, rowTop, rowWidth, entryHeight);
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
    public static class ButtonEntry extends ContainerObjectSelectionList.Entry<EntropyEventListWidget.ButtonEntry> {
        private static final ResourceLocation ICON_OVERLAY_LOCATION = ResourceLocation.withDefaultNamespace("world_list/warning_highlighted");
        private static final Tooltip ACCESSIBILITY_TOOLTIP = Tooltip.create(Component.translatable("entropy.options.accessibilityMode.eventDisabled"));
        public final Checkbox checkbox;
        public final EventInfo eventInfo;

        private ButtonEntry(EventInfo eventInfo, Checkbox checkbox) {
            this.checkbox = checkbox;
            this.eventInfo = eventInfo;
        }

        public static EntropyEventListWidget.ButtonEntry create(EventInfo eventInfo, Font textRenderer) {
            EntropySettings settings = Entropy.getInstance().settings;
            String eventID = eventInfo.id;
            boolean isDisabledByAccessibilityMode = eventInfo.event.isDisabledByAccessibilityMode() && Entropy.getInstance().settings.accessibilityMode;
            boolean enableCheckbox = !settings.disabledEvents.contains(eventID) && !isDisabledByAccessibilityMode;
            final Checkbox checkbox = Checkbox.builder(Component.translatable(EventRegistry.getTranslationKey(eventID)), textRenderer).pos(0, 0).selected(enableCheckbox).onValueChange(isDisabledByAccessibilityMode ? ButtonEntry::onDisabledCheckboxPressed : Checkbox.OnValueChange.NOP).build();
            if (isDisabledByAccessibilityMode)
                checkbox.setTooltip(ACCESSIBILITY_TOOLTIP);

            return new EntropyEventListWidget.ButtonEntry(eventInfo, checkbox);
        }

        private static void onDisabledCheckboxPressed(Checkbox widget, boolean checked) {
            if (checked) {
                widget.onPress();
            }
        }

        public void render(GuiGraphics drawContext, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            checkbox.setPosition(x + 32, y);
            checkbox.render(drawContext, mouseX, mouseY, tickDelta);

            if(Entropy.getInstance().settings.accessibilityMode && eventInfo.event.isDisabledByAccessibilityMode()) {
                drawContext.blitSprite(RenderType::guiTextured, ICON_OVERLAY_LOCATION, x, y - 6, 32, 32);

                if(mouseX >= x && mouseX <= x + 32 && mouseY >= y && mouseY <= y + entryHeight)
                    Minecraft.getInstance().screen.setTooltipForNextRenderPass(ACCESSIBILITY_TOOLTIP, new MenuTooltipPositioner(checkbox.getRectangle()), false);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if(checkbox.active) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                this.checkbox.onPress();
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

    public record EventInfo(String name, String id, Event event) {}
}