package icey.survivaloverhaul.common.temperature;

import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.config.Config;
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
		if (player.getFireTimer() > 0)
		{
			return (float) Config.Baked.onFireModifier;
		}
		
		return 0.0f;
	}
}
