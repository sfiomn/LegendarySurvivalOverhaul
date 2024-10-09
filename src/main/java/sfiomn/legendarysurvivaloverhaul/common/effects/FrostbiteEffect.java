package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.api.ModDamageTypes;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.DamageSourceUtil;
import sfiomn.legendarysurvivaloverhaul.util.DamageUtil;
import net.minecraft.world.entity.player.Player;

public class FrostbiteEffect extends MobEffect
{
	public FrostbiteEffect()
	{
		super(MobEffectCategory.HARMFUL, 9164281);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void applyEffectTick(@NotNull LivingEntity entity, int amplifier)
	{
		if(entity instanceof Player player && !entity.hasEffect(MobEffectRegistry.COLD_IMMUNITY.get()))
		{
			Level level = entity.getCommandSenderWorld();
            if (DamageUtil.isModDangerous(level) && DamageUtil.healthAboveDifficulty(level, player) && !player.isSleeping())
			{
				player.hurt(DamageSourceUtil.getDamageSource(level, ModDamageTypes.HYPOTHERMIA), 1.0f);
			}
		}
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		int time = 50 >> amplifier;
		return time == 0 || duration % time == 0;
	}

	public static boolean playerIsImmuneToFrost(Player player)
	{
		return player.hasEffect(MobEffectRegistry.COLD_IMMUNITY.get()) || player.hasEffect(MobEffectRegistry.TEMPERATURE_IMMUNITY.get());
	}
}
