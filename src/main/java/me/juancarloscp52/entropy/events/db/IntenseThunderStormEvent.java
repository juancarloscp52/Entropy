package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;

import java.util.Random;

public class IntenseThunderStormEvent extends AbstractTimedEvent {

    Random random;
    public IntenseThunderStormEvent() {
        this.translationKey="entropy.events.IntenseThunder";
    }

    @Override
    public void init() {
        random=new Random();
        Entropy.getInstance().eventHandler.server.getOverworld().setWeather(0,this.getDuration(),true,true);
    }

    @Override
    public void end() {
        this.hasEnded=true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}

    @Override
    public void tick() {

        if(getTickCount() % 10 == 0){
            PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> {
                LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(serverPlayerEntity.getServerWorld());
                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofCenter(serverPlayerEntity.getServerWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING,new BlockPos(serverPlayerEntity.getX()+(random.nextInt(50)-25),serverPlayerEntity.getY()+50+(random.nextInt(10)-5),serverPlayerEntity.getZ()+(random.nextInt(60)-25)))));
                serverPlayerEntity.getServerWorld().spawnEntity(lightningEntity);
            });
        }
        super.tick();
    }

    @Override
    public String type() {
        return "rain";
    }

    @Override
    public short getDuration() {
        return (short)(Entropy.getInstance().settings.baseEventDuration *2);
    }
}
