package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class FakeTeleportEvent extends AbstractInstantEvent {
    private static final List<Supplier<Event>> TELEPORT_EVENTS = Arrays.asList(CloseRandomTPEvent::new, FarRandomTPEvent::new, SkyBlockEvent::new, SkyEvent::new, Teleport0Event::new, TeleportHeavenEvent::new);
    public static final int TICKS_UNTIL_TELEPORT_BACK = 100;
    final Map<ServerPlayer,TeleportInfo> originalPositions = new HashMap<>();
    Event teleportEvent = TELEPORT_EVENTS.get(new Random().nextInt(TELEPORT_EVENTS.size())).get();
    int ticksAfterFirstTeleport = 0;

    @Override
    public void init() {
        savePositions(originalPositions);
        teleportEvent.init();
    }

    @Override
    public void tick() {
        if(!teleportEvent.hasEnded())
            teleportEvent.tick();
        else if(++ticksAfterFirstTeleport == TICKS_UNTIL_TELEPORT_BACK)
            loadPositions(originalPositions);
    }

    @Override
    public void tickClient() {
        if(!teleportEvent.hasEnded())
            teleportEvent.tickClient();
        else
            ticksAfterFirstTeleport++;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void renderQueueItem(GuiGraphics drawContext, float tickdelta, int x, int y) {
        if(hasEnded())
            super.renderQueueItem(drawContext, tickdelta, x, y);
        else
            teleportEvent.renderQueueItem(drawContext, tickdelta, x, y);
    }

    @Override
    public boolean hasEnded() {
        return teleportEvent.hasEnded() && ticksAfterFirstTeleport > TICKS_UNTIL_TELEPORT_BACK;
    }

    @Override
    public Optional<String> getExtraData() {
        return Optional.of(EventRegistry.getEventId(teleportEvent));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void readExtraData(String id) {
        teleportEvent = EventRegistry.get(id);
    }

    public static void savePositions(Map<ServerPlayer,TeleportInfo> positions) {
        for(ServerPlayer player : Entropy.getInstance().eventHandler.getActivePlayers()) {
            BlockPos pos = player.blockPosition();

            positions.put(player, new TeleportInfo(pos.getX(), pos.getY(), pos.getZ(), player.getYHeadRot(), player.getXRot()));
        }
    }

    public static void loadPositions(Map<ServerPlayer,TeleportInfo> positions) {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            TeleportInfo info = positions.get(player);

            player.fallDistance = 0;
            player.teleportTo(player.serverLevel(), info.x + 0.5D, info.y, info.z + 0.5D, info.yaw, info.pitch);
        });
    }

    public static record TeleportInfo(int x, int y, int z, float yaw, float pitch) {}
}
