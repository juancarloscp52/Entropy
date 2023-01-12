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
import me.juancarloscp52.entropy.client.Screens.Widgets.EntropyEventListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;

import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Locale;

public class EntropyEventConfigurationScreen extends Screen {
    private static final Identifier LOGO = new Identifier("entropy", "textures/logo-with-text.png");
    EntropySettings settings = Entropy.getInstance().settings;

    EntropyEventListWidget list;

    Screen parent;

    public EntropyEventConfigurationScreen(Screen parent) {
        super(Text.translatable("entropy.options.disableEvents"));
        this.parent = parent;
    }

    protected void init() {
        list = new EntropyEventListWidget(MinecraftClient.getInstance(), this.width, this.height, 56, this.height - 32, 25);
        list.addAllFromRegistry();
        this.addSelectableChild(list);
        // Done button
        ButtonWidget done = ButtonWidget.builder(ScreenTexts.DONE, button -> onDone()).position(this.width / 2 - 100, this.height - 26).width(200).build();
        this.addDrawableChild(done);
        // Check all button
        ButtonWidget checkAll = ButtonWidget.builder(Text.translatable("entropy.options.checkAllEvents"), button -> onCheckAll()).position(this.width / 2 - 100 - 100, this.height - 26).width(100).build();

        this.addDrawableChild(checkAll);
        // Uncheck all button
        ButtonWidget uncheckAll = ButtonWidget.builder(Text.translatable("entropy.options.uncheckAllEvents"), button -> onUncheckAll()).position(this.width / 2 - 100 + 200, this.height - 26).width(100).build();
        this.addDrawableChild(uncheckAll);

        // Search box
        Text searchText = Text.translatable("entropy.options.search");
        TextFieldWidget search = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, this.width / 2 - 100, 29, 200, 20, searchText);
        search.setPlaceholder(searchText);
        search.setChangedListener(newText -> {
            String lowerCasedNewText = newText.toLowerCase(Locale.ROOT);
            list.visibleEntries.clear();

            if (newText.isBlank())
                list.children().stream().forEach(buttonEntry -> {
                    buttonEntry.button.visible = true;
                    list.visibleEntries.add(buttonEntry);
                });
            else {
                list.children().stream().forEach(buttonEntry -> {
                    buttonEntry.button.visible = buttonEntry.eventName.toLowerCase(Locale.ROOT).contains(lowerCasedNewText) || buttonEntry.eventID.toLowerCase(Locale.ROOT).contains(lowerCasedNewText);

                    if(buttonEntry.button.visible)
                        list.visibleEntries.add(buttonEntry);
                });
                list.setScrollAmount(0.0D);
            }
        });
        this.addDrawableChild(search);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        matrices.push();
        matrices.translate(this.width / 2f - 18.8f, 0, 0);
        matrices.scale(0.2f, 0.2f, 0.2f);
        RenderSystem.setShaderTexture(0, LOGO);
        this.drawTexture(matrices, 0, 0, 0, 0, 188, 187);
        matrices.pop();
        RenderSystem.disableBlend();
        this.list.render(matrices, mouseX, mouseY, delta);
        this.textRenderer.drawWithShadow(matrices, this.title, this.width / 2f - textRenderer.getWidth(this.title) / 2f, 12, 14737632);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (super.mouseReleased(mouseX, mouseY, button)) {
            return true;
        } else return this.list.mouseReleased(mouseX, mouseY, button);
    }

    private void onDone() {
        settings.disabledEvents = new ArrayList<>();
        this.list.children().forEach(buttonEntry -> {
            if (!buttonEntry.button.isChecked())
                settings.disabledEvents.add(buttonEntry.eventID);
        });
        Entropy.getInstance().saveSettings();
        close();
    }

    private void onCheckAll() {
        this.list.children().forEach(buttonEntry -> {
            if (buttonEntry.button.visible && !buttonEntry.button.isChecked()) {buttonEntry.button.onPress();}
        });
    }

    private void onUncheckAll() {
        this.list.children().forEach(buttonEntry -> {
            if (buttonEntry.button.visible && buttonEntry.button.isChecked()) {buttonEntry.button.onPress();}
        });
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}