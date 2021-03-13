package me.juancarloscp52.entropy.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.juancarloscp52.entropy.client.Screens.EntropyConfigurationScreen;
import me.juancarloscp52.entropy.client.Screens.EntropyErrorScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;


public class EntropyModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent)-> {
            if(MinecraftClient.getInstance().getGame().getCurrentSession()==null){
                return new EntropyConfigurationScreen(parent);
            }else{
                return new EntropyErrorScreen(parent,new LiteralText("Entropy settings cannot be opened during gameplay. Please close your world or disconnect from the current server before editing entropy settings."));
            }

        };
    }


}
