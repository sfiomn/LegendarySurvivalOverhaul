package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;

public class DefaultModifier extends ModifierBase
{
	public DefaultModifier()
	{
		super();
	}
	
	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		// LegendarySurvivalOverhaul.LOGGER.debug("Default temp influence : " + defaultTemperature);
		return defaultTemperature;
	}
}
