package icey.survivaloverhaul.common.effects;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.DamageSources;
import icey.survivaloverhaul.common.DamageUtil;
import icey.survivaloverhaul.setup.EffectRegistry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.world.World;

public class HypothermiaEffect extends GenericEffect
{

	public HypothermiaEffect()
	{
		super(9164281, "hypothermia", EffectType.HARMFUL);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void performEffect(LivingEntity entity, int amplifier)
	{
		if(entity instanceof PlayerEntity && !entity.isPotionActive(EffectRegistry.HEAT_RESISTANCE))
		{
			World world = entity.getEntityWorld();
			PlayerEntity player = (PlayerEntity) entity;
			
			if (DamageUtil.isModDangerous(world) && DamageUtil.healthAboveDifficulty(world, player))
			{
				player.attackEntityFrom(DamageSources.HYPOTHERMIA, 1.0f);
			}
		}
	}
}
