package icey.survivaloverhaul.common.effects;

import icey.survivaloverhaul.api.DamageSources;
import icey.survivaloverhaul.common.DamageUtil;
import icey.survivaloverhaul.setup.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.world.World;

public class HyperthermiaEffect extends GenericEffect
{

	public HyperthermiaEffect()
	{
		super(9164281, "hyperthermia", EffectType.HARMFUL);
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
				player.attackEntityFrom(DamageSources.HYPERTHERMIA, 1.0f);
			}
		}
	}

}
