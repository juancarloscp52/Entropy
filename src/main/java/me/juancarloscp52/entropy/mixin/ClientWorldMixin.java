package me.juancarloscp52.entropy.mixin;

import me.juancarloscp52.entropy.client.EntropyClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    @Inject(method = "disconnect", at = @At("HEAD"))
    private void onDisconnect(CallbackInfo ci) {
        if (EntropyClient.getInstance().clientEventHandler == null)
            return;
        EntropyClient.getInstance().clientEventHandler.endChaos();
        EntropyClient.getInstance().clientEventHandler = null;
    }

}
