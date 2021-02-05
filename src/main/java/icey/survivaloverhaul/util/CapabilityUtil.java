package icey.survivaloverhaul.util;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.capability.heartmods.HeartModifierCapability;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;
import net.minecraft.entity.player.PlayerEntity;

public class CapabilityUtil
{

	public static TemperatureCapability getTempCapability(PlayerEntity player)
	{
		return player.getCapability(Main.TEMPERATURE_CAP).orElse(new TemperatureCapability());
	}

	public static HeartModifierCapability getHeartModCapability(PlayerEntity player)
	{
		return player.getCapability(Main.HEART_MOD_CAP).orElse(new HeartModifierCapability());
	}
	
}
