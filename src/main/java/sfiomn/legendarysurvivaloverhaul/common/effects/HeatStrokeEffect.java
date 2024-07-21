package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.ModDamageTypes;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.DamageSourceUtil;
import sfiomn.legendarysurvivaloverhaul.util.DamageUtil;
import net.minecraft.world.entity.player.Player;

public class HeatStrokeEffect extends GenericEffect
{

	public HeatStrokeEffect()
	{
		super(16756041, MobEffectCategory.HARMFUL);
	}
	
	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		if(entity instanceof Player player && !entity.hasEffect(MobEffectRegistry.HEAT_RESISTANCE.get()))
		{
			Level level = entity.getCommandSenderWorld();

            if (DamageUtil.isModDangerous(level) && DamageUtil.healthAboveDifficulty(level, player) && !player.isSleeping())
			{
				player.hurt(DamageSourceUtil.getDamageSource(level, ModDamageTypes.HYPERTHERMIA), 1.0f);
			}
		}
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		int time = 50 >> amplifier;
		return time == 0 || duration % time == 0;
	}

	public static boolean playerIsImmuneToHeat(Player player)
	{
		return player.hasEffect(MobEffectRegistry.HEAT_RESISTANCE.get()) || player.hasEffect(MobEffects.FIRE_RESISTANCE);
	}
}
