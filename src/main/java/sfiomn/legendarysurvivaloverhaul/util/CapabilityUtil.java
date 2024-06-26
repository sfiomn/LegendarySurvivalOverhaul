package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.heartmods.HeartModifierCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.heartmods.HeartModifierProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessProvider;

/**
 * Helper functions for quickly getting player capabilities.
 * @author Icey
 */
public final class CapabilityUtil
{
	private CapabilityUtil() {}
	
	/**
	 * Gets the temperature capability of the given player.
	 * @param player Player
	 * @return The temperature capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
	public static TemperatureCapability getTempCapability(Player player)
	{
		return player.getCapability(TemperatureProvider.TEMPERATURE_CAPABILITY).orElse(new TemperatureCapability());
	}

	/**
	 * Gets the temperature item capability of the given itemstack.
	 * @param itemStack ItemStack
	 * @return The temperature item capability of the given itemstack if it exists, or a new dummy capability if it doesn't.
	 */
	public static TemperatureItemCapability getTempItemCapability(ItemStack itemStack)
	{
		return itemStack.getCapability(TemperatureItemCapability.TemperatureItemProvider.TEMPERATURE_ITEM_CAPABILITY).orElse(new TemperatureItemCapability());
	}

	/**
	 * Gets the heart modifier capability of the given player.
	 * @param player Player
	 * @return The heart modifier capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
	public static HeartModifierCapability getHeartModCapability(Player player)
	{
		return player.getCapability(HeartModifierProvider.HEART_MODIFIER_CAPABILITY).orElse(new HeartModifierCapability());
	}

	/**
	 * Gets the wetness capability of the given player.
	 * @param player Player
	 * @return The wetness capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
	public static WetnessCapability getWetnessCapability(Player player)
	{
		return player.getCapability(WetnessProvider.WETNESS_CAPABILITY).orElse(new WetnessCapability());
	}

	/**
	 * Gets the thirst capability of the given player.
	 * @param player Player
	 * @return The thirst capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
	public static ThirstCapability getThirstCapability(Player player)
	{
		return player.getCapability(ThirstProvider.THIRST_CAPABILITY).orElse(new ThirstCapability());
	}

	/**
	 * Gets the Food capability of the given player.
	 * @param player Player
	 * @return The food capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
	public static FoodCapability getFoodCapability(Player player)
	{
		return player.getCapability(FoodProvider.FOOD_CAPABILITY).orElse(new FoodCapability());
	}

	/**
	 * Gets the Body Damage capability of the given player.
	 * @param player Player
	 * @return The body damage capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
	public static BodyDamageCapability getBodyDamageCapability(Player player)
	{
		return player.getCapability(BodyDamageProvider.BODY_DAMAGE_CAPABILITY).orElse(new BodyDamageCapability());
	}
}
