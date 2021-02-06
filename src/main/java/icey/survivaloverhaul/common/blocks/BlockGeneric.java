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
	
	public BlockGeneric(Material materialIn, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound)
	{
		this(materialIn, toolUsed, toolStrength, hardness, resistance, sound, ItemGroup.BUILDING_BLOCKS);
	}
	
	public BlockGeneric(Material materialIn, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, boolean associatedItem)
	{
		this(materialIn, toolUsed, toolStrength, hardness, resistance, sound, associatedItem ? ItemGroup.BUILDING_BLOCKS : null);
	}
	
	public BlockGeneric(Material materialIn, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, ItemGroup group)
	{
		super(
			Block.Properties
				.create(materialIn)
				.sound(sound)
				.hardnessAndResistance(hardness, resistance)
				.harvestTool(ToolType.get(toolUsed))
			);
		
		this.group = group;
	}
}
