package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class ThirstEffect extends GenericEffect
{
	public ThirstEffect()
	{
		super(10870382, EffectType.HARMFUL);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		if(entity instanceof PlayerEntity)
		{
			ThirstCapability thirstCapability = CapabilityUtil.getThirstCapability((PlayerEntity) entity);

			// Twice strength of Hunger effect
			thirstCapability.addThirstExhaustion(Config.Baked.thirstEffectModifier * (float)(amplifier + 1));
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		// Apply thirsty effect every 50 ticks for amplifier 0
		int time = 50 >> amplifier;
		return time == 0 || duration % time == 0;
	}
}
