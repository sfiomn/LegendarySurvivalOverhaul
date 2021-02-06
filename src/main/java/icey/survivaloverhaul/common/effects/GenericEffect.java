package icey.survivaloverhaul.common.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.entity.LivingEntity;

public class GenericEffect extends Effect
{

	public GenericEffect(int liquidColorIn, EffectType type)
	{
		super(type, liquidColorIn);
	}
	
	@Override
	public void performEffect(LivingEntity entity, int amplifier)
	{
		
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) 
	{
		return false;
	}
}
