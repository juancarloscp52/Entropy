package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;

public class OnePunchEvent extends AbstractTimedEvent {

    @Override
    public void init() {
        Variables.shouldLaunchEntity++;
        Variables.isOnePunchActivated++;
    }

    @Override
    public void end() {
        Variables.shouldLaunchEntity--;
        Variables.isOnePunchActivated--;
        super.end();
    }
}
