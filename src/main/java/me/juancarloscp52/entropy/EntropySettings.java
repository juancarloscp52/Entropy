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

package me.juancarloscp52.entropy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.text.Text;

public class EntropySettings {

    public enum UIStyle {
        GTAV("entropy.options.ui.gtav"), MINECRAFT("entropy.options.ui.minecraft");

        public final Text text;
        public final Text tooltip;

        private UIStyle(String text) {
            this.text = Text.translatable(text);
            this.tooltip = Text.translatable(text + ".tooltip");
        }
    }

    public enum VotingMode {
        MAJORITY("entropy.options.votingMode.majority"), PROPORTIONAL("entropy.options.votingMode.proportional");

        public final Text text;
        public final Text tooltip;

        private VotingMode(String text) {
            this.text = Text.translatable(text);
            this.tooltip = Text.translatable(text + ".tooltip");
        }
    }
    public short timerDuration = 900;
    public short baseEventDuration = 600;
    public boolean integrations = false;
    public VotingMode votingMode = VotingMode.PROPORTIONAL;
    public UIStyle UIstyle = UIStyle.GTAV;
    public List<String> disabledEvents = new ArrayList<>();
    public boolean accessibilityMode = false;
}
