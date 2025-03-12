package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;

public class SilenceEvent extends AbstractTimedEvent {
    public static final EventType<SilenceEvent> TYPE = EventType.builder(SilenceEvent::new).build();
    private double previousVolume;
    private boolean wereSubtitlesActive;

    @Override
    public void initClient() {
        var options = Minecraft.getInstance().options;
        var master = options.getSoundSourceOptionInstance(SoundSource.MASTER);

        previousVolume = master.get();
        master.set(0.0D);
        wereSubtitlesActive = options.showSubtitles().get();
        options.showSubtitles().set(false);
    }

    @Override
    public void endClient() {
        var options = Minecraft.getInstance().options;

        options.getSoundSourceOptionInstance(SoundSource.MASTER).set(previousVolume);
        options.showSubtitles().set(wereSubtitlesActive);
        super.endClient();
    }

    @Override
    public EventType<SilenceEvent> getType() {
        return TYPE;
    }
}
