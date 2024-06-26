package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.Objects;

public class PlayerTemporaryModifier extends ModifierBase
{
	public PlayerTemporaryModifier()
	{
		super();

	}
	
	@Override
	public float getPlayerInfluence(Player player)
	{
		float sum = 0.0f;

		if (player.hasEffect(MobEffectRegistry.HOT_FOOD.get())) {
			sum += 1 + Objects.requireNonNull(player.getEffect(MobEffectRegistry.HOT_FOOD.get())).getAmplifier();
		} else if (player.hasEffect(MobEffectRegistry.COLD_FOOD.get())) {
			sum -= 1 + Objects.requireNonNull(player.getEffect(MobEffectRegistry.COLD_FOOD.get())).getAmplifier();
		}

		if (player.hasEffect(MobEffectRegistry.HOT_DRINk.get())) {
			sum += 1 + Objects.requireNonNull(player.getEffect(MobEffectRegistry.HOT_DRINk.get())).getAmplifier();
		} else if (player.hasEffect(MobEffectRegistry.COLD_DRINK.get())) {
			sum -= 1 + Objects.requireNonNull(player.getEffect(MobEffectRegistry.COLD_DRINK.get())).getAmplifier();
		}

		// LegendarySurvivalOverhaul.LOGGER.debug("Temporary temp influence : " + sum);
		return sum;
	}
}
