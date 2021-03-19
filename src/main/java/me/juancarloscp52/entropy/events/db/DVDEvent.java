package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class DVDEvent extends AbstractTimedEvent {

    double x = 0;
    double y = 0;
    double velX=0;
    double velY=0;
    int size = 150;
    Random random = new Random();
    MinecraftClient client;
    public DVDEvent() {
        this.translationKey="entropy.events.DVD";
    }

    @Override
    public void initClient() {
        velX=random.nextDouble()*4+0.9d;
        velY=random.nextDouble()*4+0.9d;
        client=MinecraftClient.getInstance();
    }

    @Override
    public void endClient() {
        this.hasEnded=true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
        renderDVDOverlay(matrixStack, tickdelta);
    }

    @Override
    public void tickClient() {
        y+=velY;
        x+=velX;
        int height= client.getWindow().getScaledHeight();
        int width= client.getWindow().getScaledWidth();
        if(y+size>height || y<0){
            y=MathHelper.clamp(y,0,height-size);
            velY= (velY>0? -1:1)*(random.nextDouble()*2+0.8d);
        }
        if(x+size > width || x < 0){
            x=MathHelper.clamp(x,0,width-size);
            velX= (velX>0? -1:1)*(random.nextDouble()*2+0.8d);
        }

        super.tickClient();
    }

    @Override
    public String type() {
        return "DVD";
    }

    @Override
    public short getDuration() {
        return (short)(Entropy.getInstance().settings.baseEventDuration);
    }

    private void renderDVDOverlay(MatrixStack matrixStack, float tickdelta) {
        if(client==null)
            return;
        int height= client.getWindow().getScaledHeight();
        int width= client.getWindow().getScaledWidth();
        int topSize = MathHelper.floor(y);
        int leftSize = MathHelper.floor(x);
        int bottomSize = MathHelper.floor(y+size);
        int rightSize = MathHelper.floor(x+size);
        DrawableHelper.fill(matrixStack,0,0,width,topSize,MathHelper.packRgb(0,0,0)+ 255<<24);
        DrawableHelper.fill(matrixStack,0,0,leftSize,height,MathHelper.packRgb(0,0,0)+ 255<<24);
        DrawableHelper.fill(matrixStack,0,height,width,bottomSize,MathHelper.packRgb(0,0,0)+ 255<<24);
        DrawableHelper.fill(matrixStack,width,0,rightSize,height,MathHelper.packRgb(0,0,0)+ 255<<24);

    }

}
