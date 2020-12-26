package icey.survivaloverhaul.common.blocks;

import icey.survivaloverhaul.Main;
import net.minecraft.block.Block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

import net.minecraft.item.ItemGroup;

public class BlockGeneric extends Block
{
	public ItemGroup group;
	public boolean associatedItem;
	
	public BlockGeneric(Material materialIn, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound)
	{
		super(
			Block.Properties
				.create(materialIn)
				.sound(sound)
				.hardnessAndResistance(hardness, resistance)
				.harvestTool(ToolType.get(toolUsed))
			);
		
		group = ItemGroup.BUILDING_BLOCKS;
		this.associatedItem = true;
		
		this.setRegistryName(Main.MOD_ID, name);
	}
	
	public BlockGeneric(Material materialIn, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, ItemGroup group)
	{
		super(
			Block.Properties
				.create(materialIn)
				.sound(sound)
				.hardnessAndResistance(hardness, resistance)
				.harvestTool(ToolType.get(toolUsed))
			);
		
		this.group = group;
		this.associatedItem = true;
		
		this.setRegistryName(Main.MOD_ID, name);
	}
	
	public BlockGeneric(Material materialIn, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, boolean associatedItem)
	{
		super(
			Block.Properties
				.create(materialIn)
				.sound(sound)
				.hardnessAndResistance(hardness, resistance)
				.harvestTool(ToolType.get(toolUsed))
			);
		
		this.group = null;
		this.associatedItem = associatedItem;
		
		this.setRegistryName(Main.MOD_ID, name);
	}
}
