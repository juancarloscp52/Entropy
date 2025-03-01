package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.fabric.mixin.client.keybinding.KeyBindingAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Key;

public class NoUseKeyEvent extends AbstractTimedEvent {
    private Key boundUseKey;

    @Override
    public void initClient() {
        GameOptions options = MinecraftClient.getInstance().options;

        boundUseKey = ((KeyBindingAccessor) options.useKey).fabric_getBoundKey();
        options.useKey.setBoundKey(InputUtil.UNKNOWN_KEY);
        options.useKey.setPressed(false);
        KeyBinding.updateKeysByCode();
    }

    @Override
    public void endClient() {
        GameOptions options = MinecraftClient.getInstance().options;

        options.setKeyCode(options.useKey, boundUseKey);
        KeyBinding.updateKeysByCode();
        super.endClient();
    }

    @Override
    public String type() {
        return "use";
    }
}
