package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyUtils;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class LagEvent extends AbstractTimedEvent {
    public static final EventType<LagEvent> TYPE = EventType.builder(LagEvent::new).category(EventCategory.MOVEMENT).disabledByAccessibilityMode().build();
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

    @Override
    public EventType<LagEvent> getType() {
        return TYPE;
    }
}
