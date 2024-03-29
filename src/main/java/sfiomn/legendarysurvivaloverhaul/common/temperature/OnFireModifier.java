package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.entity.player.PlayerEntity;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class OnFireModifier extends ModifierBase
{

	public OnFireModifier()
	{
		super();
	}
	

	@Override
	public float getPlayerInfluence(PlayerEntity player)
	{
		if (player.getRemainingFireTicks() > 0)
		{
			// LegendarySurvivalOverhaul.LOGGER.debug("On fire temp influence : " + String.valueOf(Config.Baked.onFireModifier));
			return (float) Config.Baked.onFireModifier;
		}
		
		return 0.0f;
	}
}
