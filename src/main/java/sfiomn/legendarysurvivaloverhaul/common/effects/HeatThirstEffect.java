package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class HeatThirstEffect extends MobEffect
{
	public HeatThirstEffect()
	{
		super(MobEffectCategory.HARMFUL, 10870382);
	}
	
	@Override
	public void applyEffectTick(@NotNull LivingEntity entity, int amplifier)
	{
		if(entity instanceof Player)
		{
			ThirstCapability thirstCapability = CapabilityUtil.getThirstCapability((Player) entity);

			// Twice strength of Hunger effect
			thirstCapability.addThirstExhaustion((float) (Config.Baked.heatThirstEffectModifier * (amplifier + 1)));
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		// Apply thirsty effect every 50 ticks for amplifier 0
		int time = 50 >> amplifier;
		return time == 0 || duration % time == 0;
	}
}
