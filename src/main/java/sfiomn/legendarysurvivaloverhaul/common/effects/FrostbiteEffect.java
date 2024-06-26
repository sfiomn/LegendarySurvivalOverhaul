package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.ModDamageTypes;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.DamageSourceUtil;
import sfiomn.legendarysurvivaloverhaul.util.DamageUtil;
import net.minecraft.world.entity.player.Player;

public class FrostbiteEffect extends GenericEffect
{
	public FrostbiteEffect()
	{
		super(9164281, MobEffectCategory.HARMFUL);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		if(entity instanceof Player && !entity.hasEffect(MobEffectRegistry.COLD_RESISTANCE.get()))
		{
			Level level = entity.getCommandSenderWorld();
			Player player = (Player) entity;
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
		return player.hasEffect(MobEffectRegistry.COLD_RESISTANCE.get());
	}
}
