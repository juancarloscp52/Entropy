package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

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

        Minecraft mc = Minecraft.getInstance();
        this.hadScreenOpenLastTick = this.hasScreenOpen;
        this.hasScreenOpen = mc.screen != null && !(mc.screen instanceof CreativeModeInventoryScreen || mc.screen instanceof InventoryScreen);

        //screen was closed
        if(this.hadScreenOpenLastTick && !this.hasScreenOpen) {
            //no automatic interaction for half a second after closing the screen
            this.afterScreenClosedCooldown = 10;
            mc.options.keyUse.setDown(false);
        }
        else
            mc.options.keyUse.setDown(true);
    }

    @Override
    public void endClient() {
        super.endClient();
        Minecraft.getInstance().options.keyUse.setDown(false);
    }

    @Override
    public String type() {
        return "use";
    }
}
