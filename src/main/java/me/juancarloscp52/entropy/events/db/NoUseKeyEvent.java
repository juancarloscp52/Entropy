package me.juancarloscp52.entropy.events.db;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;
import net.fabricmc.fabric.mixin.client.keybinding.KeyBindingAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

public class NoUseKeyEvent extends AbstractTimedEvent {
    public static final EventType<NoUseKeyEvent> TYPE = EventType.builder(NoUseKeyEvent::new).category(EventCategory.USE).build();
    private Key boundUseKey;

    @Override
    public void initClient() {
        Options options = Minecraft.getInstance().options;

        boundUseKey = ((KeyBindingAccessor) options.keyUse).fabric_getBoundKey();
        options.keyUse.setKey(InputConstants.UNKNOWN);
        options.keyUse.setDown(false);
        KeyMapping.resetMapping();
    }

    @Override
    public void endClient() {
        Options options = Minecraft.getInstance().options;

        options.keyUse.setKey(boundUseKey);
        KeyMapping.resetMapping();
        super.endClient();
    }

    @Override
    public EventType<NoUseKeyEvent> getType() {
        return TYPE;
    }
}
