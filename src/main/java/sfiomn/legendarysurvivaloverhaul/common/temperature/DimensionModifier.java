package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperature;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;

public class DimensionModifier extends ModifierBase
{
	public DimensionModifier()
	{
		super();
	}
	
	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		JsonTemperature dimensionTemperature = JsonConfig.dimensionTemperatures.get(world.dimension().location().toString());
		if (dimensionTemperature == null)
			return TemperatureEnum.NORMAL.getMiddle();
		return dimensionTemperature.temperature;
	}
}
