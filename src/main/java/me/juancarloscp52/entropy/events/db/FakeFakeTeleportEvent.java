package me.juancarloscp52.entropy.events.db;

import java.util.HashMap;
import java.util.Map;

import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.db.FakeTeleportEvent.TeleportInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class FakeFakeTeleportEvent extends AbstractInstantEvent {
    private final Map<ServerPlayerEntity,TeleportInfo> positionsBeforeFakeTeleport = new HashMap<>();
    private FakeTeleportEvent fakeTeleportEvent = new FakeTeleportEvent();
    private int ticksAfterSecondTeleport = 0;

    @Override
    public void init() {
        fakeTeleportEvent.init();
    }

    @Override
    public void tick() {
        if(!fakeTeleportEvent.teleportEvent.hasEnded())
            fakeTeleportEvent.teleportEvent.tick();
        else if(++fakeTeleportEvent.ticksAfterFirstTeleport == FakeTeleportEvent.TICKS_UNTIL_TELEPORT_BACK) {
            FakeTeleportEvent.savePositions(positionsBeforeFakeTeleport);
            FakeTeleportEvent.loadPositions(fakeTeleportEvent.originalPositions);
        }
        else if(fakeTeleportEvent.ticksAfterFirstTeleport > FakeTeleportEvent.TICKS_UNTIL_TELEPORT_BACK && ++ticksAfterSecondTeleport == FakeTeleportEvent.TICKS_UNTIL_TELEPORT_BACK)
            FakeTeleportEvent.loadPositions(positionsBeforeFakeTeleport);
    }

    @Override
    public void tickClient() {
        if(!fakeTeleportEvent.hasEnded())
            fakeTeleportEvent.tickClient();
        else
            ticksAfterSecondTeleport++;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void renderQueueItem(MatrixStack matrixStack, float tickdelta, int x, int y) {
        if(hasEnded())
            super.renderQueueItem(matrixStack, tickdelta, x, y);
        else
            fakeTeleportEvent.renderQueueItem(matrixStack, tickdelta, x, y);
    }

    @Override
    public boolean hasEnded() {
        return fakeTeleportEvent.hasEnded() && ticksAfterSecondTeleport > FakeTeleportEvent.TICKS_UNTIL_TELEPORT_BACK;
    }

    @Override
    public void writeExtraData(PacketByteBuf buf) {
        fakeTeleportEvent.writeExtraData(buf);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void readExtraData(PacketByteBuf buf) {
        fakeTeleportEvent.readExtraData(buf);
    }
}
