package icey.survivaloverhaul.common.temperature;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.api.temperature.TemporaryModifier;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerTemporaryModifier extends ModifierBase
{
	public PlayerTemporaryModifier()
	{
		super();

		this.setRegistryName(Main.MOD_ID, "temporary");
	}
	
	@Override
	public float getPlayerInfluence(PlayerEntity player)
	{
		float sum = 0.0f;
		
		for(TemporaryModifier tm : TemperatureCapability.getTempCapability(player).getTemporaryModifiers().values())
		{
			sum += tm.temperature;
		}
		
		return sum;
	}
}
