package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureImmunityEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.terrafirmacraft.TerraFirmaCraftUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class AltitudeModifier extends ModifierBase
{
	public AltitudeModifier()
	{
		super();
	}
	
	@Override
	public float getWorldInfluence(@Nullable Player player, Level level, BlockPos pos)
	{
		if (level.dimensionType().hasCeiling() || TerraFirmaCraftUtil.shouldUseTerraFirmaCraftTemp())
		{
			return 0.0f;
		}
		else
		{

			//0 - 64 = (1 to 0) * multiplier		(-5 to 0)
			//64 - 128 = (0 to -1) * multiplier		(0 to -5)
			//128 - 192 = (-1 to -2) * multiplier	(-5 to -10)
			//192 - 256 = (-2 to -3) * multiplier	(-10 to -15)
			// LegendarySurvivalOverhaul.LOGGER.debug("Altitude temp influence : " + (Math.abs((64.0f - pos.getY()) / 64.0f) * ((float) Config.Baked.altitudeModifier));
			float altitudeDiff = (pos.getY() - 64.0f) / 64.0f;
			if (player != null && altitudeDiff > 0 && TemperatureUtil.hasImmunity(player, TemperatureImmunityEnum.HIGH_ALTITUDE))
				return 0.0f;
			return (Math.abs(altitudeDiff) * ((float) Config.Baked.altitudeModifier));
		}
	}
}
