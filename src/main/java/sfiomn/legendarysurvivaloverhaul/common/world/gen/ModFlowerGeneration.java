package sfiomn.legendarysurvivaloverhaul.common.world.gen;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class ModFlowerGeneration {

    public static void generateFlowers(final BiomeLoadingEvent event) {

        RegistryKey<Biome> key = RegistryKey.create(Registry.BIOME_REGISTRY, Objects.requireNonNull(event.getName()));
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);

        boolean canIceFernSpawn = false;
        boolean canSunFernSpawn = false;

        if (Config.Baked.iceFernBiomeNames.contains(key.location().toString())) {
            canIceFernSpawn = true;
            // LegendarySurvivalOverhaul.LOGGER.debug("Add ice fern in biome " + key.location());
        } else {
            for (BiomeDictionary.Type type : types) {
                if (Config.Baked.iceFernBiomeTypes.contains(type.getName())) {
                    canIceFernSpawn = true;
                    // LegendarySurvivalOverhaul.LOGGER.debug("Add ice fern biome " + key.location() + " with type " + type.getName());
                }
            }
        }
        if (Config.Baked.sunFernBiomeNames.contains(key.location().toString())) {
            canSunFernSpawn = true;
            // LegendarySurvivalOverhaul.LOGGER.debug("Add sun fern in biome " + key.location());
        } else {
            for (BiomeDictionary.Type type : types) {
                if (Config.Baked.sunFernBiomeTypes.contains(type.getName())) {
                    canSunFernSpawn = true;
                    // LegendarySurvivalOverhaul.LOGGER.debug("Add sun fern biome " + key.location() + " with type " + type.getName());
                }
            }
        }

        if (canIceFernSpawn) {
            List<Supplier<ConfiguredFeature<?, ?>>> base = event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);

            base.add(() -> ModConfiguredFeatures.ICE_FERN_CONFIG);
        }
        if (canSunFernSpawn) {
            List<Supplier<ConfiguredFeature<?, ?>>> base = event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);

            base.add(() -> ModConfiguredFeatures.SUN_FERN_CONFIG);
        }
    }
}
