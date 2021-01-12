package icey.survivaloverhaul.common.effects;

import icey.survivaloverhaul.api.DamageSources;
import icey.survivaloverhaul.common.DamageUtil;
import icey.survivaloverhaul.setup.EffectRegistry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.world.World;

public class FrostbiteEffect extends GenericEffect
{
	public FrostbiteEffect()
	{
		super(9164281, "frostbite", EffectType.HARMFUL);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void performEffect(LivingEntity entity, int amplifier)
	{
		if(entity instanceof PlayerEntity && !entity.isPotionActive(EffectRegistry.ModEffects.HEAT_RESISTANCE))
		{
			World world = entity.getEntityWorld();
			PlayerEntity player = (PlayerEntity) entity;
			
			if (DamageUtil.isModDangerous(world) && DamageUtil.healthAboveDifficulty(world, player) && !player.isSleeping())
			{
				player.attackEntityFrom(DamageSources.HYPOTHERMIA, 1.0f);
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
