package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureImmunityEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class OnFireModifier extends ModifierBase
{

	public OnFireModifier()
	{
		super();
	}
	

	@Override
	public float getPlayerInfluence(Player player)
	{
		if (player.getRemainingFireTicks() > 0 && !TemperatureUtil.hasImmunity(player, TemperatureImmunityEnum.ON_FIRE))
		{
			// LegendarySurvivalOverhaul.LOGGER.debug("On fire temp influence : " + String.valueOf(Config.Baked.onFireModifier));
			return (float) Config.Baked.onFireModifier;
		}
		
		return 0.0f;
	}
}
