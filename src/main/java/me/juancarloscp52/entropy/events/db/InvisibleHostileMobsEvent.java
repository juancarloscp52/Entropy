/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;

public class InvisibleHostileMobsEvent extends InvisibleEveryoneEvent {
    @Override
    public boolean shouldBeInvisible(Entity entity) {
        return entity instanceof Enemy && super.shouldBeInvisible(entity);
    }
}
