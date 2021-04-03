package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

import java.util.Random;

public class ItemRainEvent extends AbstractTimedEvent {

    Random random;

    @Override
    public void init() {
        random = new Random();
    }

    @Override
    public void end() {
        this.hasEnded = true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public String type() {
        return "rain";
    }

    @Override
    public void tick() {
        if (getTickCount() % 10 == 0) {
            for (int i = 0; i < 7; i++) {
                PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> {
                    ItemEntity item = new ItemEntity(serverPlayerEntity.getServerWorld(), serverPlayerEntity.getX() + (random.nextInt(100) - 50), serverPlayerEntity.getY() + 50 + (random.nextInt(10) - 5), serverPlayerEntity.getZ() + (random.nextInt(100) - 50), new ItemStack(getRandomItem(), 1));
                    serverPlayerEntity.getServerWorld().spawnEntity(item);
                });
            }
        }
        super.tick();
    }

    private Item getRandomItem() {
        Item item = Registry.ITEM.getRandom(new Random());
        if (item.toString().equals("debug_stick") || item.toString().contains("spawn_egg") || item.toString().contains("command_block") || item.toString().contains("structure_void") || item.toString().contains("barrier")) {
            item = getRandomItem();
        }
        return item;
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
