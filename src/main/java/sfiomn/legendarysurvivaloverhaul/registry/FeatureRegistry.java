package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.world.gen.feature.FernFeature;
import sfiomn.legendarysurvivaloverhaul.common.world.gen.feature.WaterPlantFeature;

public class FeatureRegistry {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, LegendarySurvivalOverhaul.MOD_ID);

    public static final RegistryObject<Feature<BlockClusterFeatureConfig>> FERN = FEATURES.register("fern", () -> new FernFeature(BlockClusterFeatureConfig.CODEC));
    public static final RegistryObject<Feature<BlockClusterFeatureConfig>> WATER_PLANT = FEATURES.register("water_plant", () -> new WaterPlantFeature(BlockClusterFeatureConfig.CODEC));

    public static void register(IEventBus eventBus){
        FEATURES.register(eventBus);
    }
}
