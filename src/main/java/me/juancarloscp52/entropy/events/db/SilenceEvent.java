package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;

public class SilenceEvent extends AbstractTimedEvent {
    private double previousVolume;
    private boolean wereSubtitlesActive;

    @Override
    public void initClient() {
        var options = MinecraftClient.getInstance().options;
        var master = options.getSoundVolumeOption(SoundCategory.MASTER);

        previousVolume = master.getValue();
        master.setValue(0.0D);
        wereSubtitlesActive = options.getShowSubtitles().getValue();
        options.getShowSubtitles().setValue(false);
    }

    @Override
    public void endClient() {
        var options = MinecraftClient.getInstance().options;

        options.getSoundVolumeOption(SoundCategory.MASTER).setValue(previousVolume);
        options.getShowSubtitles().setValue(wereSubtitlesActive);
        super.endClient();
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
