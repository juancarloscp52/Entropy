package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class UpgradeRandomGearEvent extends AbstractInstantEvent {

    public static final EventType<UpgradeRandomGearEvent> TYPE = EventType.builder(UpgradeRandomGearEvent::new).build();
    private static HashMap<Item, Item> _upgrades = new HashMap<Item, Item>() {
        {
            // HELMET
            put(Items.LEATHER_HELMET, Items.GOLDEN_HELMET);
            put(Items.GOLDEN_HELMET, Items.CHAINMAIL_HELMET);
            put(Items.CHAINMAIL_HELMET, Items.IRON_HELMET);
            put(Items.IRON_HELMET, Items.TURTLE_HELMET);
            put(Items.TURTLE_HELMET, Items.DIAMOND_HELMET);
            put(Items.DIAMOND_HELMET, Items.NETHERITE_HELMET);

            // CHESTPLATE
            put(Items.LEATHER_CHESTPLATE, Items.GOLDEN_CHESTPLATE);
            put(Items.GOLDEN_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE);
            put(Items.CHAINMAIL_CHESTPLATE, Items.IRON_CHESTPLATE);
            put(Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE);
            put(Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE);

            // LEGGINGS
            put(Items.LEATHER_LEGGINGS, Items.GOLDEN_LEGGINGS);
            put(Items.GOLDEN_LEGGINGS, Items.CHAINMAIL_LEGGINGS);
            put(Items.CHAINMAIL_LEGGINGS, Items.IRON_LEGGINGS);
            put(Items.IRON_LEGGINGS, Items.DIAMOND_LEGGINGS);
            put(Items.DIAMOND_LEGGINGS, Items.NETHERITE_LEGGINGS);

            // BOOTS
            put(Items.LEATHER_BOOTS, Items.GOLDEN_BOOTS);
            put(Items.GOLDEN_BOOTS, Items.CHAINMAIL_BOOTS);
            put(Items.CHAINMAIL_BOOTS, Items.IRON_BOOTS);
            put(Items.IRON_BOOTS, Items.DIAMOND_BOOTS);
            put(Items.DIAMOND_BOOTS, Items.NETHERITE_BOOTS);

            // SWORD
            put(Items.WOODEN_SWORD, Items.GOLDEN_SWORD);
            put(Items.GOLDEN_SWORD, Items.STONE_SWORD);
            put(Items.STONE_SWORD, Items.IRON_SWORD);
            put(Items.IRON_SWORD, Items.DIAMOND_SWORD);
            put(Items.DIAMOND_SWORD, Items.NETHERITE_SWORD);

            // SHOVEL
            put(Items.WOODEN_SHOVEL, Items.GOLDEN_SHOVEL);
            put(Items.GOLDEN_SHOVEL, Items.STONE_SHOVEL);
            put(Items.STONE_SHOVEL, Items.IRON_SHOVEL);
            put(Items.IRON_SHOVEL, Items.DIAMOND_SHOVEL);
            put(Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL);

            // PICKAXE
            put(Items.WOODEN_PICKAXE, Items.GOLDEN_PICKAXE);
            put(Items.GOLDEN_PICKAXE, Items.STONE_PICKAXE);
            put(Items.STONE_PICKAXE, Items.IRON_PICKAXE);
            put(Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE);
            put(Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE);

            // AXE
            put(Items.WOODEN_AXE, Items.GOLDEN_AXE);
            put(Items.GOLDEN_AXE, Items.STONE_AXE);
            put(Items.STONE_AXE, Items.IRON_AXE);
            put(Items.IRON_AXE, Items.DIAMOND_AXE);
            put(Items.DIAMOND_AXE, Items.NETHERITE_AXE);

            // HOE
            put(Items.WOODEN_HOE, Items.GOLDEN_HOE);
            put(Items.GOLDEN_HOE, Items.STONE_HOE);
            put(Items.STONE_HOE, Items.IRON_HOE);
            put(Items.IRON_HOE, Items.DIAMOND_HOE);
            put(Items.DIAMOND_HOE, Items.NETHERITE_HOE);
        }
    };

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {

            var inventory = serverPlayerEntity.getInventory();
            var items = new ArrayList<Tuple<Integer, ItemStack>>();

            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (!stack.isEmpty() && _upgrades.containsKey(stack.getItem())) {
                    items.add(new Tuple<>(i, stack));
                }
            }

            if (!items.isEmpty()) {
                Collections.shuffle(items);
                Tuple<Integer, ItemStack> tuple = items.getFirst();
                upgrade(inventory, tuple.getA(), tuple.getB(), serverPlayerEntity.getRandom());
            }
        });
    }

    private void upgrade(Inventory inventory, int index, ItemStack itemStack, RandomSource random) {
        var newItem = _upgrades.get(itemStack.getItem());
        var newDamage = (float) itemStack.getDamageValue() / (float) itemStack.getMaxDamage()
                * (float) newItem.getDefaultInstance().getMaxDamage();
        var newItemStack = new ItemStack(newItem);

        newItemStack.setDamageValue((int) newDamage);
        newItemStack.set(DataComponents.ENCHANTMENTS, itemStack.getEnchantments());

        inventory.setItem(index, newItemStack);

        if (_upgrades.containsKey(newItem) && random.nextDouble() < 0.25d)
            upgrade(inventory, index, newItemStack, random);
    }

    @Override
    public EventType<UpgradeRandomGearEvent> getType() {
        return TYPE;
    }
}
