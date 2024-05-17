package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;

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
		if (world.dimension() == World.NETHER)
			return (float) Config.Baked.netherDefaultTemperature;
		else if (world.dimension() == World.END)
			return (float) Config.Baked.endDefaultTemperature;
		return (float) Config.Baked.overworldDefaultTemperature;
	}
}
