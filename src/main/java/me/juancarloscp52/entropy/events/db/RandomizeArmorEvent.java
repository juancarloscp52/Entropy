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
import me.juancarloscp52.entropy.EntropyTags.EnchantmentTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Optional;


public class RandomizeArmorEvent extends AbstractInstantEvent {
    public static final EventType<RandomizeArmorEvent> TYPE = EventType.builder(RandomizeArmorEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity ->
        {
            replaceArmorInSlot(serverPlayerEntity, EquipmentSlot.HEAD);
            replaceArmorInSlot(serverPlayerEntity, EquipmentSlot.CHEST);
            replaceArmorInSlot(serverPlayerEntity, EquipmentSlot.LEGS);
            replaceArmorInSlot(serverPlayerEntity, EquipmentSlot.FEET);
        });

    }

    private void replaceArmorInSlot(ServerPlayer player, EquipmentSlot slot) {
        player.setItemSlot(slot, getRandomItem(player, slot));
    }

    private ItemStack getRandomItem(ServerPlayer serverPlayerEntity, EquipmentSlot slot){
        RandomSource random = RandomSource.create();
        Item item = BuiltInRegistries.ITEM.getRandom(random).get().value();
        DataComponentMap components = item.components();
        if(components.has(DataComponents.EQUIPPABLE) && components.get(DataComponents.EQUIPPABLE).slot()==slot){
            if(!item.requiredFeatures().isSubsetOf(serverPlayerEntity.level().enabledFeatures()))
                return getRandomItem(serverPlayerEntity, slot);
            ItemStack stack = new ItemStack(item);
            for(int i=0;i< random.nextInt(4);i++){
                Holder<Enchantment> enchantment = getRandomEnchantment(serverPlayerEntity, stack);
                if (enchantment != null) {
                    stack.enchant(enchantment, getRandomLevel(enchantment.value()));
                }
            }
            return stack;
        }
        else
            return getRandomItem(serverPlayerEntity, slot);
    }

    private Holder<Enchantment> getRandomEnchantment(ServerPlayer player, ItemStack item) {
        return getRandomEnchantment(player, item, 10);
    }

    private Holder<Enchantment> getRandomEnchantment(ServerPlayer player, ItemStack item, int tries) {
        if (tries <= 0) {
            return null;
        }
        final RegistryAccess registries = player.registryAccess();
        Optional<Holder.Reference<Enchantment>> enchantment = registries.lookupOrThrow(Registries.ENCHANTMENT).getRandom(player.getRandom());
        if (enchantment.isPresent() && enchantment.get().value().canEnchant(item) && !enchantment.get().is(EnchantmentTags.DO_NOT_ENCHANT_WITH))
            return enchantment.get();
        else
            return getRandomEnchantment(player, item, --tries);
    }
    private int getRandomLevel(Enchantment enchantment){
        return RandomSource.create().nextInt(enchantment.getMaxLevel()-enchantment.getMinLevel()+1)+ enchantment.getMinLevel();
    }

    @Override
    public EventType<RandomizeArmorEvent> getType() {
        return TYPE;
    }
}
