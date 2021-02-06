package icey.survivaloverhaul.common.temperature;

import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.api.temperature.TemporaryModifier;
import icey.survivaloverhaul.util.CapabilityUtil;
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
