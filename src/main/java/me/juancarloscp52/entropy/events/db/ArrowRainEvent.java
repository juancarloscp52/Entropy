package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.ArrowEntity;

import java.util.Random;

public class ArrowRainEvent extends AbstractTimedEvent {

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
    public void tick() {
        if (getTickCount() % 10 == 0) {
            for (int i = 0; i < 10; i++) {
                PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> {
                    ArrowEntity arrow = new ArrowEntity(serverPlayerEntity.getServerWorld(), serverPlayerEntity.getX() + (random.nextInt(50) - 25), serverPlayerEntity.getY() + 50 + (random.nextInt(10) - 5), serverPlayerEntity.getZ() + (random.nextInt(50) - 25));
                    serverPlayerEntity.getServerWorld().spawnEntity(arrow);
                });
            }
        }
        super.tick();
    }

    @Override
    public String type() {
        return "rain";
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}