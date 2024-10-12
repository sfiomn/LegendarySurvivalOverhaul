package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperature;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.common.integration.terrafirmacraft.TerraFirmaCraftUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;

public class DimensionModifier extends ModifierBase
{
	public DimensionModifier()
	{
		super();
	}
	
	@Override
	public float getWorldInfluence(@Nullable Player player, Level level, BlockPos pos)
	{
		if (TerraFirmaCraftUtil.shouldUseTerraFirmaCraftTemp())
			return 0.0f;

		JsonTemperature dimensionTemperature = JsonConfig.dimensionTemperatures.get(level.dimension().location().toString());
		if (dimensionTemperature == null)
			return TemperatureEnum.NORMAL.getMiddle();
		return dimensionTemperature.temperature;
	}
}
