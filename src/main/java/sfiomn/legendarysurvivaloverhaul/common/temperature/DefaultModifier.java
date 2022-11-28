package sfiomn.legendarysurvivaloverhaul.common.temperature;

import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DefaultModifier extends ModifierBase
{
	public DefaultModifier()
	{
		super();
	}
	
	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		return defaultTemperature;
	}
}
