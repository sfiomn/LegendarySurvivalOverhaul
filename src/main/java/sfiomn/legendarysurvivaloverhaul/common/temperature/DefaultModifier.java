package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.integration.terrafirmacraft.TerraFirmaCraftUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.awt.*;

public class DefaultModifier extends ModifierBase
{
	public DefaultModifier()
	{
		super();
	}
	
	@Override
	public float getWorldInfluence(Level level, BlockPos pos)
	{
		if (TerraFirmaCraftUtil.shouldUseTerraFirmaCraftTemp())
			return 0.0f;

		// LegendarySurvivalOverhaul.LOGGER.debug("Default temp influence : " + defaultTemperature);
		if (level.dimension() == Level.NETHER)
			return (float) Config.Baked.netherDefaultTemperature;
		else if (level.dimension() == Level.END)
			return (float) Config.Baked.endDefaultTemperature;
		return (float) Config.Baked.overworldDefaultTemperature;
	}
}
