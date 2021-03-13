package me.juancarloscp52.entropy.mixin;

import me.juancarloscp52.entropy.Variables;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public World world;

    @Shadow public abstract double getX();

    @Shadow public abstract double getY();

    @Shadow public abstract double getZ();

    @Inject(method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;", at=@At("HEAD"),cancellable = true)
    private void randomDrops(ItemStack stack, float yOffset, CallbackInfoReturnable<ItemEntity> cir) {
        if(Variables.noDrops){
            cir.setReturnValue(null);
            cir.cancel();
        }
        if (Variables.randomDrops || Variables.luckyDrops) {
            if (stack.isEmpty()) {
                cir.setReturnValue(null);
                cir.cancel();
            } else if (this.world.isClient) {
                cir.setReturnValue(null);
                cir.cancel();
            } else {
                ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY() + (double) yOffset, this.getZ(), computeItemStack(stack));
                itemEntity.setToDefaultPickupDelay();
                this.world.spawnEntity(itemEntity);
                cir.setReturnValue(itemEntity);
                cir.cancel();
            }
            cir.cancel();
        }
    }

    private ItemStack computeItemStack(ItemStack itemStack){
        if(Variables.luckyDrops){
            itemStack.setCount(itemStack.getCount()*5);
            return itemStack;
        }else if(Variables.randomDrops){
            return new ItemStack(getRandomItem(), itemStack.getCount());
        }
        return null;
    }
    private Item getRandomItem(){
        Item item = Registry.ITEM.getRandom(new Random());
        if(item.toString().equals("debug_stick") || item.toString().contains("spawn_egg") || item.toString().contains("command_block") || item.toString().contains("structure_void") || item.toString().contains("barrier")){
            item = getRandomItem();
        }
        return item;
    }

}
