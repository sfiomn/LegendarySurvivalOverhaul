package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.entity.player.PlayerEntity;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class SprintModifier extends ModifierBase
{
	public SprintModifier()
	{
		super();
	}
	
	@Override
	public float getPlayerInfluence(PlayerEntity player)
	{
		if (player.isSprinting())
		{
			// LegendarySurvivalOverhaul.LOGGER.debug("Sprint temp influence : " + Config.Baked.sprintModifier);
			return (float) Config.Baked.sprintModifier;
		}
		else 
		{
			// LegendarySurvivalOverhaul.LOGGER.debug("Sprint temp influence : " + 0.0f);
			return 0.0f;
		}
	}
}
