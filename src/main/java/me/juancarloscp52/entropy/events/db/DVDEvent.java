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
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class DVDEvent extends AbstractTimedEvent {

    double x = 0;
    double y = 0;
    double velX = 0;
    double velY = 0;
    int size = 150;
    Random random = new Random();
    MinecraftClient client;

    @Override
    public void initClient() {
        velX = random.nextDouble() * 8 + 2d;
        velY = random.nextDouble() * 8 + 2d;
        client = MinecraftClient.getInstance();
    }

    @Override
    public void endClient() {
        this.hasEnded = true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
        renderDVDOverlay(matrixStack, tickdelta);
    }

    @Override
    public void tickClient() {
        y += velY;
        x += velX;
        int height = client.getWindow().getScaledHeight();
        int width = client.getWindow().getScaledWidth();
        if (y + size > height || y < 0) {
            y = MathHelper.clamp(y, 0, height - size);
            velY = (velY > 0 ? -1 : 1) * (getRandomSpeed());
        }
        if (x + size > width || x < 0) {
            x = MathHelper.clamp(x, 0, width - size);
            velX = (velX > 0 ? -1 : 1) * (getRandomSpeed());
        }

        super.tickClient();
    }

    @Override
    public String type() {
        return "DVD";
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }

    private void renderDVDOverlay(MatrixStack matrixStack, float tickdelta) {
        if (client == null)
            return;
        int height = client.getWindow().getScaledHeight();
        int width = client.getWindow().getScaledWidth();
        int topSize = MathHelper.floor(y);
        int leftSize = MathHelper.floor(x);
        int bottomSize = MathHelper.floor(y + size);
        int rightSize = MathHelper.floor(x + size);
        DrawableHelper.fill(matrixStack, 0, 0, width, topSize, MathHelper.packRgb(0, 0, 0) + 255 << 24);
        DrawableHelper.fill(matrixStack, 0, 0, leftSize, height, MathHelper.packRgb(0, 0, 0) + 255 << 24);
        DrawableHelper.fill(matrixStack, 0, height, width, bottomSize, MathHelper.packRgb(0, 0, 0) + 255 << 24);
        DrawableHelper.fill(matrixStack, width, 0, rightSize, height, MathHelper.packRgb(0, 0, 0) + 255 << 24);

    }

    private double getRandomSpeed(){
        return random.nextDouble() * 8 + 2d;
    }

}
