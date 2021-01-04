package icey.survivaloverhaul.common.effects;

import icey.survivaloverhaul.Main;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.entity.LivingEntity;

public class GenericEffect extends Effect
{

	public GenericEffect(int liquidColorIn, String name, EffectType type)
	{
		super(type, liquidColorIn);
		this.setRegistryName(Main.MOD_ID, name);
	}
	
	@Override
	public void performEffect(LivingEntity entity, int amplifier)
	{
		
	}
}
