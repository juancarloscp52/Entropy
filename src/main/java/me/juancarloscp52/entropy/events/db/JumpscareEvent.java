package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;

public class JumpscareEvent extends AbstractInstantEvent {
    public static final EventType<JumpscareEvent> TYPE = EventType.builder(JumpscareEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.GUARDIAN_ELDER_EFFECT, 1.0f)));
    }

    @Override
    public EventType<JumpscareEvent> getType() {
        return TYPE;
    }
}
