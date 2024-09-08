package sfiomn.legendarysurvivaloverhaul.common.world.gen;

import net.minecraft.world.gen.blockplacer.DoublePlantBlockPlacer;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.FeatureRegistry;

public class ModConfiguredFeatures {

    public static final ConfiguredFeature<?, ?> SUN_FERN_CONFIG = FeatureRegistry.FERN.get()
            .configured((new BlockClusterFeatureConfig.Builder(
                    new SimpleBlockStateProvider(BlockRegistry.SUN_FERN_CROP.get().defaultBlockState()), SimpleBlockPlacer.INSTANCE))
                    .tries(1)
                    .canReplace()
                    .build())
            .count(FeatureSpread.of(5, 3))
            .decorated(Placement.CHANCE.configured(new ChanceConfig(Config.Baked.sunFernSpawnChance)));

    public static final ConfiguredFeature<?, ?> ICE_FERN_CONFIG = FeatureRegistry.FERN.get()
            .configured((new BlockClusterFeatureConfig.Builder(
                    new SimpleBlockStateProvider(BlockRegistry.ICE_FERN_CROP.get().defaultBlockState()), SimpleBlockPlacer.INSTANCE))
                    .tries(1)
                    .canReplace()
                    .build())
            .count(FeatureSpread.of(5, 3))
            .decorated(Placement.CHANCE.configured(new ChanceConfig(Config.Baked.iceFernSpawnChance)));


    public static final ConfiguredFeature<?, ?> WATER_PLANT_CONFIG = FeatureRegistry.WATER_PLANT.get()
            .configured((new BlockClusterFeatureConfig.Builder(
                    new SimpleBlockStateProvider(BlockRegistry.WATER_PLANT_CROP.get().defaultBlockState()), DoublePlantBlockPlacer.INSTANCE))
                    .tries(1)
                    .canReplace()
                    .build())
            .count(FeatureSpread.of(3, 5))
            .decorated(Placement.CHANCE.configured(new ChanceConfig(Config.Baked.waterPlantSpawnChance)));
}
