package icey.survivaloverhaul.common.world;

import java.util.List;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.registry.BlockRegistry;
import icey.survivaloverhaul.util.OreGenerationUtil;
import icey.survivaloverhaul.util.OreGenerationUtil.OreOptions;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class OreGeneration 
{
	
	@SubscribeEvent
	public static void GenerateOres(final BiomeLoadingEvent event) 
	{
		
		List<BlockState> BS = OreGenerationUtil.getBlockState();
		List<OreOptions> LOO = OreGenerationUtil.getOreOptions();
		
		for (int i =0; i<BS.size(); i++) 
		{
			OreOptions OO = LOO.get(i);
			boolean noload = false;
			for(int c=0; c<OO.Biomes.length;c++)
				if (event.getCategory() == OO.Biomes[c])
					noload = true;
			if (noload == false)// might make it so you can apply your own builder for each block else apply default builder  
			{
				event.getGeneration().withFeature(OO.GS, Feature.ORE
						.withConfiguration(new OreFeatureConfig(OO.FBT, BS.get(i), OO.veinSize))
						.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(OO.minHeight, 0, OO.maxHeight))).square()
						.func_242731_b(OO.amount));
				Main.LOGGER.debug("build: "+ event.getCategory() + "\nOre: " + BS.get(i).getBlock().getRegistryName());
			}
			
		}
	}
	public static void register() //could move this
	{
			OreGenerationUtil.add(BlockRegistry.CINNABAR_ORE.get());
	}
}
