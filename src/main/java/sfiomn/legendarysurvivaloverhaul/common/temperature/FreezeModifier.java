package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class FreezeModifier extends ModifierBase
{
	public FreezeModifier()
	{
		super();
	}
	
	@Override
	public float getPlayerInfluence(Player player)
	{
		if (Config.Baked.maxFreezeTemperatureModifier == 0)
			return 0.0f;

		int freezeTickTimer = CapabilityUtil.getTempCapability(player).getFreezeTickTimer();

		if (freezeTickTimer > 0)
		{
			return (float) Mth.lerp(Math.min(freezeTickTimer / (double) Config.Baked.maxFreezeEffectTick, 1), 0, Config.Baked.maxFreezeTemperatureModifier);
		}
		else 
		{
			return 0.0f;
		}
	}
}
