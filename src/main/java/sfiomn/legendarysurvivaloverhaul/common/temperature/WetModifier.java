package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.entity.player.PlayerEntity;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;


public class WetModifier extends ModifierBase
{
	public WetModifier()
	{
		super();
	}
	
	@Override
	public float getPlayerInfluence(PlayerEntity player)
	{
		if (Config.Baked.wetnessEnabled)
		{
			WetnessCapability wetCap = CapabilityUtil.getWetnessCapability(player);
			if (wetCap.getWetness() == 0) {
				// LegendarySurvivalOverhaul.LOGGER.debug("Wet player temp influence : " + 0.0f);
				return 0.0f;
			} else {
				// LegendarySurvivalOverhaul.LOGGER.debug("Wet player temp influence : " + (float) (Config.Baked.wetMultiplier * MathUtil.invLerp(0, WetnessCapability.WETNESS_LIMIT, wetCap.getWetness())));
				return (float) (Config.Baked.wetMultiplier * MathUtil.invLerp(0, WetnessCapability.WETNESS_LIMIT, wetCap.getWetness()));
			}
		}
		return 0.0f;
	}
}
