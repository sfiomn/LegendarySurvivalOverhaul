package icey.survivaloverhaul.common.tempmods;

import icey.survivaloverhaul.Main;
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
			return (float) Config.BakedConfigValues.sprintModifier;
		}
		else 
		{
			return 0.0f;
		}
	}
}
