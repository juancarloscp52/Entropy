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

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.MinecraftClient;

public class LowRenderDistanceEvent extends AbstractTimedEvent {

    MinecraftClient client;
    private int viewDistance = 0;

    @Override
    public void initClient() {
        client = MinecraftClient.getInstance();
        viewDistance = this.client.options.getViewDistance().getValue();
        this.client.options.getViewDistance().setValue(2);
    }

    @Override
    public void endClient() {
        super.endClient();
        client = MinecraftClient.getInstance();
        this.client.options.getViewDistance().setValue(viewDistance);
    }

    @Override
    public String type() {
        return "renderDistance";
    }
}
