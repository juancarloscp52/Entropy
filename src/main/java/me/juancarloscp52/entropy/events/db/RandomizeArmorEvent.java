package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

import java.util.Random;

public class RandomizeArmorEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity ->
        {
            serverPlayerEntity.inventory.armor.set(3, getRandomItem(EquipmentSlot.HEAD));
            serverPlayerEntity.inventory.armor.set(2, getRandomItem(EquipmentSlot.CHEST));
            serverPlayerEntity.inventory.armor.set(1, getRandomItem(EquipmentSlot.LEGS));
            serverPlayerEntity.inventory.armor.set(0, getRandomItem(EquipmentSlot.FEET));

        });

    }

    private ItemStack getRandomItem(EquipmentSlot slot){
        Random random = new Random();
        Item item = Registry.ITEM.getRandom(random);
        if(item instanceof ArmorItem && ((ArmorItem)item).getSlotType()==slot){
            ItemStack stack = new ItemStack(item);
            for(int i=0;i< random.nextInt(4);i++){
                Enchantment enchantment = getRandomEnchantment(stack);
                stack.addEnchantment(enchantment,getRandomLevel(enchantment));
            }
            return stack;
        }
        else
            return getRandomItem(slot);
    }
    private Enchantment getRandomEnchantment(ItemStack item){
        Enchantment enchantment = Registry.ENCHANTMENT.get(new Random().nextInt(Registry.ENCHANTMENT.getIds().size()));
        if(enchantment!=null && enchantment.isAcceptableItem(item))
            return enchantment;
        else
            return getRandomEnchantment(item);
    }
    private int getRandomLevel(Enchantment enchantment){
        return new Random().nextInt(enchantment.getMaxLevel()-enchantment.getMinLevel()+1)+ enchantment.getMinLevel();
    }
}
