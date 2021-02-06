package icey.survivaloverhaul.common.blocks;

import net.minecraft.block.Block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockGeneric extends Block
{
	public BlockGeneric(Material materialIn, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound)
	{
		super(
			Block.Properties
				.create(materialIn)
				.sound(sound)
				.hardnessAndResistance(hardness, resistance)
				.harvestTool(ToolType.get(toolUsed))
			);
	}
}
