package sfiomn.legendarysurvivaloverhaul.common.world.gen;

import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.placement.NoiseDependant;
import net.minecraft.world.gen.placement.Placement;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;

public class ModConfiguredFeatures {

    public static final ConfiguredFeature<?, ?> SUN_FERN_CONFIG = Feature.FLOWER
            .configured((new BlockClusterFeatureConfig.Builder(
                    new SimpleBlockStateProvider(BlockRegistry.SUN_FERN.get().defaultBlockState()), SimpleBlockPlacer.INSTANCE))
                    .tries(48)
                    .build())
            .decorated(Features.Placements.ADD_32)
            .decorated(Features.Placements.HEIGHTMAP)
            .squared()
            .decorated(Placement.COUNT_NOISE.configured(new NoiseDependant(-0.8D, 15, 3)));

    public static final ConfiguredFeature<?, ?> ICE_FERN_CONFIG = Feature.FLOWER
            .configured((new BlockClusterFeatureConfig.Builder(
                    new SimpleBlockStateProvider(BlockRegistry.ICE_FERN.get().defaultBlockState()), SimpleBlockPlacer.INSTANCE))
                    .tries(48)
                    .build())
            .decorated(Features.Placements.ADD_32)
            .decorated(Features.Placements.HEIGHTMAP)
            .squared()
            .decorated(Placement.COUNT_NOISE.configured(new NoiseDependant(-0.8D, 15, 3)));
}
