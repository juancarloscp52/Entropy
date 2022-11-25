/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SlimeEntity;

public class SlimePyramidEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for (var player : PlayerLookup.all(Entropy.getInstance().eventHandler.server)) {

            var slimeSize = player.getRandom().nextInt(3) + 4;

            var angle = player.getRandom().nextDouble() * Math.PI;
            var cos = Math.cos(angle);
            var sin = Math.sin(angle);

            var slime = new SlimeEntity(EntityType.SLIME, player.getWorld());
            slime.setSize(slimeSize, true);
            slime.setPosition(player.getPos().add(cos * 5, 0, sin * 5));
            player.getWorld().spawnEntity(slime);

            // > 1 coz the top slime of size 0 is always dying
            while (slimeSize > 1) {
                slimeSize--;

                var slime2 = new SlimeEntity(EntityType.SLIME, player.getWorld());
                slime2.setSize(slimeSize, true);
                player.getWorld().spawnEntity(slime2);

                slime2.startRiding(slime, true);
                slime = slime2;
            }
        }
    }
}
