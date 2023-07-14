package me.juancarloscp52.entropy.events.db;

import org.joml.Quaterniond;
import org.joml.Vector3f;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.EntityTypeTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.server.ConstantColorDustParticleEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

public class RainbowTrailsEvent extends AbstractTimedEvent {
    @Override
    public void tickClient() {
        super.tickClient();
        float xOffset = (MathHelper.sin(tickCount) + 1.0F) / 2.0F;
        float yOffset = (MathHelper.cos(tickCount) + 1.0F) / 2.0F;
        Vector3f color = HSBtoRGB(((tickCount * 5) % 360) / 360.0F, 1.0F, 1.0F);

        MinecraftClient.getInstance().player.clientWorld.getEntities().forEach(entity -> {
            if(entity.getType().isIn(EntityTypeTags.NO_RAINBOW_TRAIL))
                return;

            Quaterniond relativePosition = new Quaterniond(-0.5D + xOffset, 0.0D, -0.5D, 0.0D);
            float bodyYaw = entity.getBodyYaw();
            float rotation = (Math.abs(bodyYaw) % 360) * MathHelper.RADIANS_PER_DEGREE * 2.0F;

            if(bodyYaw > 0)
                rotation *= -1;

            relativePosition.rotateLocalY(rotation);
            entity.getWorld().addParticle(new ConstantColorDustParticleEffect(color, 1.0F),
                    entity.getX() + relativePosition.x,
                    entity.getY() + 0.5D + relativePosition.y + yOffset,
                    entity.getZ() + relativePosition.z,
                    0.0D, 0.0D, 0.0D);
        });
    }

    @Override
    public void render(DrawContext drawContext, float tickdelta) {}

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }

    private Vector3f HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0;
        int g = 0;
        int b = 0;

        if (saturation == 0)
            r = g = b = (int) (brightness * 255.0F + 0.5F);
        else {
            float h = (hue - (float) Math.floor(hue)) * 6.0F;
            float f = h - (float) Math.floor(h);
            float p = brightness * (1.0F - saturation);
            float q = brightness * (1.0F - saturation * f);
            float t = brightness * (1.0F - (saturation * (1.0F - f)));

            switch ((int) h) {
                case 0:
                    r = (int) (brightness * 255.0F + 0.5F);
                    g = (int) (t * 255.0F + 0.5F);
                    b = (int) (p * 255.0F + 0.5F);
                    break;
                case 1:
                    r = (int) (q * 255.0F + 0.5F);
                    g = (int) (brightness * 255.0F + 0.5F);
                    b = (int) (p * 255.0F + 0.5F);
                    break;
                case 2:
                    r = (int) (p * 255.0F + 0.5F);
                    g = (int) (brightness * 255.0F + 0.5F);
                    b = (int) (t * 255.0F + 0.5F);
                    break;
                case 3:
                    r = (int) (p * 255.0F + 0.5F);
                    g = (int) (q * 255.0F + 0.5F);
                    b = (int) (brightness * 255.0F + 0.5F);
                    break;
                case 4:
                    r = (int) (t * 255.0F + 0.5F);
                    g = (int) (p * 255.0F + 0.5F);
                    b = (int) (brightness * 255.0F + 0.5F);
                    break;
                case 5:
                    r = (int) (brightness * 255.0F + 0.5F);
                    g = (int) (p * 255.0F + 0.5F);
                    b = (int) (q * 255.0F + 0.5F);
                    break;
            }
        }

        return new Vector3f(r / 255.0F, g / 255.0F, b / 255.0F);
    }
}
