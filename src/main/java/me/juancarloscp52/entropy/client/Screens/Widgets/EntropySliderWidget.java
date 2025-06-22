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

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;


public class EntropySliderWidget extends AbstractSliderButton {

    private final String translationKey;
    private final ValueFormatter formatter;
    private final ValueUpdater valueUpdater;
    private final double min;
    private final double max;

    public EntropySliderWidget(int x, int y, int width, int height, String translationKey, double value, double min, double max, ValueFormatter formatter, ValueUpdater valueUpdater) {
        super(x, y, width, height, Component.translatable(translationKey), fractionFromValue(value, min, max));
        this.translationKey = translationKey;
        this.formatter = formatter;
        this.valueUpdater = valueUpdater;
        this.min = min;
        this.max = max;
        this.updateMessage();
    }

    private static double fractionFromValue(final double value, final double min, final double max) {
        return (value - min) / (max - min);
    }

    public double getValue() {
        return min + value * (max - min);
    }

    protected void updateMessage() {
    }

    @Override
    public Component getMessage() {
        return Component.translatable(translationKey, formatter.format(getValue()));
    }

    @Override
    protected void applyValue() {
        valueUpdater.applyValue(getValue());
    }

    @FunctionalInterface
    public interface ValueFormatter {
        Component format(double value);
    }

    @FunctionalInterface
    public interface ValueUpdater {
        void applyValue(double value);
    }
}
