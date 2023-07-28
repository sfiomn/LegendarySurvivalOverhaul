package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.entity.player.PlayerEntity;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;

import java.util.Objects;

public class PlayerTemporaryModifier extends ModifierBase
{
	public PlayerTemporaryModifier()
	{
		super();

	}
	
	@Override
	public float getPlayerInfluence(PlayerEntity player)
	{
		float sum = 0.0f;

		if (player.hasEffect(EffectRegistry.HOT_FOOD.get())) {
			sum += 1 + Objects.requireNonNull(player.getEffect(EffectRegistry.HOT_FOOD.get())).getAmplifier();
		} else if (player.hasEffect(EffectRegistry.COLD_FOOD.get())) {
			sum -= 1 + Objects.requireNonNull(player.getEffect(EffectRegistry.COLD_FOOD.get())).getAmplifier();
		}

		if (player.hasEffect(EffectRegistry.HOT_DRINk.get())) {
			sum += 1 + Objects.requireNonNull(player.getEffect(EffectRegistry.HOT_DRINk.get())).getAmplifier();
		} else if (player.hasEffect(EffectRegistry.COLD_DRINK.get())) {
			sum -= 1 + Objects.requireNonNull(player.getEffect(EffectRegistry.COLD_DRINK.get())).getAmplifier();
		}

		// LegendarySurvivalOverhaul.LOGGER.debug("Temporary temp influence : " + sum);
		return sum;
	}
}
