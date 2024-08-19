package sfiomn.legendarysurvivaloverhaul.api.thirst;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonEffectParameter;

import java.util.List;

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
	public static HydrationEnum traceWater(Player player)
	{
		return internal.traceWater(player);
	}

	/**
	 * Player takes a drink with the specified values and a list of possible effects
	 * @param player
	 * @param hydration
	 * @param saturation
	 * @param effects
	 */
	public static void takeDrink(Player player, int hydration, float saturation, List<JsonEffectParameter> effects)
	{
		internal.takeDrink(player, hydration, saturation, effects);
	}

	/**
	 * Player takes a drink with the specified values and no chance to trigger side effects
	 * @param player
	 * @param hydration
	 * @param saturation
	 */
	public static void takeDrink(Player player, int hydration, float saturation)
	{
		internal.takeDrink(player, hydration, saturation);
	}

	/**
	 * Player takes a drink with the values of the HydrationEnum
	 * @param player
	 * @param hydrationEnum
	 */
	public static void takeDrink(Player player, HydrationEnum hydrationEnum)
	{
		internal.takeDrink(player, hydrationEnum);
	}

	/**
	 * Add thirst exhaustion responsible for the thirst depletion
	 * @param player
	 * @param exhaustion
	 */
	public static void addExhaustion(Player player, float exhaustion)
	{
		internal.addExhaustion(player, exhaustion);
	}

	/**
	 * Get hydration enum the player is looking at, with the given maximum distance
	 * @param player
	 * @param finalDistance
	 */

	public HydrationEnum getHydrationEnumLookedAt(Player player, double finalDistance) {
		return internal.getHydrationEnumLookedAt(player, finalDistance);
	}

	/**
	 * Sets the thirst enum name tag on the stack
	 * @param stack Item stack drink
	 * @param hydrationEnum Thirst Enum
	 */
	public static void setHydrationEnumTag(final ItemStack stack, HydrationEnum hydrationEnum)
	{
		internal.setThirstEnumTag(stack, hydrationEnum);
	}

	/**
	 * Gets the hydration enum from the thirst enum tag on the stack
	 * @param stack Item stack drink
	 * @return null if tag is missing
	 */
	@Nullable
	public static HydrationEnum getHydrationEnumTag(final ItemStack stack)
	{
		return internal.getHydrationEnumTag(stack);
	}

	/**
	 * Removes the hydration enum tag on the stack if it exists
	 * @param stack Item stack drink
	 */
	public static void removeHydrationEnumTag(final ItemStack stack)
	{
		internal.removeHydrationEnumTag(stack);
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

    /**
     * Retrieves the JsonConsumableThirst from the jsonThirstConsumable json file for the given itemStack
     *
     * @param itemRegistryName Item stack registry name
     * @param itemStack        Item stack
     */
    public static JsonConsumableThirst getThirstJsonConfig(ResourceLocation itemRegistryName, ItemStack itemStack) {
        return internal.getThirstJsonConfig(itemRegistryName, itemStack);
    }

	/**
	 * Deactivate the thirst system for the given player
	 *
	 * @param player The player to which deactivate the thirst system
	 */
	public static void deactivateThirst(Player player) {
		internal.deactivateThirst(player);
	}

	/**
	 * Activate the thirst system for the given player
	 *
	 * @param player The player to which activate the thirst system
	 */
	public static void activateThirst(Player player) {
		internal.activateThirst(player);
	}

	/**
	 * Check if the thirst system is active for the given player
	 *
	 * @param player The player thirst system to be checked
	 */
	public static boolean isThirstActive(Player player) {
		return internal.isThirstActive(player);
	}
}
