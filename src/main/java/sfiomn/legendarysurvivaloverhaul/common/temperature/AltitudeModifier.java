package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class AltitudeModifier extends ModifierBase
{
	public AltitudeModifier()
	{
		super();
	}
	
	@Override
	public float getWorldInfluence(Level level, BlockPos pos)
	{
		if (level.dimensionType().hasCeiling())
		{
			return 0.0f;
		}
		else
		{
			//0 - 64 = (1 to 0) * multiplier + 1		(-4 to -1)
			//64 - 128 = (0 to -1) * multiplier + 1		(-1 to -2)
			//128 - 192 = (-1 to -2) * multiplier + 1	(-2 to -5)
			//192 - 256 = (-2 to -3) * multiplier + 1	(-5 to -8)
			// LegendarySurvivalOverhaul.LOGGER.debug("Altitude temp influence : " + (Math.abs((64.0f - pos.getY()) / 64.0f) * ((float) Config.Baked.altitudeModifier));
			return (Math.abs((64.0f - pos.getY()) / 64.0f) * ((float) Config.Baked.altitudeModifier));
		}
	}
}
