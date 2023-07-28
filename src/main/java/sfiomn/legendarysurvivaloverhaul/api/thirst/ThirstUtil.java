package sfiomn.legendarysurvivaloverhaul.api.thirst;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ThirstUtil
{
	public static IThirstUtil internal;

	/**
	 * Ray traces the block a player is looking at and returns it as a ThirstEnumBlockPos
	 * <br>
	 * Returns null if there is no trace result
	 * @param player
	 * @return ThirstEnumBlockPos trace result
	 *
	 */
	@Nullable
	public static ThirstEnum traceWater(PlayerEntity player)
	{
		return internal.traceWater(player);
	}

	/**
	 * Player takes a drink with the specified values and a chance to make them thirsty
	 * @param player
	 * @param thirst
	 * @param saturation
	 * @param dirtyChance 0.0f - 1.0f
	 */
	public static void takeDrink(PlayerEntity player, int thirst, float saturation, float dirtyChance)
	{
		internal.takeDrink(player, thirst, saturation, dirtyChance);
	}

	/**
	 * Player takes a drink with the specified values and no chance to make them thirsty
	 * @param player
	 * @param thirst
	 * @param saturation
	 */
	public static void takeDrink(PlayerEntity player, int thirst, float saturation)
	{
		internal.takeDrink(player, thirst, saturation);
	}

	/**
	 * Player takes a drink with the values of the ThirstEnum
	 * @param player
	 * @param thirstEnum
	 */
	public static void takeDrink(PlayerEntity player, ThirstEnum thirstEnum)
	{
		internal.takeDrink(player, thirstEnum);
	}

	/**
	 * Add thirst exhaustion responsible for the thirst depletion
	 * @param player
	 * @param exhaustion
	 */
	public static void addExhaustion(PlayerEntity player, float exhaustion)
	{
		internal.addExhaustion(player, exhaustion);
	}

	/**
	 * Get the ThirstEnum from the targeted fluid player is looking at, up to the final distance
	 * @param player
	 * @param finalDistance
	 */
	public static ThirstEnum getThirstEnumLookedAt(PlayerEntity player, double finalDistance) {
		return internal.getThirstEnumLookedAt(player, finalDistance);
	}

	/**
	 * Returns a new Purified Water Bucket item
	 * @return ItemStack purified water bucket
	 */
	public static ItemStack createPurifiedWaterBucket()
	{
		return internal.createPurifiedWaterBucket();
	}

	/**
	 * Sets the thirst enum name tag on the stack
	 * @param stack Item stack drink
	 * @param thirstEnum Thirst Enum
	 */
	public static void setThirstEnumTag(final ItemStack stack, ThirstEnum thirstEnum)
	{
		internal.setThirstEnumTag(stack, thirstEnum);
	}

	/**
	 * Gets the thirst enum from the thirst enum tag on the stack
	 * @param stack Item stack drink
	 * @return null if tag is missing
	 */
	@Nullable
	public static ThirstEnum getThirstEnumTag(final ItemStack stack)
	{
		return internal.getThirstEnumTag(stack);
	}

	/**
	 * Removes the thirst enum tag on the stack if it exists
	 * @param stack Item stack drink
	 */
	public static void removeThirstEnumTag(final ItemStack stack)
	{
		internal.removeThirstEnumTag(stack);
	}

	/**
	 * Sets the thirst capacity tag on the stack, so the number of drink doses
	 * @param stack Item stack drink
	 * @param capacity number of doses drink
	 */
	public static void setCapacityTag(final ItemStack stack, int capacity)
	{
		internal.setCapacityTag(stack, capacity);
	}

	/**
	 * Gets the capacity tag on the stack
	 * @param stack Item stack drink
	 * @return 0 if tag is missing
	 */
	public static int getCapacityTag(final ItemStack stack)
	{
		return internal.getCapacityTag(stack);
	}

	/**
	 * Removes the capacity tag on the stack if it exists
	 * @param stack Item stack drink
	 */
	public static void removeCapacityTag(final ItemStack stack)
	{
		internal.removeCapacityTag(stack);
	}
}
