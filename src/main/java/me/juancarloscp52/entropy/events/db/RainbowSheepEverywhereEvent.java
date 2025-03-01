package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;

public class RainbowSheepEverywhereEvent extends AbstractTimedEvent {
    @Override
    public void initClient() {
        Variables.rainbowSheepEverywhere = true;
    }

    @Override
    public void endClient() {
        super.endClient();
        Variables.rainbowSheepEverywhere = false;
    }

}
