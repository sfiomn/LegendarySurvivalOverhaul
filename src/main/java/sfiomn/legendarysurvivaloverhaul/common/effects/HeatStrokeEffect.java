package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.potion.Effects;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.DamageSources;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.DamageUtil;
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
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		if(entity instanceof PlayerEntity && !entity.hasEffect(EffectRegistry.HEAT_RESISTANCE.get()))
		{
			World world = entity.getCommandSenderWorld();
			PlayerEntity player = (PlayerEntity) entity;
			
			if (DamageUtil.isModDangerous(world) && DamageUtil.healthAboveDifficulty(world, player) && !player.isSleeping())
			{
				player.hurt(DamageSources.HYPERTHERMIA, 1.0f);
			}
		}
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		int time = 50 >> amplifier;
		return time == 0 || duration % time == 0;
	}

	public static boolean playerIsImmuneToHeat(PlayerEntity player)
	{
		return player.hasEffect(EffectRegistry.HEAT_RESISTANCE.get()) || player.hasEffect(Effects.FIRE_RESISTANCE);
	}
}
