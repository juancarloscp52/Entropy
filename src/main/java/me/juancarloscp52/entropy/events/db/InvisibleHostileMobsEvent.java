/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.Monster;

public class InvisibleHostileMobsEvent extends InvisibleEveryoneEvent {
    @Override
    public boolean shouldBeInvisible(Entity entity) {
        return entity instanceof Monster && super.shouldBeInvisible(entity);
    }
}
