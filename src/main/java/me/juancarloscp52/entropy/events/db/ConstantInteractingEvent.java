package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;

public class ConstantInteractingEvent extends AbstractTimedEvent {
    private boolean hasScreenOpen = false;
    private boolean hadScreenOpenLastTick = false;
    private int afterScreenClosedCooldown = 0;

    @Override
    public void tickClient() {
        super.tickClient();

        //make sure players have time to move away from a chest or similar after they close the screen
        if(this.afterScreenClosedCooldown > 0) {
            this.afterScreenClosedCooldown--;
            return;
        }

        MinecraftClient mc = MinecraftClient.getInstance();
        this.hadScreenOpenLastTick = this.hasScreenOpen;
        this.hasScreenOpen = mc.currentScreen != null && !(mc.currentScreen instanceof AbstractInventoryScreen);

        //screen was closed
        if(this.hadScreenOpenLastTick && !this.hasScreenOpen) {
            //no automatic interaction for half a second after closing the screen
            this.afterScreenClosedCooldown = 10;
            mc.options.useKey.setPressed(false);
        }
        else
            mc.options.useKey.setPressed(true);
    }

    @Override
    public void endClient() {
        super.endClient();
        MinecraftClient.getInstance().options.useKey.setPressed(false);
    }

    @Override
    public String type() {
        return "use";
    }
}
