package icey.survivaloverhaul.common.temperature;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.config.Config;
import net.minecraft.entity.player.PlayerEntity;

public class SprintModifier extends ModifierBase
{
	public SprintModifier()
	{
		super();
		this.setRegistryName(Main.MOD_ID, "sprint");
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
