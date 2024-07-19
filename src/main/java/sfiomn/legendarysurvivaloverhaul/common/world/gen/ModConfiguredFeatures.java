package sfiomn.legendarysurvivaloverhaul.common.world.gen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_FERN_CONFIG_KEY = registerKey("ice_fern_config_feature");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SUN_FERN_CONFIG_KEY = registerKey("sun_fern_config_feature");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WATER_PLANT_CONFIG_KEY = registerKey("water_plant_config_feature");

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, name));
    }

    public static void registerSimpleRandomPatchConfig(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> resourceKey, BlockState blockState) {
        FeatureUtils.register(context,
                resourceKey,
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(1, 5, 3,
                        PlacementUtils.filtered(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(BlockStateProvider.simple(blockState)),
                                BlockPredicate.replaceable())));
    }

    public static void registerDoubleRandomPatchConfig(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> resourceKey, BlockState blockState) {
        FeatureUtils.register(context,
                resourceKey,
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(5, 5, 3,
                        PlacementUtils.filtered(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(BlockStateProvider.simple(blockState)),
                                BlockPredicate.replaceable())));
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        registerSimpleRandomPatchConfig(context, ICE_FERN_CONFIG_KEY, BlockRegistry.ICE_FERN.get().defaultBlockState());
        registerSimpleRandomPatchConfig(context, SUN_FERN_CONFIG_KEY, BlockRegistry.SUN_FERN.get().defaultBlockState());
        registerSimpleRandomPatchConfig(context, WATER_PLANT_CONFIG_KEY, BlockRegistry.WATER_PLANT.get().defaultBlockState());
    }
}
