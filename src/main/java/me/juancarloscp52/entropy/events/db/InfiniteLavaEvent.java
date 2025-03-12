package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

public class InfiniteLavaEvent extends AbstractTimedEvent {
    public static final EventType<InfiniteLavaEvent> TYPE = EventType.builder(InfiniteLavaEvent::new).build();

    @Override
    public void init() {
        MinecraftServer server = Entropy.getInstance().eventHandler.server;
        server.getGameRules().getRule(GameRules.RULE_LAVA_SOURCE_CONVERSION).set(true, server);
    }

    @Override
    public void end() {
        MinecraftServer server = Entropy.getInstance().eventHandler.server;
        server.getGameRules().getRule(GameRules.RULE_LAVA_SOURCE_CONVERSION).set(false, server);
        super.end();
    }

    @Override
    public EventType<InfiniteLavaEvent> getType() {
        return TYPE;
    }
}
