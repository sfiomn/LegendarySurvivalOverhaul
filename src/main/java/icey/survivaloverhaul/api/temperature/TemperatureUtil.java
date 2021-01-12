package icey.survivaloverhaul.api.temperature;

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
	 * TODO: Implement capabilities properly! <br>
	 * Calling this infrequently is recommended for the best performance. <br>
	 * @param player
	 * @return Player's target temperature
	 */
	public static int getPlayerTargetTemperature(PlayerEntity player)
	{
		return internal.getPlayerTargetTemperature(player);
	}
	
	/**
	 * Calculates the temperature of the world at a position. <br>
	 * Calling this infrequently is recommended for the best performance.
	 * @param world
	 * @param pos
	 * @return World temperature at position
	 */
	public static int getWorldTemperature(World world, BlockPos pos)
	{
		return internal.getWorldTemperature(world, pos);
	}
	
	/**
	 * Takes a temperature and clamps it to fit within TemperatureEnum bounds
	 * @param temperature
	 * @return clamped temperature
	 */
	public static int clampTemperature(int temperature)
	{
		return internal.clampTemperature(temperature);
	}
	
	/**
	 * Takes a temperature and finds its matching TemperatureEnum enum
	 * @param temperature
	 * @return TemperatureEnum for the temperature
	 */
	public static TemperatureEnum getTemperatureEnum(int temp)
	{
		return internal.getTemperatureEnum(temp);
	}
	
	/**
	 * Sets the armor temperature tag on the stack so it heats or cools
	 * @param stack
	 * @param temperature
	 */
	public static void setArmorTemperatureTag(final ItemStack stack, float temperature)
	{
		internal.setArmorTemperatureTag(stack, temperature);
	}
	
	/**
	 * Gets the armor temperature tag on the stack
	 * @param stack
	 * @return temperature, 0.0f if tag is missing
	 */
	public static float getArmorTemperatureTag(final ItemStack stack)
	{
		return internal.getArmorTemperatureTag(stack);
	}
	
	/**
	 * Removes the armor temperature tag on the stack if it exists
	 * @param stack
	 */
	public static void removeArmorTemperatureTag(final ItemStack stack)
	{
		internal.removeArmorTemperatureTag(stack);
	}
}
