package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;

public class InfiniteLavaEvent extends AbstractTimedEvent {
    @Override
    public void init() {
        MinecraftServer server = Entropy.getInstance().eventHandler.server;
        server.getGameRules().get(GameRules.LAVA_SOURCE_CONVERSION).set(true, server);
    }

    @Override
    public void end() {
        MinecraftServer server = Entropy.getInstance().eventHandler.server;
        server.getGameRules().get(GameRules.LAVA_SOURCE_CONVERSION).set(false, server);
        super.end();
    }
}
