package me.juancarloscp52.entropy.mixin;

import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractSelectionList.class)
public interface EntryListWidgetAccessor {

    @Accessor
    boolean getScrolling();

}