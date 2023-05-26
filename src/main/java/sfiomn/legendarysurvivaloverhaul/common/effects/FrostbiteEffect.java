package sfiomn.legendarysurvivaloverhaul.common.effects;

import sfiomn.legendarysurvivaloverhaul.api.DamageSources;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.world.World;

public class FrostbiteEffect extends GenericEffect
{
	public FrostbiteEffect()
	{
		super(9164281, EffectType.HARMFUL);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		if(entity instanceof PlayerEntity && !entity.hasEffect(EffectRegistry.COLD_RESISTANCE.get()))
		{
			World world = entity.getCommandSenderWorld();
			PlayerEntity player = (PlayerEntity) entity;
			
			if (DamageUtil.isModDangerous(world) && DamageUtil.healthAboveDifficulty(world, player) && !player.isSleeping())
			{
				player.hurt(DamageSources.HYPOTHERMIA, 1.0f);
			}
		}
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		int time = 50 >> amplifier;
		return time == 0 || duration % time == 0;
	}

	public static boolean playerIsImmuneToFrost(PlayerEntity player)
	{
		return player.hasEffect(EffectRegistry.COLD_RESISTANCE.get());
	}
}
