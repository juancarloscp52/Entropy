package me.juancarloscp52.entropy.events;

import java.util.ArrayList;
import java.util.List;

import me.juancarloscp52.entropy.Entropy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;

public abstract class AbstractMultiEvent implements Event {
    private List<Event> events = List.of();
    private short tickCount = 0;
    private boolean ended = false;
    private short duration = Short.MAX_VALUE;

    @Override
    public void init() {
        events = selectEvents();
        events.forEach(Event::init);
        updateDuration();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void initClient() {
        updateDuration();
        events.forEach(event -> {
            if(!(event.isDisabledByAccessibilityMode() && Entropy.getInstance().settings.accessibilityMode))
                event.initClient();
        });
    }

    @Override
    public void end() {
        events.forEach(event -> {
            if(!event.hasEnded())
                event.end();
        });
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void endClient() {
        events.forEach(event -> {
            if(!event.hasEnded())
                event.endClient();
        });
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
        events.forEach(event -> {
            if(!event.hasEnded())
                event.render(matrixStack, tickdelta);
        });
    }

    @Override
    public void renderQueueItem(MatrixStack matrixStack, float tickdelta, int x, int y) {
        MinecraftClient client = MinecraftClient.getInstance();
        int barLeft = client.getWindow().getScaledWidth() - 39;

        DrawableHelper.fill(matrixStack, barLeft, y, barLeft + 2, y + events.size() * 13 - 5, 0xFFFFFFFF);

        for(int i = 0; i < events.size(); i++) {
            Event event = events.get(i);

            event.renderQueueItem(matrixStack, tickdelta, x, y);
            matrixStack.translate(0, 13, 0);
        }

        matrixStack.translate(0, -13, 0);
    }

    @Override
    public void tick() {
        events.forEach(event -> {
            if(!event.hasEnded())
                event.tick();
        });

        if(++tickCount >= getDuration())
            end();
    }

    @Override
    public void tickClient() {
        events.forEach(event -> {
            if(!event.hasEnded())
                event.tickClient();
        });

        if(++tickCount >= getDuration())
            endClient();
    }

    @Override
    public short getTickCount() {
        return tickCount;
    }

    @Override
    public void setTickCount(short tickCount) {
        this.tickCount = tickCount;
    }

    @Override
    public short getDuration() {
        return duration;
    }

    public void updateDuration()  {
        short longestDuration = 0;

        for(Event event : events) {
            short eventDuration = event.getDuration();

            if(eventDuration > longestDuration)
                longestDuration = eventDuration;
        }

        duration = longestDuration;
    }

    @Override
    public boolean hasEnded() {
        return ended || events.stream().allMatch(Event::hasEnded);
    }

    @Override
    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    @Override
    public void writeExtraData(PacketByteBuf buf) {
        buf.writeInt(events.size());
        events.forEach(event -> buf.writeString(EventRegistry.getEventId(event)));
        events.forEach(event -> event.writeExtraData(buf));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void readExtraData(PacketByteBuf buf) {
        int eventCount = buf.readInt();

        events = new ArrayList<>();

        for(int i = 0; i < eventCount; i++) {
            events.add(EventRegistry.get(buf.readString()));
        }

        events.forEach(event -> event.readExtraData(buf));
    }

    public abstract List<Event> selectEvents();

    public List<Event> selectedEvents() {
        return events;
    }
}
