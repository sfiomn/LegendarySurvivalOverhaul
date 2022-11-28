package sfiomn.legendarysurvivaloverhaul.common.temperature;

import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifier;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerTemporaryModifier extends ModifierBase
{
	public PlayerTemporaryModifier()
	{
		super();

	}
	
	@Override
	public float getPlayerInfluence(PlayerEntity player)
	{
		float sum = 0.0f;
		
		for(TemporaryModifier tm : CapabilityUtil.getTempCapability(player).getTemporaryModifiers().values())
		{
			sum += tm.temperature;
		}
		
		return sum;
	}
}
