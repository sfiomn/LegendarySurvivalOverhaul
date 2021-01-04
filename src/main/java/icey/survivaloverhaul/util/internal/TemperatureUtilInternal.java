package icey.survivaloverhaul.util.internal;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.*;
import icey.survivaloverhaul.common.temperature.ModifierBase;
import icey.survivaloverhaul.common.temperature.ModifierDynamicBase;
import icey.survivaloverhaul.util.WorldUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TemperatureUtilInternal implements ITemperatureUtil
{

	@Override
	public int getPlayerTargetTemperature(PlayerEntity player)
	{
		float sum = 0.0f;
		World world = player.getEntityWorld();
		BlockPos pos = WorldUtil.getSidedBlockPos(world, player);
		
		for(ModifierBase modifier : Main.MODIFIERS.getValues())
		{
			sum += modifier.getWorldInfluence(world, pos);
			sum += modifier.getPlayerInfluence(player);
		}
		
		for (ModifierDynamicBase dynamicModifier : Main.DYNAMIC_MODIFIERS.getValues())
		{
			sum += dynamicModifier.applyDynamicWorldInfluence(world, pos, sum);
			sum += dynamicModifier.applyDynamicPlayerInfluence(player, sum);
		}
		
		return (int) sum;
	}

	@Override
	public int getWorldTemperature(World world, BlockPos pos)
	{
		float sum = 0.0f;
		
		for(ModifierBase modifier : Main.MODIFIERS.getValues())
		{
			sum += modifier.getWorldInfluence(world, pos);
		}
		for (ModifierDynamicBase dynamicModifier : Main.DYNAMIC_MODIFIERS.getValues())
		{
			sum += dynamicModifier.applyDynamicWorldInfluence(world, pos, sum);
		}
		
		return (int) sum;
	}

	@Override
	public int clampTemperature(int temperature)
	{
		return MathHelper.clamp(temperature, TemperatureStateEnum.HYPOTHERMIA.getLowerBound(), TemperatureStateEnum.HYPERTHERMIA.getUpperBound());
	}

	@Override
	public TemperatureStateEnum getTemperatureEnum(int temp)
	{
		for(TemperatureStateEnum tempEnum : TemperatureStateEnum.values())
		{
			if(tempEnum.matches(temp))
			{
				return tempEnum;
			}
		}
		
		// Temperature invaled, assume extremes
		if(temp < 0)
		{
			return TemperatureStateEnum.HYPOTHERMIA;
		}
		else
		{
			return TemperatureStateEnum.HYPERTHERMIA;
		}
	}

	@Override
	public void setArmorTemperatureTag(ItemStack stack, float temperature)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getArmorTemperatureTag(ItemStack stack)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeArmorTemperatureTag(ItemStack stack)
	{
		// TODO Auto-generated method stub
		
	}

}
