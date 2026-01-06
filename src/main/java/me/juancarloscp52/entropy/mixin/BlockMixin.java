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

import me.juancarloscp52.entropy.EntropyTags.BlockTags;
import me.juancarloscp52.entropy.EntropyTags.ItemTags;
import me.juancarloscp52.entropy.Variables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "updateEntityMovementAfterFallOn", at = @At("HEAD"), cancellable = true)
    private void bounce(BlockGetter level, Entity entity, CallbackInfo ci) {
        if(Variables.bouncyBlocks && !entity.isSuppressingBounce()) {
            Vec3 vec3 = entity.getDeltaMovement();
            if (vec3.y < (double)-0.1F) {
                ci.cancel();
                double d = entity instanceof LivingEntity ? (double) 1.0F : 0.8;
                entity.setDeltaMovement(vec3.x, -vec3.y * d, vec3.z);
            }
        }
    }

    @Inject(method = "popResource", at = @At("HEAD"), cancellable = true)
    private static void randomDrops(Level world, BlockPos pos, ItemStack stack, CallbackInfo ci) {
        if (Variables.noDrops) {
            ci.cancel();
        }
        if (Variables.randomDrops || Variables.luckyDrops) {
            if (world instanceof ServerLevel serverLevel && !stack.isEmpty() && serverLevel.getGameRules().get(GameRules.BLOCK_DROPS)) {
                float radius = 0.5F;
                double xOffset = (double) (world.random.nextFloat() * radius) + 0.25D;
                double yOffset = (double) (world.random.nextFloat() * radius) + 0.25D;
                double zOffset = (double) (world.random.nextFloat() * radius) + 0.25D;
                ItemEntity itemEntity = new ItemEntity(serverLevel, (double) pos.getX() + xOffset, (double) pos.getY() + yOffset, (double) pos.getZ() + zOffset, computeItemStack(stack, serverLevel));
                itemEntity.setDefaultPickUpDelay();
                serverLevel.addFreshEntity(itemEntity);
            }
            ci.cancel();
        }
    }

    @Inject(method = "playerDestroy", at = @At("HEAD"))
    private void explodeOnBreak(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo ci){
        if(Variables.explodingPickaxe){
            player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 20,4,false, false));
            world.explode(null,pos.getX(),pos.getY(),pos.getZ(), RandomSource.create().nextIntBetweenInclusive(1,3), Level.ExplosionInteraction.TNT);
        }
    }

    private static ItemStack computeItemStack(ItemStack itemStack, Level world) {
        if (Variables.luckyDrops) {
            itemStack.setCount(itemStack.getCount() * 5);
            return itemStack;
        } else if (Variables.randomDrops) {
            return new ItemStack(getRandomItem(world), itemStack.getCount());
        }
        return null;
    }

    private static Item getRandomItem(Level world) {
        Item item = BuiltInRegistries.ITEM.getRandom(RandomSource.create()).get().value();
        if (item.builtInRegistryHolder().is(ItemTags.DOES_NOT_DROP_RANDOMLY)) {
            item = getRandomItem(world);
        }
        return item.requiredFeatures().isSubsetOf(world.enabledFeatures()) ? item : getRandomItem(world);
    }

    @Inject(method = "getFriction", at = @At("RETURN"), cancellable = true)
    private void changeSlipperiness(CallbackInfoReturnable<Float> cir) {
        if (Variables.slippery)
            cir.setReturnValue(1.02f);
    }

    @Inject(method = "shouldRenderFace", at = @At("HEAD"), cancellable = true)
    private static void shouldDrawSide(BlockState state, BlockState neighbor, Direction face, CallbackInfoReturnable<Boolean> ci) {
        if (Variables.xrayActive) {
            ci.setReturnValue(state.is(BlockTags.SHOWN_DURING_XRAY));
        }
    }

}