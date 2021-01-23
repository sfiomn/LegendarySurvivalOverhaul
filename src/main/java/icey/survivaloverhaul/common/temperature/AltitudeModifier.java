package icey.survivaloverhaul.common.temperature;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.config.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AltitudeModifier extends ModifierBase
{
	public AltitudeModifier()
	{
		super();
		this.setRegistryName(Main.MOD_ID, "altitude");
	}
	
	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		if (world.getDimensionType().getHasCeiling())
		{
			return 0.0f;
		}
		else
		{
			//0 - 64 = (1 to 0) * multiplier + 1		(-4 to -1)
			//64 - 128 = (0 to -1) * multiplier + 1		(-1 to -2)
			//128 - 192 = (-1 to -2) * multiplier + 1	(-2 to -5)
			//192 - 256 = (-2 to -3) * multiplier + 1	(-5 to -8)
			return -1.0f * (Math.abs(((64.0f - pos.getY()) / 64.0f * ((float) Config.BakedConfigValues.altitudeModifier)) + 1.0f));
		}
	}
}
