package icey.survivaloverhaul.util;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.capability.heartmods.HeartModifierCapability;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;
import icey.survivaloverhaul.common.capability.wetness.WetnessCapability;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Helper functions for quickly getting player capabilities.
 * @author Icey
 */
public final class CapabilityUtil
{
	private CapabilityUtil() {}
	
	/**
	 * Gets the temperature capability of the given player.
	 * @param player
	 * @return The temperature capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
	public static TemperatureCapability getTempCapability(PlayerEntity player)
	{
		return player.getCapability(Main.TEMPERATURE_CAP).orElse(new TemperatureCapability());
	}

	/**
	 * Gets the heart modifier capability of the given player.
	 * @param player
	 * @return The heart modifier capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
	public static HeartModifierCapability getHeartModCapability(PlayerEntity player)
	{
		return player.getCapability(Main.HEART_MOD_CAP).orElse(new HeartModifierCapability());
	}

	/**
	 * Gets the wetness capability of the given player.
	 * @param player
	 * @return The wetness capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
	public static WetnessCapability getWetnessCapability(PlayerEntity player)
	{
		return player.getCapability(Main.WETNESS_CAP).orElse(new WetnessCapability());
	}
	
}
