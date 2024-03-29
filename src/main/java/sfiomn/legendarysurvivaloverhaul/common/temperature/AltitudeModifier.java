package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class AltitudeModifier extends ModifierBase
{
	public AltitudeModifier()
	{
		super();
	}
	
	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		if (world.dimensionType().hasCeiling())
		{
			return 0.0f;
		}
		else
		{
			//0 - 64 = (1 to 0) * multiplier + 1		(-4 to -1)
			//64 - 128 = (0 to -1) * multiplier + 1		(-1 to -2)
			//128 - 192 = (-1 to -2) * multiplier + 1	(-2 to -5)
			//192 - 256 = (-2 to -3) * multiplier + 1	(-5 to -8)
			// LegendarySurvivalOverhaul.LOGGER.debug("Altitude temp influence : " + -1.0f * (Math.abs((64.0f - pos.getY()) / 64.0f * ((float) Config.Baked.altitudeModifier))));
			return -1.0f * (Math.abs((64.0f - pos.getY()) / 64.0f * ((float) Config.Baked.altitudeModifier)));
		}
	}
}
