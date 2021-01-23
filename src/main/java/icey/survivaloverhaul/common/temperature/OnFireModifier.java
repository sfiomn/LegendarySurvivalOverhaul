package icey.survivaloverhaul.common.temperature;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.config.Config;
import net.minecraft.entity.player.PlayerEntity;

public class OnFireModifier extends ModifierBase
{

	public OnFireModifier()
	{
		super();
		this.setRegistryName(Main.MOD_ID, "on_fire");
	}
	

	@Override
	public float getPlayerInfluence(PlayerEntity player)
	{
		if (player.getFireTimer() > 0)
		{
			return (float) Config.BakedConfigValues.onFireModifier;
		}
		
		return 0.0f;
	}
}
