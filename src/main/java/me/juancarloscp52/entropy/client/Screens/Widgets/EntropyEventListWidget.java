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
import me.juancarloscp52.entropy.events.EventRegistry;
import me.juancarloscp52.entropy.mixin.EntryListWidgetAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntropyEventListWidget extends ElementListWidget<EntropyEventListWidget.ButtonEntry> {
    public final List<ButtonEntry> visibleEntries = new ArrayList<>();

    public EntropyEventListWidget(MinecraftClient minecraftClient, int i, int j, int k, int l, int m) {
        super(minecraftClient, i, j, k, l, m);
        this.centerListVertically = false;
    }

    public void addAllFromRegistry() {
        var list = new ArrayList<Pair<String, String>>();
        for(var eventId : EventRegistry.entropyEvents.keySet())
            list.add(new Pair<String,String>(Text.translatable(EventRegistry.getTranslationKey(eventId)).getString(), eventId));

        Collections.sort(list, (a, b) -> a.getLeft().compareTo(b.getLeft()));
        list.forEach(this::addEvent);
    }

    public int addEvent(Pair<String,String> event) {
        return this.addEntry(EntropyEventListWidget.ButtonEntry.create(event));
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

            if (this.getEntry(index).button.visible) {
                drawIndex++;

                if (rowBottom >= this.top && rowTop <= this.bottom)
                    this.renderEntry(matrices, mouseX, mouseY, delta, index, rowLeft, rowTop, rowWidth, entryHeight);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static class ButtonEntry extends ElementListWidget.Entry<EntropyEventListWidget.ButtonEntry> {
        public final CheckboxWidget button;
        public final String eventName;
        public final String eventID;

        private ButtonEntry(String eventName, String eventID, CheckboxWidget button) {
            this.button = button;
            this.eventName = eventName;
            this.eventID = eventID;
        }

        public static EntropyEventListWidget.ButtonEntry create(Pair<String,String> event) {
            EntropySettings settings = Entropy.getInstance().settings;
            String eventID = event.getRight();
            return new EntropyEventListWidget.ButtonEntry(event.getLeft(), eventID, new CheckboxWidget(0, 0, MinecraftClient.getInstance().getWindow().getScaledWidth(), 20, Text.translatable(EventRegistry.getTranslationKey(eventID)), !settings.disabledEvents.contains(eventID)));
        }

        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            button.setPos(x + 32,y);
            button.render(matrices, mouseX, mouseY, tickDelta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.button.onPress();
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        }

        public List<? extends Element> children() {
            return ImmutableList.of(this.button);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(button);
        }
    }

}