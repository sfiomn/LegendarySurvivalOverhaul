package icey.survivaloverhaul.util;

import java.util.ArrayList;
import java.util.List;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.blocks.BlockGeneric;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import net.minecraft.world.gen.feature.template.RuleTest;

public class OreGenerationUtil 
{
	static List<BlockState> BS = new ArrayList<BlockState>();//hehe
	static List<OreOptions> oreOptions = new ArrayList<OreOptions>();
	
	/**
	 * Adds block to the list of blocks using the BlockGenaric class
	 * 
	 * @param BlockGenaric
	 */
	public static void add(BlockGeneric BG) { OreGenerationUtil.add(BG.getDefaultState(), BG.OO); }
	
	/**
	 * Adds blockstate to list along with the options associated with that block
	 * 
	 * @param BS - Blockstate
	 * @param OO - OreOptions
	 */
	public static void add(BlockState BS,  OreOptions OO) 
	{
		if (BS == null)
		{
			Main.LOGGER.error("BS null");
			if (OO == null) 
				Main.LOGGER.error("OO null");
			return;
		}
		
		if (OO == null) 
		{
			Main.LOGGER.error("oo null");
			return;
		}
		
		@SuppressWarnings("unused")
		boolean N=true,E=true,D=true;
		for(int i=0; i<OO.Biomes.length;i++) 
		{
			switch (OO.Biomes[i]) 
			{
			case NETHER:
				N = false;
				break;
			case THEEND:
				E = false;
				break;
			default:
				D = false;
				break;
			}
		}
		//if (!(N == true && D == true || E == true && D == true || E == true && N == true)) //attempt to auto categorize fillerblocktype 
		//{
			//if (N == true)
				//OO.FBT = FillerBlockType.BASE_STONE_NETHER;
			//else if (D == true)
				OO.FBT = FillerBlockType.BASE_STONE_OVERWORLD;
		//}
		//else
			//Main.LOGGER.error("Cannot have multiple types of dementions selected at the moment");
			//throw new Exception("Cannot have multiple types of dementions selected at the moment");
				
		OreGenerationUtil.BS.add(BS);
		OreGenerationUtil.oreOptions.add(OO);
	} 
	
	public static List<BlockState> getBlockState()
	{
		return BS;
	}
	public static List<OreOptions> getOreOptions()
	{
		return oreOptions; 
	}
	public static class OreOptions
	{
		public Category[] Biomes;
		public Decoration GS;
		public RuleTest FBT = FillerBlockType.BASE_STONE_OVERWORLD;
		public int veinSize,minHeight,maxHeight,amount; 
		
		/**
		 * A neat way to manage the default options for ore generation
		 * 
		 * @param Biome Blacklist
		 * @param GS - where you want the ore to generate
		 * @param veinSize
		 * @param minHeight
		 * @param maxHeight
		 * @param amount
		 */
		public OreOptions(Category Biome, Decoration GS, int veinSize, int minHeight, int maxHeight, int amount) 
		{
			this(new Category[] {Biome}, GS, veinSize, minHeight, maxHeight, amount);
		}
		
		public OreOptions(Category[] Biomes, Decoration GS, int veinSize, int minHeight, int maxHeight, int amount) 
		{
			this.Biomes = Biomes;
			this.GS = GS;
			this.veinSize = veinSize;
			this.minHeight = minHeight;
			this.maxHeight = maxHeight;
			this.amount = amount;
		}
	}
}

