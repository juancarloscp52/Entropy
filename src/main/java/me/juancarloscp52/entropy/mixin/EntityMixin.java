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

import me.juancarloscp52.entropy.EntropyTags.ItemTags;
import me.juancarloscp52.entropy.Variables;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    private Level level;

    @Shadow
    private Entity vehicle;

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

    @Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;ZZ)Z", at = @At("HEAD"), cancellable = true)
    private void preventMounting(Entity entityToMount, boolean force, boolean sendGameEvent, CallbackInfoReturnable<Boolean> cir) {
        if(Variables.forceRiding && vehicle != null)
            cir.setReturnValue(false);
    }

    @Inject(method = "removeVehicle", at = @At("HEAD"), cancellable = true)
    private void preventDismounting(CallbackInfo ci) {
        if(Variables.forceRiding)
            ci.cancel();
    }

    @Inject(method = "spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;F)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("HEAD"), cancellable = true)
    private void randomDrops(ServerLevel level, ItemStack stack, float yOffset, CallbackInfoReturnable<ItemEntity> cir) {
        if (Variables.noDrops) {
            cir.setReturnValue(null);
            cir.cancel();
        }
        if (Variables.randomDrops || Variables.luckyDrops) {
            if (stack.isEmpty()) {
                cir.setReturnValue(null);
                cir.cancel();
            } else if (this.level.isClientSide()) {
                cir.setReturnValue(null);
                cir.cancel();
            } else {
                ItemEntity itemEntity = new ItemEntity(level, this.getX(), this.getY() + (double) yOffset, this.getZ(), computeItemStack(stack));
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
                cir.setReturnValue(itemEntity);
                cir.cancel();
            }
            cir.cancel();
        }
    }

    @Unique
    private ItemStack computeItemStack(ItemStack itemStack) {
        if (Variables.luckyDrops) {
            itemStack.setCount(itemStack.getCount() * 5);
            return itemStack;
        } else if (Variables.randomDrops) {
            return new ItemStack(getRandomItem(), itemStack.getCount());
        }
        return null;
    }

    @Unique
    private Item getRandomItem() {
        Item item = BuiltInRegistries.ITEM.getRandom(RandomSource.create()).get().value();
        if (item.builtInRegistryHolder().is(ItemTags.DOES_NOT_DROP_RANDOMLY)) {
            item = getRandomItem();
        }
        return item.requiredFeatures().isSubsetOf(level.enabledFeatures()) ? item : getRandomItem();
    }

}
