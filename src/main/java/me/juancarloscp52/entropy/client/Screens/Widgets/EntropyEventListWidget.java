package me.juancarloscp52.entropy.client.Screens.Widgets;

import com.google.common.collect.ImmutableList;
import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropySettings;
import me.juancarloscp52.entropy.events.EventRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

import java.util.List;

public class EntropyEventListWidget extends ElementListWidget<EntropyEventListWidget.ButtonEntry> {

    public EntropyEventListWidget(MinecraftClient minecraftClient, int i, int j, int k, int l, int m) {
        super(minecraftClient, i, j, k, l, m);
        this.centerListVertically = false;
    }

    public void addAllFromRegistry() {
        EventRegistry.entropyEvents.keySet().forEach(this::addEvent);
    }

    public int addEvent(String eventID) {
        return this.addEntry(EntropyEventListWidget.ButtonEntry.create(eventID));
    }

    public int getRowWidth() {
        return 400;
    }

    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 32;
    }


    @Environment(EnvType.CLIENT)
    public static class ButtonEntry extends ElementListWidget.Entry<EntropyEventListWidget.ButtonEntry> {
        public final CheckboxWidget button;
        public final String eventID;

        private ButtonEntry(String eventID, CheckboxWidget button) {
            this.button = button;
            this.eventID = eventID;
        }

        public static EntropyEventListWidget.ButtonEntry create(String eventID) {
            EntropySettings settings = Entropy.getInstance().settings;
            return new EntropyEventListWidget.ButtonEntry(eventID, new CheckboxWidget(0, 0, MinecraftClient.getInstance().getWindow().getScaledWidth(), 20, new TranslatableText(EventRegistry.getTranslationKey(eventID)), !settings.disabledEvents.contains(eventID)));
        }

        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            button.y = y;
            button.x = x + 32;
            button.render(matrices, mouseX, mouseY, tickDelta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.button.onPress();
            return true;
        }

        public List<? extends Element> children() {
            return ImmutableList.of(this.button);
        }
    }

}
