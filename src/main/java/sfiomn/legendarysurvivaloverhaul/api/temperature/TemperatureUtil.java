package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ItemAttributeModifierEvent;

import java.util.UUID;

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
	public static float getPlayerTargetTemperature(Player player)
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
	public static float getWorldTemperature(Level world, BlockPos pos)
	{
		return internal.getWorldTemperature(world, pos);
	}
	
	/**
	 * Takes a temperature and clamps it to fit within TemperatureEnum bounds
	 * @param temperature Temperature
	 * @return Clamped temperature
	 */
	public static float clampTemperature(float temperature)
	{
		return internal.clampTemperature(temperature);
	}
	
	/**
	 * Takes a temperature and finds its matching TemperatureEnum enum
	 * @param temperature Temperature
	 * @return TemperatureEnum for the temperature
	 */
	public static TemperatureEnum getTemperatureEnum(float temperature)
	{
		return internal.getTemperatureEnum(temperature);
	}

	/**
	 * Check if player has the given temperature immunity
	 * @param player Player
	 * @param immunity Temperature immunity
	 */
	public static boolean hasImmunity(Player player, TemperatureImmunityEnum immunity)
	{
		return internal.hasImmunity(player, immunity);
	}

	/**
	 * Add a temperature immunity to the given player
	 * @param player Player
	 * @param immunity Temperature immunity
	 */
	public static void addImmunity(Player player, TemperatureImmunityEnum immunity)
	{
		internal.addImmunity(player, immunity);
	}

	/**
	 * Remove a temperature immunity to the given player
	 * @param player Player
	 * @param immunity Temperature immunity
	 */
	public static void removeImmunity(Player player, TemperatureImmunityEnum immunity)
	{
		internal.removeImmunity(player, immunity);
	}

	/**
	 * Adds all registered temperature attribute modifiers on an item via the ItemAttributeModifier event
	 * @param event ItemAttributeModifierEvent
	 */
	public static void applyItemAttributeModifiers(ItemAttributeModifierEvent event) {
		internal.applyItemAttributeModifiers(event);
	}

	/**
	 * Adds a temperature attribute modifier on the provided player. Don't forget to keep track of the uuid used
	 * @param player player
	 * @param temperature Temperature value to be added to the player
	 * @param uuid Uuid of the modifier, necessary to remove the modifier when not used anymore
	 */
	public static void addTemperatureModifier(Player player, double temperature, UUID uuid) {
		internal.addTemperatureModifier(player, temperature, uuid);
	}

	/**
	 * Adds a heat resistance attribute modifier on the provided player. Don't forget to keep track of the uuid used
	 * @param player player
	 * @param resistance Heat resistance value to be added to the player
	 * @param uuid Uuid of the modifier, necessary to remove the modifier when not used anymore
	 */
	public static void addHeatResistanceModifier(Player player, double resistance, UUID uuid) {
		internal.addHeatResistanceModifier(player, resistance, uuid);
	}

	/**
	 * Adds a cold resistance attribute modifier on the provided player. Don't forget to keep track of the uuid used
	 * @param player player
	 * @param resistance Cold resistance value to be added to the player
	 * @param uuid Uuid of the modifier, necessary to remove the modifier when not used anymore
	 */
	public static void addColdResistanceModifier(Player player, double resistance, UUID uuid) {
		internal.addColdResistanceModifier(player, resistance, uuid);
	}

	/**
	 * Adds a thermal resistance attribute modifier on the provided player. Don't forget to keep track of the uuid used
	 * @param player player
	 * @param resistance Thermal resistance value to be added to the player
	 * @param uuid Uuid of the modifier, necessary to remove the modifier when not used anymore
	 */
	public static void addThermalResistanceModifier(Player player, double resistance, UUID uuid) {
		internal.addThermalResistanceModifier(player, resistance, uuid);
	}

	/**
	 * Sets the coat type tag on the armor stack, so it heats, cools or both
	 * @param stack Item stack armor
	 * @param coatId Coat Id
	 */
	public static void setArmorCoatTag(final ItemStack stack, String coatId)
	{
		internal.setArmorCoatTag(stack, coatId);
	}

	/**
	 * Gets the coat type tag on the armor stack
	 * @param stack Item stack armor
	 * @return "" if tag is missing
	 */
	public static String getArmorCoatTag(final ItemStack stack)
	{
		return internal.getArmorCoatTag(stack);
	}

	/**
	 * Removes the coat type tag from the armor stack if it exists
	 * @param stack Item stack armor
	 */
	public static void removeArmorCoatTag(final ItemStack stack)
	{
		internal.removeArmorCoatTag(stack);
	}
}
