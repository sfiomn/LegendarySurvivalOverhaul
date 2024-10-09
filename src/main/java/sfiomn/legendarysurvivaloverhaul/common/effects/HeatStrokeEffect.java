package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.api.ModDamageTypes;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.DamageSourceUtil;
import sfiomn.legendarysurvivaloverhaul.util.DamageUtil;
import net.minecraft.world.entity.player.Player;

public class HeatStrokeEffect extends MobEffect
{

	public HeatStrokeEffect()
	{
		super(MobEffectCategory.HARMFUL, 16756041);
	}
	
	@Override
	public void applyEffectTick(@NotNull LivingEntity entity, int amplifier)
	{
		if(entity instanceof Player player && !entity.hasEffect(MobEffectRegistry.HEAT_IMMUNITY.get()))
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
		return player.hasEffect(MobEffectRegistry.HEAT_IMMUNITY.get()) || player.hasEffect(MobEffectRegistry.TEMPERATURE_IMMUNITY.get());
	}
}
