package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.db.FakeTeleportEvent.TeleportInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerPlayer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeFakeTeleportEvent extends AbstractInstantEvent {
    private final Map<ServerPlayer,TeleportInfo> positionsBeforeFakeTeleport = new HashMap<>();
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
    public void renderQueueItem(GuiGraphics drawContext, float tickdelta, int x, int y) {
        if(hasEnded())
            super.renderQueueItem(drawContext, tickdelta, x, y);
        else
            fakeTeleportEvent.renderQueueItem(drawContext, tickdelta, x, y);
    }

    @Override
    public boolean hasEnded() {
        return fakeTeleportEvent.hasEnded() && ticksAfterSecondTeleport > FakeTeleportEvent.TICKS_UNTIL_TELEPORT_BACK;
    }

    @Override
    public Optional<String> getExtraData() {
        return fakeTeleportEvent.getExtraData();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void readExtraData(String id) {
        fakeTeleportEvent.readExtraData(id);
    }
}
