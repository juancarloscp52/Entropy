package me.juancarloscp52.entropy.events;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.server.network.ServerPlayerEntity;


public interface Event {

    default void init() {}
    @Environment(EnvType.CLIENT)
    default void initClient() {}
    void end();
    @Environment(EnvType.CLIENT)
    void endClient();
    default void endPlayer(ServerPlayerEntity player){}
    @Environment(EnvType.CLIENT)
    void render(MatrixStack matrixStack, float tickdelta);
    @Environment(EnvType.CLIENT)
    void renderQueueItem(MatrixStack matrixStack, float tickdelta, int x, int y);
    void tick();
    @Environment(EnvType.CLIENT)
    void tickClient();
    short getTickCount();
    void setTickCount(short index);
    short getDuration();
    boolean hasEnded();
    void setEnded(boolean ended);
    String getTranslationKey();
    default String type (){
        return "none";
    }



}
