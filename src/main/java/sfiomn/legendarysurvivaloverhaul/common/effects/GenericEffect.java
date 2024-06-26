package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class GenericEffect extends MobEffect
{

	public GenericEffect(int liquidColorIn, MobEffectCategory type)
	{
		super(type, liquidColorIn);
	}
	
	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return false;
	}
}
