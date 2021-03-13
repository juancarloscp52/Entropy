package me.juancarloscp52.entropy.client.Screens;

import me.juancarloscp52.entropy.client.EntropyClient;
import me.juancarloscp52.entropy.events.Event;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.util.math.MatrixStack;

public class EntropyCreditsScreen extends CreditsScreen {
    Event currentEvent;
    public EntropyCreditsScreen(Event currentEvent) {
        super(true, () -> {});
        this.currentEvent=currentEvent;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        EntropyClient.getInstance().clientEventHandler.render(matrices,delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
