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

package me.juancarloscp52.entropy.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.juancarloscp52.entropy.client.Screens.EntropyConfigurationScreen;
import me.juancarloscp52.entropy.client.Screens.EntropyErrorScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;


public class EntropyModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent) -> {
            if (MinecraftClient.getInstance().getGame().getCurrentSession() == null) {
                return new EntropyConfigurationScreen(parent);
            } else {
                return new EntropyErrorScreen(parent, Text.translatable("entropy.options.error"));
            }

        };
    }


}
