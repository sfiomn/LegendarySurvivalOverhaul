package sfiomn.legendarysurvivaloverhaul.api.wetness;

import net.minecraft.entity.player.PlayerEntity;

public class WetnessUtil
{
	public static IWetnessUtil internal;

	/**
	 * Add wetness value to the wetness of the given player
	 *
	 * @param player The player to which wetness is added
	 * @param wetness The wetness value
	 */
	public static void addWetness(PlayerEntity player, int wetness) {
		internal.addWetness(player, wetness);
	}

	/**
	 * Deactivate the wetness system for the given player
	 *
	 * @param player The player to which deactivate the wetness system
	 */
	public static void deactivateWetness(PlayerEntity player) {
		internal.deactivateWetness(player);
	}

	/**
	 * Activate the wetness system for the given player
	 *
	 * @param player The player to which activate the wetness system
	 */
	public static void activateWetness(PlayerEntity player) {
		internal.activateWetness(player);
	}

	/**
	 * Check if the wetness system is active for the given player
	 *
	 * @param player The player wetness system to be checked
	 */
	public static boolean isWetnessActive(PlayerEntity player) {
		return internal.isWetnessActive(player);
	}
}
