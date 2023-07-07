package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class TimeModifier extends ModifierBase
{
	
	public TimeModifier()
	{
		super();
	}
	
	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		// This effect should only be provided in surface worlds
		if(world.dimensionType().hasCeiling())
		{
			return 0.0f;
		}
		
		long time = world.getLevelData().getDayTime();

		float timeTemperature = (float) Math.sin ((time * Math.PI) / 12000.0f) * (float) Config.Baked.timeMultiplier;
		
		float biomeMultiplier = 1.0f + (Math.abs(normalizeToPositiveNegative(getTempForBiome(world, world.getBiome(pos)))) * ((float)Config.Baked.biomeTimeMultiplier - 1.0f));
		timeTemperature *= biomeMultiplier;

		// LegendarySurvivalOverhaul.LOGGER.debug("Time temp influence : " + timeTemperature);
		// float tempInfl = applyUndergroundEffect(timeTemperature, world, pos);
		// LegendarySurvivalOverhaul.LOGGER.debug("Time temp influence after underground : " + tempInfl);

		return applyUndergroundEffect(timeTemperature, world, pos);
	}
}