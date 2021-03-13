package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.FireballEntity;

import java.util.Random;

public class MeteorRainEvent extends AbstractTimedEvent {

    Random random;
    public MeteorRainEvent() {
        this.translationKey="entropy.events.meteorRain";
    }

    @Override
    public void init() {
        random=new Random();
    }

    @Override
    public void end() {
        this.hasEnded=true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}

    @Override
    public void tick() {

        if(getTickCount() % 20 == 0){
            for(int i = 0; i< 7; i++){
                PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> {
                    FireballEntity meteor = new FireballEntity(serverPlayerEntity.getServerWorld(),serverPlayerEntity,0,-1*(random.nextInt(4)+1),0);
                    meteor.refreshPositionAndAngles(serverPlayerEntity.getX()+(random.nextInt(100)-50),serverPlayerEntity.getY()+50+(random.nextInt(10)-5),serverPlayerEntity.getZ()+(random.nextInt(100)-50),meteor.yaw,meteor.pitch);
                    meteor.explosionPower=2;
                    serverPlayerEntity.getServerWorld().spawnEntity(meteor);
                });

            }
        }
        if(getTickCount() == getTickCount()/2){
            PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> {
                FireballEntity meteor = new FireballEntity(serverPlayerEntity.getServerWorld(),serverPlayerEntity,0,-1*(random.nextInt(4)+1),0);
                meteor.refreshPositionAndAngles(serverPlayerEntity.getX()+(random.nextInt(100)-50),serverPlayerEntity.getY()+50+(random.nextInt(10)-5),serverPlayerEntity.getZ()+(random.nextInt(100)-50),meteor.yaw,meteor.pitch);
                meteor.explosionPower=4;
                serverPlayerEntity.getServerWorld().spawnEntity(meteor);
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
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
