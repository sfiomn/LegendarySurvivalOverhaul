package sfiomn.legendarysurvivaloverhaul.data.integration;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.data.integration.providers.CuriosBlockTagProvider;
import sfiomn.legendarysurvivaloverhaul.data.integration.providers.CuriosItemTagProvider;
import sfiomn.legendarysurvivaloverhaul.data.integration.providers.CuriosProvider;
import sfiomn.legendarysurvivaloverhaul.data.providers.ModBlockTagProvider;
import sfiomn.legendarysurvivaloverhaul.data.providers.ModItemTagProvider;

import java.util.concurrent.CompletableFuture;

public final class IntegrationDataGenerators {
    public static void addIntegrationProviders(GatherDataEvent event, DataGenerator gen, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        if (LegendarySurvivalOverhaul.curiosLoaded) {
            gen.addProvider(event.includeServer(),
                    new CuriosProvider(
                            packOutput,
                            existingFileHelper,
                            lookupProvider
                    ));

            CuriosBlockTagProvider blockTagProvider = gen.addProvider(event.includeServer(),
                    new CuriosBlockTagProvider(packOutput, lookupProvider, existingFileHelper));

            gen.addProvider(event.includeServer(),
                    new CuriosItemTagProvider(
                            packOutput,
                            lookupProvider,
                            blockTagProvider.contentsGetter(),
                            existingFileHelper
                    ));
        }
    }
}
