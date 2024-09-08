package sfiomn.legendarysurvivaloverhaul.common.world.gen;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.List;
import java.util.function.Supplier;

public class ModPlantGeneration {

    public static void generatePlants(final BiomeLoadingEvent event) {

        ResourceLocation biomeName = event.getName();
        Biome.Category biomeCategory = event.getCategory();

        boolean canIceFernSpawn = false;
        boolean canSunFernSpawn = false;
        boolean canWaterPlantSpawn = false;

        if (biomeName != null) {
            canIceFernSpawn = Config.Baked.iceFernBiomeNames.contains(biomeName.toString());
            canSunFernSpawn = Config.Baked.sunFernBiomeNames.contains(biomeName.toString());
            canWaterPlantSpawn = Config.Baked.waterPlantBiomeNames.contains(biomeName.toString());
        }

        //  biomeCategory.getName() returns the lowercase name of the category
        //  biomeCategory.toString() is the normal uppercase name of the category
        if (!canIceFernSpawn)
            canIceFernSpawn = Config.Baked.iceFernBiomeCategories.contains(biomeCategory.getName()) ||
                    Config.Baked.iceFernBiomeCategories.contains(biomeCategory.toString());

        if (!canSunFernSpawn)
            canSunFernSpawn = Config.Baked.sunFernBiomeCategories.contains(biomeCategory.getName()) ||
                    Config.Baked.sunFernBiomeCategories.contains(biomeCategory.toString());

        if (!canWaterPlantSpawn)
            canWaterPlantSpawn = Config.Baked.waterPlantBiomeCategories.contains(biomeCategory.getName()) ||
                    Config.Baked.waterPlantBiomeCategories.contains(biomeCategory.toString());

        if (canIceFernSpawn) {
            LegendarySurvivalOverhaul.LOGGER.debug("Generate ice fern in biome " + biomeName);
            List<Supplier<ConfiguredFeature<?, ?>>> base = event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);

            base.add(() -> ModConfiguredFeatures.ICE_FERN_CONFIG);
        }

        if (canSunFernSpawn) {
            LegendarySurvivalOverhaul.LOGGER.debug("Generate sun fern in biome " + biomeName);
            List<Supplier<ConfiguredFeature<?, ?>>> base = event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);

            base.add(() -> ModConfiguredFeatures.SUN_FERN_CONFIG);
        }

        if (canWaterPlantSpawn) {
            LegendarySurvivalOverhaul.LOGGER.debug("Generate water plant in biome " + biomeName);
            List<Supplier<ConfiguredFeature<?, ?>>> base = event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);

            base.add(() -> ModConfiguredFeatures.WATER_PLANT_CONFIG);
        }
    }
}
