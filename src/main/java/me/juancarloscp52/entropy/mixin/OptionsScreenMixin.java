package me.juancarloscp52.entropy.mixin;

import me.juancarloscp52.entropy.client.Screens.EntropyConfigurationScreen;
import me.juancarloscp52.entropy.client.Screens.EntropyErrorScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void insertEntropySettingsButton(CallbackInfo ci) {
        this.addButton(new ButtonWidget(this.width - 100, this.height - 20, 100, 20, new TranslatableText("entropy.options.title"), button -> {
            if (MinecraftClient.getInstance().getGame().getCurrentSession() == null) {
                this.client.openScreen(new EntropyConfigurationScreen(this));
            } else {
                this.client.openScreen(new EntropyErrorScreen(this, new TranslatableText("entropy.options.error")));
            }
        }));
    }

}
