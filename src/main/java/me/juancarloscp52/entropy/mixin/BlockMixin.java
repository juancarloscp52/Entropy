package me.juancarloscp52.entropy.mixin;

import me.juancarloscp52.entropy.Variables;
import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

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
                ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + xOffset, (double) pos.getY() + yOffset, (double) pos.getZ() + zOffset, computeItemStack(stack));
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            }
            ci.cancel();
        }
    }

    private static ItemStack computeItemStack(ItemStack itemStack) {
        if (Variables.luckyDrops) {
            itemStack.setCount(itemStack.getCount() * 5);
            return itemStack;
        } else if (Variables.randomDrops) {
            return new ItemStack(getRandomItem(), itemStack.getCount());
        }
        return null;
    }

    private static Item getRandomItem() {
        Item item = Registry.ITEM.getRandom(new Random());
        if (item.toString().equals("debug_stick") || item.toString().contains("spawn_egg") || item.toString().contains("command_block") || item.toString().contains("structure_void") || item.toString().contains("barrier")) {
            item = getRandomItem();
        }
        return item;
    }

    @Inject(method = "getSlipperiness", at = @At("RETURN"), cancellable = true)
    private void changeSlipperiness(CallbackInfoReturnable<Float> cir) {
        if (Variables.slippery)
            cir.setReturnValue(1.02f);
    }

}