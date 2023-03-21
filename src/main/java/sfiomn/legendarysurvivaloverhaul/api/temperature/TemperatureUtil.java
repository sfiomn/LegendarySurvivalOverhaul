package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;

public class TemperatureUtil
{
	public static ITemperatureUtil internal;
	
	/**
	 * Calculates the target temperature of a player. <br>
	 * This is the target temperature, not the player's actual temperature! <br>
	 * Calling this infrequently is recommended for the best performance. <br>
	 * @param player Player
	 * @return Player's target temperature
	 */
	public static int getPlayerTargetTemperature(PlayerEntity player)
	{
		return internal.getPlayerTargetTemperature(player);
	}
	
	/**
	 * Calculates the temperature of the world at a position. <br>
	 * Calling this infrequently is recommended for the best performance.
	 * @param world World
	 * @param pos Position
	 * @return World temperature at position
	 */
	public static int getWorldTemperature(World world, BlockPos pos)
	{
		return internal.getWorldTemperature(world, pos);
	}
	
	/**
	 * Takes a temperature and clamps it to fit within TemperatureEnum bounds
	 * @param temperature Temperature
	 * @return Clamped temperature
	 */
	public static int clampTemperature(int temperature)
	{
		return internal.clampTemperature(temperature);
	}
	
	/**
	 * Takes a temperature and finds its matching TemperatureEnum enum
	 * @param temperature Temperature
	 * @return TemperatureEnum for the temperature
	 */
	public static TemperatureEnum getTemperatureEnum(int temperature)
	{
		return internal.getTemperatureEnum(temperature);
	}
	
	/**
	 * Sets the armor temperature type tag on the stack, so it heats, cools or both
	 * @param stack Item stack armor
	 * @param coatId Padding Id
	 */
	public static void setArmorCoatTag(final ItemStack stack, String coatId)
	{
		internal.setArmorCoatTag(stack, coatId);
	}

	/**
	 * Gets the armor temperature type tag on the stack
	 * @param stack Item stack armor
	 * @return "" if tag is missing
	 */
	public static String getArmorCoatTag(final ItemStack stack)
	{
		return internal.getArmorCoatTag(stack);
	}
	
	/**
	 * Removes the armor temperature type tag on the stack if it exists
	 * @param stack Item stack armor
	 */
	public static void removeArmorCoatTag(final ItemStack stack)
	{
		internal.removeArmorCoatTag(stack);
	}
}
