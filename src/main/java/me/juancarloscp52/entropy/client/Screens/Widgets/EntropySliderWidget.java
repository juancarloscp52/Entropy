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

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;


public class EntropySliderWidget extends SliderWidget {

    String translationKey;
    MessageSupplier messageSupplier;
    ValueUpdater valueUpdater;

    public EntropySliderWidget(int x, int y, int width, int height, String translationKey, double value, MessageSupplier messageSupplier, ValueUpdater valueUpdater) {
        super(x, y, width, height, Text.translatable(translationKey), value);
        this.translationKey=translationKey;
        this.messageSupplier=messageSupplier;
        this.valueUpdater=valueUpdater;
        this.updateMessage();
    }

    protected void updateMessage() {
    }

    @Override
    public Text getMessage() {
        return this.messageSupplier.updateMessage(this,this.translationKey,this.value);
    }

    @Override
    protected void applyValue() {
        valueUpdater.applyValue(this.value);
    }

    public interface MessageSupplier {
        Text updateMessage(EntropySliderWidget slider,String translationKey, double value);
    }

    public interface ValueUpdater {
        void applyValue(double value);
    }
}
