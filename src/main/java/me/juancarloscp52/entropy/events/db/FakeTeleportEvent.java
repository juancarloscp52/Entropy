package me.juancarloscp52.entropy.events.db;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class FakeTeleportEvent extends AbstractInstantEvent {
    private static final List<Supplier<Event>> TELEPORT_EVENTS = Arrays.asList(CloseRandomTPEvent::new, FarRandomTPEvent::new, SkyBlockEvent::new, Teleport0Event::new, TeleportHeavenEvent::new);
    public static final int TICKS_UNTIL_TELEPORT_BACK = 100;
    final Map<ServerPlayerEntity,TeleportInfo> originalPositions = new HashMap<>();
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
    public void renderQueueItem(MatrixStack matrixStack, float tickdelta, int x, int y) {
        if(hasEnded())
            super.renderQueueItem(matrixStack, tickdelta, x, y);
        else
            teleportEvent.renderQueueItem(matrixStack, tickdelta, x, y);
    }

    @Override
    public boolean hasEnded() {
        return teleportEvent.hasEnded() && ticksAfterFirstTeleport > TICKS_UNTIL_TELEPORT_BACK;
    }

    @Override
    public void writeExtraData(PacketByteBuf buf) {
        buf.writeString(EventRegistry.getEventId(teleportEvent));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void readExtraData(PacketByteBuf buf) {
        teleportEvent = EventRegistry.get(buf.readString());
    }

    public static void savePositions(Map<ServerPlayerEntity,TeleportInfo> positions) {
        for(ServerPlayerEntity player : Entropy.getInstance().eventHandler.getActivePlayers()) {
            BlockPos pos = player.getBlockPos();

            positions.put(player, new TeleportInfo(pos.getX(), pos.getY(), pos.getZ(), player.getHeadYaw(), player.getPitch()));
        }
    }

    public static void loadPositions(Map<ServerPlayerEntity,TeleportInfo> positions) {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            TeleportInfo info = positions.get(player);

            player.fallDistance = 0;
            player.teleport(player.getWorld(), info.x + 0.5D, info.y, info.z + 0.5D, info.yaw, info.pitch);
        });
    }

    public static record TeleportInfo(int x, int y, int z, float yaw, float pitch) {}
}
