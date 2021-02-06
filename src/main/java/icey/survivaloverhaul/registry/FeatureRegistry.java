package icey.survivaloverhaul.registry;

import icey.survivaloverhaul.Main;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class FeatureRegistry
{
	public static ConfiguredFeature<?, ?> CINNABAR_ORE_CF;
	
	public static void commonSetup(final FMLCommonSetupEvent event)
	{
		event.enqueueWork(() -> {
			CINNABAR_ORE_CF = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
					BlockRegistry.CINNABAR_ORE.get().getDefaultState(), 6)).range(48).square().func_242731_b(10);
		        
		     Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(Main.MOD_ID, "cinnabar_ore"), CINNABAR_ORE_CF);
		   }
		);
	}
	
	public static void biomeModification(final BiomeLoadingEvent event)
	{
		event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> CINNABAR_ORE_CF);
	}
}
