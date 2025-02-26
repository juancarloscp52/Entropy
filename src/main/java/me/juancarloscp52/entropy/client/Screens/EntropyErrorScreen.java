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

import java.util.List;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

public class EntropyErrorScreen extends Screen {
    Screen parent;
    Text message;

    public EntropyErrorScreen(Screen parent, Text message) {
        super(Text.translatable("entropy.errorScreen.title"));
        this.message = message;
        this.parent = parent;
    }


    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.BACK,
                button -> close()).position(this.width / 2 - 100, this.height - 40).width(200).build());
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        renderBackground(drawContext);
        List<OrderedText> lines = textRenderer.wrapLines(message, this.width / 2);
        for (int i = 0; i < lines.size(); i++) {
            OrderedText line = lines.get(i);
            drawContext.drawTextWithShadow(textRenderer, line, this.width / 4, this.height / 2 - (lines.size() * 9 / 2) + i * 9, 16777215);
        }
        super.render(drawContext, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}
