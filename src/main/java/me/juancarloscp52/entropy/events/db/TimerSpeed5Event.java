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

public class TimerSpeed5Event extends AbstractTimedEvent {
    public static final EventType<TimerSpeed5Event> TYPE = EventType.builder(TimerSpeed5Event::new).category(EventCategory.TIMER).build();

    @Override
    public void initClient() {
        Variables.timerMultiplier = 5;
    }

    @Override
    public void endClient() {
        Variables.timerMultiplier = 1;
        super.endClient();
    }

    @Override
    public void init() {
        Variables.timerMultiplier = 5;
    }

    @Override
    public void end() {
        Variables.timerMultiplier = 1;
        super.end();
    }

    @Override
    public short getDuration() {
        return (short) (super.getDuration()*0.75f);
    }

    @Override
    public EventType<TimerSpeed5Event> getType() {
        return TYPE;
    }
}
