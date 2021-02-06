package icey.survivaloverhaul.common.effects;

import icey.survivaloverhaul.api.DamageSources;
import icey.survivaloverhaul.registry.EffectRegistry;
import icey.survivaloverhaul.util.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.world.World;

public class HeatStrokeEffect extends GenericEffect
{

	public HeatStrokeEffect()
	{
		super(16756041, EffectType.HARMFUL);
	}
	
	@Override
	public void performEffect(LivingEntity entity, int amplifier)
	{
		if(entity instanceof PlayerEntity && !entity.isPotionActive(EffectRegistry.HEAT_RESISTANCE.get()))
		{
			World world = entity.getEntityWorld();
			PlayerEntity player = (PlayerEntity) entity;
			
			if (DamageUtil.isModDangerous(world) && DamageUtil.healthAboveDifficulty(world, player) && !player.isSleeping())
			{
				player.attackEntityFrom(DamageSources.HYPERTHERMIA, 1.0f);
			}
		}
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) 
	{
		int time = 50 >> amplifier;
		return time > 0 ? duration % time == 0 : true;
	}
}
