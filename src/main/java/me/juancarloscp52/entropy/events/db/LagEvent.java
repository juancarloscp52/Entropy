package me.juancarloscp52.entropy.events.db;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyUtils;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class LagEvent extends AbstractTimedEvent {
    Random random;
    boolean saved_pos;
    int countdown;
    Map<ServerPlayerEntity, BlockPos> player_positions;

    public void init() {
        random = Random.create();
        saved_pos = false;
        countdown = 0;
        player_positions = new HashMap<>();
    }

    public void tick() {
        if (countdown > 0) {
            countdown--;
            super.tick();
            return;
        }

        if (saved_pos) {
            Entropy.getInstance().eventHandler.getActivePlayers().forEach((serverPlayerEntity) -> {
                BlockPos pos = player_positions.get(serverPlayerEntity);
                if (pos != null) {
                    EntropyUtils.teleportPlayer(serverPlayerEntity, Vec3d.ofBottomCenter(pos));
                }

            });
            saved_pos = false;
        } else {
            Entropy.getInstance().eventHandler.getActivePlayers().forEach((serverPlayerEntity) -> {
                player_positions.put(serverPlayerEntity, serverPlayerEntity.getBlockPos());
            });
            saved_pos = true;
        }

        countdown = random.nextBetween(10, 40);
        super.tick();
    }

    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }

    public String type() {
        return "movement";
    }

    @Override
    public boolean isDisabledByAccessibilityMode() {
        return true;
    }
}
