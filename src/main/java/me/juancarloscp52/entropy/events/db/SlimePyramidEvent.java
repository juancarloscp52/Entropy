/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;

public class SlimePyramidEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for (var player : Entropy.getInstance().eventHandler.getActivePlayers()) {

            var slimeSize = player.getRandom().nextInt(4) + 4;

            var angle = player.getRandom().nextDouble() * Math.PI * 2;
            var cos = Math.cos(angle);
            var sin = Math.sin(angle);
            var addX = cos * 5;
            var addY = sin * 5;

            var slime = new Slime(EntityType.SLIME, player.level());
            slime.setSize(slimeSize, true);
            slime.setPos(player.position().add(addX, 0, addY));
            player.level().addFreshEntity(slime);

            while (slimeSize > 1) {
                slimeSize--;

                var slime2 = new Slime(EntityType.SLIME, player.level());
                slime2.setSize(slimeSize, true);
                slime2.setPos(player.position().add(addX, 0, addY));
                player.level().addFreshEntity(slime2);

                slime2.startRiding(slime, true);
                slime = slime2;
            }
        }
    }
}
