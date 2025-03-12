/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;

public class InvisibleHostileMobsEvent extends InvisibleEveryoneEvent {
    public static final EventType<InvisibleHostileMobsEvent> TYPE = EventType.builder(InvisibleHostileMobsEvent::new).category(EventCategory.INVISIBILITY).build();

    @Override
    public boolean shouldBeInvisible(Entity entity) {
        return entity instanceof Enemy && super.shouldBeInvisible(entity);
    }

    @Override
    public EventType<InvisibleHostileMobsEvent> getType() {
        return TYPE;
    }
}
