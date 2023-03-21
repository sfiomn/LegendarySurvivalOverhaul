package sfiomn.legendarysurvivaloverhaul.world.gen;

import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;

public class ModConfiguredFeatures {

    public static final ConfiguredFeature<?, ?> SUN_FERN_CONFIG = Feature.FLOWER
            .configured((new BlockClusterFeatureConfig.Builder(
                    new SimpleBlockStateProvider(BlockRegistry.SUN_FERN.get().defaultBlockState()), SimpleBlockPlacer.INSTANCE))
                    .tries(2)
                    .build())
            .decorated(Features.Placements.HEIGHTMAP_WORLD_SURFACE)
            .count(1);

    public static final ConfiguredFeature<?, ?> ICE_FERN_CONFIG = Feature.FLOWER
            .configured((new BlockClusterFeatureConfig.Builder(
                    new SimpleBlockStateProvider(BlockRegistry.ICE_FERN.get().defaultBlockState()), SimpleBlockPlacer.INSTANCE))
                    .tries(2)
                    .build())
            .decorated(Features.Placements.HEIGHTMAP_WORLD_SURFACE)
            .count(1);
}
