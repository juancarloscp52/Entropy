package me.juancarloscp52.entropy.events.db;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;
import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.fabric.mixin.client.keybinding.KeyBindingAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

public class NoAttackingEvent extends AbstractTimedEvent {
    private Key boundAttackKey;

    @Override
    public void initClient() {
        Options options = Minecraft.getInstance().options;

        boundAttackKey = ((KeyBindingAccessor) options.keyAttack).fabric_getBoundKey();
        options.keyAttack.setKey(InputConstants.UNKNOWN);
        options.keyAttack.setDown(false);
        KeyMapping.resetMapping();
    }

    @Override
    public void endClient() {
        Options options = Minecraft.getInstance().options;

        options.keyAttack.setKey(boundAttackKey);
        KeyMapping.resetMapping();
        super.endClient();
    }

    @Override
    public String type() {
        return "attack";
    }
}
