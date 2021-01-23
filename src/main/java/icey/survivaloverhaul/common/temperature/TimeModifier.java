package icey.survivaloverhaul.common.temperature;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.config.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TimeModifier extends ModifierBase
{
	
	public TimeModifier()
	{
		super();
		this.setRegistryName(Main.MOD_ID, "time");
	}
	
	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		// This effect should only be provided in surface worlds
		if(world.getDimensionType().getHasCeiling())
		{
			return 0.0f;
		}
		
		long time = world.getWorldInfo().getDayTime();
		
		float timeTemperature = (Math.abs(((time % 12000.0f) - 6000.0f) / 6000.0f) - 1.0f) * (float) Config.BakedConfigValues.timeMultiplier;
		
		if (time < 12000)
		{
			timeTemperature *= -1.0f;
		}
		
		float biomeMultiplier = 1.0f + (Math.abs(normalizeToPosNeg(getTempForBiome(world.getBiome(pos)))) * ((float)Config.BakedConfigValues.biomeTimeMultiplier - 1.0f));
		timeTemperature *= biomeMultiplier;
		
		if(timeTemperature > 0 && Config.BakedConfigValues.timeShadeModifier != 0 && !world.canSeeSky(pos.up()))
		{
			timeTemperature = Math.max(0, timeTemperature + Config.BakedConfigValues.timeShadeModifier);
		}
		
		return applyUndergroundEffect(timeTemperature, world, pos);
	}
}