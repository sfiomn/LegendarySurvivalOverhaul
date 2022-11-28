package sfiomn.legendarysurvivaloverhaul.common.temperature;

import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import net.minecraft.entity.player.PlayerEntity;

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
			return (float) Config.Baked.onFireModifier;
		}
		
		return 0.0f;
	}
}
