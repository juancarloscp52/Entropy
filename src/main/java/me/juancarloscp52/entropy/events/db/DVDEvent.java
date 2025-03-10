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

import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import java.util.Random;

public class DVDEvent extends AbstractTimedEvent {

    double x = 0;
    double y = 0;
    double velX = 0;
    double velY = 0;
    int size = 250;
    Random random = new Random();
    Minecraft client;

    @Override
    public void initClient() {
        velX = random.nextDouble() * 8 + 2d;
        velY = random.nextDouble() * 8 + 2d;
        client = Minecraft.getInstance();
        System.out.println("values");
        System.out.println(client.getWindow().getGuiScale());
        if(client.getWindow().getGuiScale()>=3){
            size=200;
        }
    }

    @Override
    public void render(GuiGraphics drawContext, DeltaTracker tickCounter) {
        renderDVDOverlay(drawContext, tickCounter);
    }

    @Override
    public void tickClient() {
        y += velY;
        x += velX;
        int height = client.getWindow().getGuiScaledHeight();
        int width = client.getWindow().getGuiScaledWidth();
        if (y + size > height || y < 0) {
            y = Mth.clamp(y, 0, height - size);
            velY = (velY > 0 ? -1 : 1) * (getRandomSpeed());
        }
        if (x + size > width || x < 0) {
            x = Mth.clamp(x, 0, width - size);
            velX = (velX > 0 ? -1 : 1) * (getRandomSpeed());
        }

        super.tickClient();
    }

    @Override
    public short getDuration() {
        return (short) (super.getDuration() * 0.75d);
    }

    private void renderDVDOverlay(GuiGraphics drawContext, DeltaTracker tickCounter) {
        if (client == null)
            return;
        int height = client.getWindow().getGuiScaledHeight();
        int width = client.getWindow().getGuiScaledWidth();
        int topSize = Mth.floor(y);
        int leftSize = Mth.floor(x);
        int bottomSize = Mth.floor(y + size);
        int rightSize = Mth.floor(x + size);
        drawContext.fill(0, 0, width, topSize, ARGB.color(255,0, 0, 0));
        drawContext.fill(0, 0, leftSize, height, ARGB.color(255,0, 0, 0));
        drawContext.fill(0, height, width, bottomSize, ARGB.color(255,0, 0, 0));
        drawContext.fill(width, 0, rightSize, height, ARGB.color(255,0, 0, 0));

    }

    private double getRandomSpeed(){
        return random.nextDouble() * 8 + 2d;
    }

}
