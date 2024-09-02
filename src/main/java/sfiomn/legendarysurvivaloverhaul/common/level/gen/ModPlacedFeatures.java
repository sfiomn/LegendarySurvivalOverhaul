package sfiomn.legendarysurvivaloverhaul.common.level.gen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> ICE_FERN_PLACED_KEY = registerKey("ice_fern_placed");
    public static final ResourceKey<PlacedFeature> SUN_FERN_PLACED_KEY = registerKey("sun_fern_placed");
    public static final ResourceKey<PlacedFeature> WATER_PLANT_PLACED_KEY= registerKey("water_plant_placed");

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, name));
    }

    public static List<PlacementModifier> worldSurfaceWithCountAndChance(int minCount, int maxCount, int chanceOnceEvery) {
        return List.of(
                RarityFilter.onAverageOnceEvery(chanceOnceEvery),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome(),
                CountPlacement.of(UniformInt.of(minCount, maxCount)));
    }

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        PlacementUtils.register(context,
                ICE_FERN_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.ICE_FERN_CONFIG_KEY),
                worldSurfaceWithCountAndChance(4, 7, 20));

        PlacementUtils.register(context,
                SUN_FERN_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.SUN_FERN_CONFIG_KEY),
                worldSurfaceWithCountAndChance(4, 7, 20));

        PlacementUtils.register(context,
                WATER_PLANT_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.WATER_PLANT_CONFIG_KEY),
                worldSurfaceWithCountAndChance(3, 5, 20));
    }

}
