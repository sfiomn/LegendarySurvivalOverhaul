package sfiomn.legendarysurvivaloverhaul.common.temperature;

import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
		
		float biomeMultiplier = 1.0f + (Math.abs(normalizeToPosNeg(getTempForBiome(world.getBiome(pos)))) * ((float)Config.Baked.biomeTimeMultiplier - 1.0f));
		timeTemperature *= biomeMultiplier;
		
		if(timeTemperature > 0 && Config.Baked.timeShadeModifier != 0 && !world.canSeeSky(pos.above()))
		{
			timeTemperature = Math.max(0, timeTemperature + Config.Baked.timeShadeModifier);
		}
		
		return applyUndergroundEffect(timeTemperature, world, pos);
	}
}