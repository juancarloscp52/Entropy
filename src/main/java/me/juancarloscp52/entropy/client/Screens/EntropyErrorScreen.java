package me.juancarloscp52.entropy.client.Screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.List;

public class EntropyErrorScreen extends Screen {
    Screen parent;
    Text message;

    public EntropyErrorScreen(Screen parent, Text message) {
        super(new TranslatableText("entropy.errorScreen.title"));
        this.message = message;
        this.parent = parent;
    }


    @Override
    protected void init() {
        super.init();
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 40, 200, 20, ScreenTexts.BACK,
                button -> onClose()));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        List<OrderedText> lines = textRenderer.wrapLines(message, this.width / 2);
        for (int i = 0; i < lines.size(); i++) {
            OrderedText line = lines.get(i);
            textRenderer.drawWithShadow(matrices, line, this.width / 4f, this.height / 2f - (lines.size() * 9 / 2f) + i * 9, 16777215);
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        this.client.openScreen(this.parent);
    }
}
