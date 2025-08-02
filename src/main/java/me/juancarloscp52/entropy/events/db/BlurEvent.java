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

public class BlurEvent extends AbstractTimedEvent {
    public static final EventType<BlurEvent> TYPE = EventType.builder(BlurEvent::new).category(EventCategory.SHADER).disabledByAccessibilityMode().build();

    @Override
    public void initClient() {
        Variables.blur = true;
    }

    @Override
    public void endClient() {
        Variables.blur = false;
        super.endClient();
    }

    @Override
    public short getDuration() {
        return (short) (super.getDuration()*1.5f);
    }

    @Override
    public EventType<BlurEvent> getType() {
        return TYPE;
    }
}
