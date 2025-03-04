package me.juancarloscp52.entropy.events.db;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyUtils;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class LagEvent extends AbstractTimedEvent {
    RandomSource random;
    boolean saved_pos;
    int countdown;
    Map<ServerPlayer, BlockPos> player_positions;

    public void init() {
        random = RandomSource.create();
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
                    EntropyUtils.teleportPlayer(serverPlayerEntity, Vec3.atBottomCenterOf(pos));
                }

            });
            saved_pos = false;
        } else {
            Entropy.getInstance().eventHandler.getActivePlayers().forEach((serverPlayerEntity) -> {
                player_positions.put(serverPlayerEntity, serverPlayerEntity.blockPosition());
            });
            saved_pos = true;
        }

        countdown = random.nextIntBetweenInclusive(10, 40);
        super.tick();
    }

    public String type() {
        return "movement";
    }

    @Override
    public boolean isDisabledByAccessibilityMode() {
        return true;
    }
}
