/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.BlockTags;
import me.juancarloscp52.entropy.EntropyTags.EntityTypeTags;
import me.juancarloscp52.entropy.EntropyTags.ItemTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;

public class MidasTouchEvent extends AbstractTimedEvent {

    @Override
    public void tick() {
        for (var player : Entropy.getInstance().eventHandler.getActivePlayers()) {
            var minX = (int) (player.getX() - (player.getX() < 0 ? 1.5 : .5));
            var minY = (int) player.getY() - 1;
            var minZ = (int) (player.getZ() - (player.getZ() < 0 ? 1.5 : .5));
            var maxX = minX + 1;
            var maxY = minY + 3;
            var maxZ = minZ + 1;

            var world = player.serverLevel();

            // Replace blocks around with golden blocks
            for (int ix = minX; ix <= maxX; ix++) {
                for (int iy = minY; iy <= maxY; iy++) {
                    for (int iz = minZ; iz <= maxZ; iz++) {

                        var blockPos = new BlockPos(ix, iy, iz);
                        if (world.getBlockState(blockPos).is(BlockTags.IGNORED_BY_MIDAS_TOUCH))
                            continue;

                        var odds = player.getRandom().nextInt(100);

                        if (odds < 96)
                            world.setBlockAndUpdate(blockPos,
                                    world.dimension() == Level.NETHER
                                    ? Blocks.NETHER_GOLD_ORE.defaultBlockState()
                                            : Blocks.GOLD_ORE.defaultBlockState());
                        else if (odds < 98)
                            world.setBlockAndUpdate(blockPos, Blocks.RAW_GOLD_BLOCK.defaultBlockState());
                        else
                            world.setBlockAndUpdate(blockPos, Blocks.GOLD_BLOCK.defaultBlockState());
                    }
                }
            }

            // Kill mobs around and spawn golden items
            var box = new AABB(minX, minY, minZ, maxX, maxY, maxZ);
            var mobs = world.getEntities(player, box, x -> x instanceof LivingEntity && x.isAlive() && !x.getType().is(EntityTypeTags.IGNORED_BY_MIDAS_TOUCH));
            for (var mob : mobs) {

                ItemStack itemStack;

                switch (player.getRandom().nextInt(16)) {
                    case 0:
                        itemStack = new ItemStack(Items.GOLD_INGOT);
                        break;
                    case 1:
                        itemStack = new ItemStack(Items.GOLD_BLOCK);
                        break;
                    case 2:
                        itemStack = new ItemStack(Items.RAW_GOLD_BLOCK);
                        break;
                    case 3:
                        itemStack = new ItemStack(Items.GOLD_ORE);
                        break;
                    case 4:
                        itemStack = new ItemStack(Items.NETHER_GOLD_ORE);
                        break;
                    default:
                        itemStack = new ItemStack(Items.GOLD_NUGGET, player.getRandom().nextInt(6) + 2);
                        break;
                }

                var entityItem = new ItemEntity(world, mob.getX(), mob.getY(), mob.getZ(), itemStack);
                world.addFreshEntity(entityItem);
                mob.kill(world);
            }

            // Replace items that player holding in his hand and everything that player
            // wearing with golden variants
            var inventory = player.getInventory();
            var inventoryItems = new ArrayList<Tuple<NonNullList<ItemStack>, Integer>>() {
                {
                    add(new Tuple<NonNullList<ItemStack>, Integer>(inventory.offhand, 0));
                    add(new Tuple<NonNullList<ItemStack>, Integer>(inventory.armor, 0));
                    add(new Tuple<NonNullList<ItemStack>, Integer>(inventory.armor, 1));
                    add(new Tuple<NonNullList<ItemStack>, Integer>(inventory.armor, 2));
                    add(new Tuple<NonNullList<ItemStack>, Integer>(inventory.armor, 3));
                    add(new Tuple<NonNullList<ItemStack>, Integer>(inventory.items, inventory.selected));
                }
            };
            for (var pair : inventoryItems) {
                var itemStack = pair.getA().get(pair.getB());
                if (itemStack.isEmpty() || itemStack.is(ItemTags.IGNORED_BY_MIDAS_TOUCH) || itemStack.is(ItemTags.MIDAS_TOUCH_GOLDEN_ITEMS))
                    continue;
                var item = itemStack.getItem();

                ItemStack newItemStack;
                DataComponentMap components = item.components();
                var food = components.get(DataComponents.FOOD);
                if (food != null) {
                    var odds = player.getRandom().nextInt(100);

                    if(item == Items.MELON_SLICE && odds < 50)
                        newItemStack = new ItemStack(Items.GLISTERING_MELON_SLICE, itemStack.getCount());
                    else if (odds < 75)
                        newItemStack = new ItemStack(Items.GOLDEN_CARROT, itemStack.getCount());
                    else if (odds < 95)
                        newItemStack = new ItemStack(Items.GOLDEN_APPLE, itemStack.getCount());
                    else
                        newItemStack = new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, itemStack.getCount());
                } else if (item instanceof BlockItem) {
                    switch (player.getRandom().nextInt(6)) { // 50% for gold ore and 16.(6)% for something else
                        case 0:
                            newItemStack = new ItemStack(Items.GOLD_BLOCK, Math.min(itemStack.getCount(),3));
                            break;
                        case 1:
                            newItemStack = new ItemStack(Items.RAW_GOLD_BLOCK, Math.min(itemStack.getCount(),3));
                            break;
                        case 2:
                            newItemStack = new ItemStack(Items.NETHER_GOLD_ORE, Math.min(itemStack.getCount(),32));
                            break;
                        default:
                            newItemStack = new ItemStack(Items.GOLD_ORE, Math.min(itemStack.getCount(),10));
                            break;
                    }
                } else if (item instanceof ArmorItem) {
                    EquipmentSlot slot = components.get(DataComponents.EQUIPPABLE).slot();
                    if (slot == EquipmentSlot.HEAD)
                        newItemStack = new ItemStack(Items.GOLDEN_HELMET, itemStack.getCount());
                    else if (slot == EquipmentSlot.CHEST)
                        newItemStack = new ItemStack(Items.GOLDEN_CHESTPLATE, itemStack.getCount());
                    else if (slot == EquipmentSlot.LEGS)
                        newItemStack = new ItemStack(Items.GOLDEN_LEGGINGS, itemStack.getCount());
                    else if (slot == EquipmentSlot.FEET)
                        newItemStack = new ItemStack(Items.GOLDEN_BOOTS, itemStack.getCount());
                    else
                        newItemStack = new ItemStack(Items.GOLD_INGOT);
                } else if (item instanceof PickaxeItem) {
                    newItemStack = new ItemStack(Items.GOLDEN_PICKAXE, itemStack.getCount());
                } else if (item instanceof AxeItem) {
                    newItemStack = new ItemStack(Items.GOLDEN_AXE, itemStack.getCount());
                } else if (item instanceof ShovelItem) {
                    newItemStack = new ItemStack(Items.GOLDEN_SHOVEL, itemStack.getCount());
                } else if (item instanceof HoeItem) {
                    newItemStack = new ItemStack(Items.GOLDEN_HOE, itemStack.getCount());
                } else if (item instanceof SwordItem) {
                    newItemStack = new ItemStack(Items.GOLDEN_SWORD, itemStack.getCount());
                } else if (item instanceof AnimalArmorItem && item != Items.WOLF_ARMOR) {
                    newItemStack = new ItemStack(Items.GOLDEN_HORSE_ARMOR, itemStack.getCount());
                } else {
                    switch (player.getRandom().nextInt(3)) {
                        case 0:
                            newItemStack = new ItemStack(Items.GOLD_NUGGET, itemStack.getCount());
                            break;
                        case 1:
                            newItemStack = new ItemStack(Items.RAW_GOLD, itemStack.getCount());
                            break;
                        default:
                            newItemStack = new ItemStack(Items.GOLD_INGOT, itemStack.getCount());
                            break;
                    }
                }
                pair.getA().set(pair.getB(), newItemStack);
            }
        }

        super.tick();

    }

    @Override
    public short getDuration() {
        return (short) (super.getDuration() * .2);
    }

}
