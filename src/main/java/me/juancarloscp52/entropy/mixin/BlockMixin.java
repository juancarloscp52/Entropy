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

import me.juancarloscp52.entropy.EntropyTags;
import me.juancarloscp52.entropy.Variables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "dropStack", at = @At("HEAD"), cancellable = true)
    private static void randomDrops(World world, BlockPos pos, ItemStack stack, CallbackInfo ci) {
        if (Variables.noDrops) {
            ci.cancel();
        }
        if (Variables.randomDrops || Variables.luckyDrops) {
            if (!world.isClient && !stack.isEmpty() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
                float radius = 0.5F;
                double xOffset = (double) (world.random.nextFloat() * radius) + 0.25D;
                double yOffset = (double) (world.random.nextFloat() * radius) + 0.25D;
                double zOffset = (double) (world.random.nextFloat() * radius) + 0.25D;
                ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + xOffset, (double) pos.getY() + yOffset, (double) pos.getZ() + zOffset, computeItemStack(stack, world));
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            }
            ci.cancel();
        }
    }

    @Inject(method = "afterBreak", at = @At("HEAD"))
    private void explodeOnBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo ci){
        if(Variables.explodingPickaxe){
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20,4,false, false));
            world.createExplosion(null,pos.getX(),pos.getY(),pos.getZ(), Random.create().nextBetween(1,3), World.ExplosionSourceType.TNT);
        }
    }

    private static ItemStack computeItemStack(ItemStack itemStack, World world) {
        if (Variables.luckyDrops) {
            itemStack.setCount(itemStack.getCount() * 5);
            return itemStack;
        } else if (Variables.randomDrops) {
            return new ItemStack(getRandomItem(world), itemStack.getCount());
        }
        return null;
    }

    private static Item getRandomItem(World world) {
        Item item = Registries.ITEM.getRandom(Random.create()).get().value();
        if (item.getRegistryEntry().isIn(EntropyTags.DOES_NOT_DROP_RANDOMLY)) {
            item = getRandomItem(world);
        }
        return item.getRequiredFeatures().isSubsetOf(world.getEnabledFeatures()) ? item : getRandomItem(world);
    }

    @Inject(method = "getSlipperiness", at = @At("RETURN"), cancellable = true)
    private void changeSlipperiness(CallbackInfoReturnable<Float> cir) {
        if (Variables.slippery)
            cir.setReturnValue(1.02f);
    }

    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private static void shouldDrawSide(BlockState state, BlockView world, BlockPos pos,
            Direction side, BlockPos otherPos, CallbackInfoReturnable<Boolean> ci) {
        if (Variables.xrayActive) {
            ci.setReturnValue(state.isIn(EntropyTags.SHOWN_DURING_XRAY));
        }
    }

}