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

package me.juancarloscp52.entropy.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.juancarloscp52.entropy.Variables;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.FogRenderer;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @ModifyReturnValue(method = "setupFog", at = @At(value = "RETURN", ordinal = 1))
    private static FogParameters changeFogDistance(FogParameters original, Camera camera, FogRenderer.FogMode fogMode, Vector4f fogColor) {
        if (Variables.customFog) {
            fogColor.x = 0.0F;
            fogColor.y = 0.0F;
            fogColor.z = 0.0F;
            return new FogParameters(-150.0F, 100.0F, original.shape(), 0.0F, 0.0F, 0.0F, 1.0F);
        }
        else {
            return original;
        }
    }

}