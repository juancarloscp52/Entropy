package me.juancarloscp52.entropy.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;

public class EntropyDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        Pack pack = fabricDataGenerator.createPack();
        EntropyBlockTagProvider blockTagProvider = pack.addProvider(EntropyBlockTagProvider::new);

        pack.addProvider(EntropyEnchantmentTagProvider::new);
        pack.addProvider(EntropyEntityTypeTagProvider::new);
        pack.addProvider((output, completableFuture) -> new EntropyItemTagProvider(output, completableFuture, blockTagProvider));
    }
}
