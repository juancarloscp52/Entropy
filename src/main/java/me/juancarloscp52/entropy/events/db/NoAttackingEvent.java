package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.fabric.mixin.client.keybinding.KeyBindingAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Key;

public class NoAttackingEvent extends AbstractTimedEvent {
    private Key boundAttackKey;

    @Override
    public void initClient() {
        GameOptions options = MinecraftClient.getInstance().options;

        boundAttackKey = ((KeyBindingAccessor) options.attackKey).fabric_getBoundKey();
        options.attackKey.setBoundKey(InputUtil.UNKNOWN_KEY);
        options.attackKey.setPressed(false);
        KeyBinding.updateKeysByCode();
    }

    @Override
    public void endClient() {
        GameOptions options = MinecraftClient.getInstance().options;

        options.setKeyCode(options.attackKey, boundAttackKey);
        KeyBinding.updateKeysByCode();
        this.hasEnded = true;
    }

    @Override
    public String type() {
        return "attack";
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
