/*
 * Copyright (c) 2021 juancarloscp52
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;

public class OnlyBackwardsEvent extends AbstractTimedEvent {
    public static final EventType<OnlyBackwardsEvent> TYPE = EventType.builder(OnlyBackwardsEvent::new).category(EventCategory.MOVEMENT).build();

    @Override
    public void initClient() {
        Variables.onlyBackwardsMovement = true;
    }

    @Override
    public void endClient() {
        Variables.onlyBackwardsMovement = false;
        super.endClient();
    }

    @Override
    public EventType<OnlyBackwardsEvent> getType() {
        return TYPE;
    }
}
