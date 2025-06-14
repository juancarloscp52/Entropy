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
import me.juancarloscp52.entropy.client.Screens.Widgets.EntropyEventListWidget;
import me.juancarloscp52.entropy.client.Screens.Widgets.EntropyEventListWidget.FilterMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;

public class EntropyEventConfigurationScreen extends Screen {
    EntropySettings settings = Entropy.getInstance().settings;

    EntropyEventListWidget list;

    Screen parent;

    CycleButton<EntropyEventListWidget.FilterMode> filterEvents;

    public EntropyEventConfigurationScreen(Screen parent) {
        super(Component.translatable("entropy.options.disableEvents"));
        this.parent = parent;
    }

    protected void init() {
        list = addRenderableWidget(new EntropyEventListWidget(Minecraft.getInstance(), this.width, this.height - 65 - 30, 0, 65, 25));
        list.addAllFromRegistry();
        this.addWidget(list);
        // Done button
        Button done = Button.builder(CommonComponents.GUI_DONE, button -> onDone()).pos(this.width / 2 - 100, this.height - 26).width(200).build();
        this.addRenderableWidget(done);
        // Check all button
        Button checkAll = Button.builder(Component.translatable("entropy.options.checkAllEvents"), button -> onCheckAll()).pos(this.width / 2 - 100 - 100, this.height - 26).width(100).build();

        this.addRenderableWidget(checkAll);
        // Uncheck all button
        Button uncheckAll = Button.builder(Component.translatable("entropy.options.uncheckAllEvents"), button -> onUncheckAll()).pos(this.width / 2 - 100 + 200, this.height - 26).width(100).build();
        this.addRenderableWidget(uncheckAll);

        // Search box
        Component searchText = Component.translatable("entropy.options.search");
        EditBox search = new EditBox(Minecraft.getInstance().font, this.width / 2 - 170, 29, 200, 20, searchText);
        search.setHint(searchText);
        setInitialFocus(search);
        search.setResponder(newText -> list.updateVisibleEntries(newText, filterEvents.getValue()));
        this.addRenderableWidget(search);

        // Filter events button
        filterEvents = CycleButton.<EntropyEventListWidget.FilterMode>builder(mode -> mode.text)
                .withInitialValue(FilterMode.ALL)
                .withValues(FilterMode.values())
                .create(this.width / 2 + 40, 29, 120, 20, Component.translatable("entropy.options.filterEvents"), (button, newValue) -> list.updateVisibleEntries(search.getValue(), newValue));
        this.addRenderableWidget(filterEvents);
    }

    public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);
        drawContext.drawString(font, this.title, this.width / 2 - font.width(this.title) / 2, 12, 0xFFE0E0E0);
        EntropyConfigurationScreen.drawLogo(drawContext);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (super.mouseReleased(mouseX, mouseY, button)) {
            return true;
        } else return this.list.mouseReleased(mouseX, mouseY, button);
    }

    private void onDone() {
        settings.disabledEventTypes = new ArrayList<>();
        this.list.children().forEach(buttonEntry -> {
            if (!buttonEntry.checkbox.selected())
                settings.disabledEventTypes.add(buttonEntry.eventInfo.typeReference().key());
        });
        Entropy.getInstance().saveSettings();
        onClose();
    }

    private void onCheckAll() {
        this.list.children().forEach(buttonEntry -> {
            if (buttonEntry.checkbox.visible && !buttonEntry.checkbox.selected() && buttonEntry.eventInfo.typeReference().value().isEnabled()) {
                buttonEntry.checkbox.onPress();
            }
        });
    }

    private void onUncheckAll() {
        this.list.children().forEach(buttonEntry -> {
            if (buttonEntry.checkbox.visible && buttonEntry.checkbox.selected() && buttonEntry.eventInfo.typeReference().value().isEnabled()) {
                buttonEntry.checkbox.onPress();
            }
        });
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}