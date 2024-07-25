package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffect;

import java.util.List;

public class BodyDamageUtil
{
	public static IBodyDamageUtil internal;

	/**
	 * Get effects applicable to the player. <br>
	 * This is based on the body part and the body health ratio, defined in the configuration file <br>
	 * @param bodyPart malus bodyPart enum
	 * @param headHealthRatio headHealthRatio
	 * @return List of effects and associated amplifier
	 */
	public static List<Pair<MobEffect, Integer>> getEffects(MalusBodyPartEnum bodyPart, float headHealthRatio)
	{
		return internal.getEffects(bodyPart, headHealthRatio);
	}

	/**
	 * Apply healing item on player's body limb, defined in its body damage capability. <br>
	 * @param player player to heal
	 * @param bodyPart bodyPart enum where healing item is applied
	 * @param healingValue healing value to be healed over healingTime
	 * @param healingTime healing time taken to heal healingValue
	 */
	public static void applyHealingTimeBodyPart(Player player, BodyPartEnum bodyPart, float healingValue, int healingTime)
	{
		internal.applyHealingTimeBodyPart(player, bodyPart, healingValue, healingTime);
	}

	/**
	 * Heal a player's body limb, defined in its body damage capability. <br>
	 * @param player player to heal
	 * @param bodyPart bodyPart enum to heal
	 * @param healingValue quantity healed
	 */
	public static void healBodyPart(Player player, BodyPartEnum bodyPart, float healingValue)
	{
		internal.healBodyPart(player, bodyPart, healingValue);
	}

	/**
	 * Hurt a player's body limb, defined in its body damage capability.<br>
	 * The remaining damage that exceed body part health will be propagated to a neighbour body part<br>
	 * @param player player to hurt
	 * @param bodyPart bodyPart enum to hurt
	 * @param damageValue damage value inflicted to body part
	 */
	public static void hurtBodyPart(Player player, BodyPartEnum bodyPart, float damageValue)
	{
		internal.hurtBodyPart(player, bodyPart, damageValue);
	}

	/**
	 * Hurt a set of player's body limbs, defined in its body damage capability. <br>
	 * The damage value will be divided across each body part <br>
	 * @param player player to hurt
	 * @param bodyParts list of bodyPart enum to hurt
	 * @param damageValue damage value inflicted to body part
	 */
	public static void balancedHurtBodyParts(Player player, List<BodyPartEnum> bodyParts, float damageValue) {
		internal.balancedHurtBodyParts(player, bodyParts, damageValue);
	}

	/**
	 * Hurt a random player's body limb, defined in its body damage capability. <br>
	 * The damage value will be fully assigned to this body limb <br>
	 * @param player player to hurt
	 * @param bodyParts list of bodyPart enum to hurt
	 * @param damageValue damage value inflicted to body part
	 */
	public static void randomHurtBodyParts(Player player, List<BodyPartEnum> bodyParts, float damageValue) {
		internal.randomHurtBodyParts(player, bodyParts, damageValue);
	}

	/**
	 * Get the health percentage of a player's body limb, defined in its body damage capability. <br>
	 * @param player player
	 * @param bodyPart bodyPart enum
	 * @return health / max health of the player's body limb
	 */
	public static float getHealthRatio(Player player, BodyPartEnum bodyPart)
	{
		return internal.getHealthRatio(player, bodyPart);
	}

	/**
	 * Get the total remaining healing applying on the player's body limb, defined in its body damage capability. <br>
	 * @param player player
	 * @param bodyPart bodyPart enum
	 * @return remaining healing that will be applied to the body part
	 */
	public static float getTotalRemainingHealing(Player player, BodyPartEnum bodyPart)
	{
		return internal.getTotalRemainingHealing(player, bodyPart);
	}

	/**
	 * Get the max health of a player's body limb, defined in its body damage capability. <br>
	 * @param player player
	 * @param bodyPart bodyPart enum
	 * @return max health of the body part
	 */
	public static float getMaxHealth(Player player, BodyPartEnum bodyPart)
	{
		return internal.getMaxHealth(player, bodyPart);
	}
}
