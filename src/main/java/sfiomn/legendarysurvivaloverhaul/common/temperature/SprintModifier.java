package sfiomn.legendarysurvivaloverhaul.common.temperature;

import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import net.minecraft.entity.player.PlayerEntity;

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
			return (float) Config.Baked.sprintModifier;
		}
		else 
		{
			return 0.0f;
		}
	}
}
