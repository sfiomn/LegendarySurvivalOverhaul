package icey.survivaloverhaul.util.internal;

import icey.survivaloverhaul.api.temperature.*;
import icey.survivaloverhaul.util.WorldUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TemperatureUtilInternal implements ITemperatureUtil
{
	private final String TEMPERATURE_TAG = "ArmorTemp";
	
	@Override
	public int getPlayerTargetTemperature(PlayerEntity player)
	{
		float sum = 0.0f;
		World world = player.getEntityWorld();
		BlockPos pos = WorldUtil.getSidedBlockPos(world, player);
		
		for(ModifierBase modifier : GameRegistry.findRegistry(ModifierBase.class).getValues())
		{
			sum += modifier.getWorldInfluence(world, pos);
			sum += modifier.getPlayerInfluence(player);
		}
		
		for (DynamicModifierBase dynamicModifier : GameRegistry.findRegistry(DynamicModifierBase.class).getValues())
		{
			sum += dynamicModifier.applyDynamicWorldInfluence(world, pos, sum);
			sum += dynamicModifier.applyDynamicPlayerInfluence(player, sum);
		}
		
		return Math.round(sum);
	}

	@Override
	public int getWorldTemperature(World world, BlockPos pos)
	{
		float sum = 0.0f;
		
		for(ModifierBase modifier : GameRegistry.findRegistry(ModifierBase.class).getValues())
		{
			sum += modifier.getWorldInfluence(world, pos);
		}
		for (DynamicModifierBase dynamicModifier : GameRegistry.findRegistry(DynamicModifierBase.class).getValues())
		{
			sum += dynamicModifier.applyDynamicWorldInfluence(world, pos, sum);
		}
		
		return (int) sum;
	}

	@Override
	public int clampTemperature(int temperature)
	{
		return MathHelper.clamp(temperature, TemperatureEnum.FROSTBITE.getLowerBound(), TemperatureEnum.HEAT_STROKE.getUpperBound());
	}

	@Override
	public TemperatureEnum getTemperatureEnum(int temp)
	{
		for(TemperatureEnum tempEnum : TemperatureEnum.values())
		{
			if(tempEnum.matches(temp))
			{
				return tempEnum;
			}
		}
		
		// Temperature invalid, assume extremes
		if(temp < 0)
		{
			return TemperatureEnum.FROSTBITE;
		}
		else
		{
			return TemperatureEnum.HEAT_STROKE;
		}
	}

	@Override
	public void setArmorTemperatureTag(ItemStack stack, float temperature)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundNBT());
		}
		
		final CompoundNBT compound = stack.getTag();
		
		compound.putFloat(TEMPERATURE_TAG, temperature);
	}

	@Override
	public float getArmorTemperatureTag(ItemStack stack)
	{
		if (stack.hasTag())
		{
			final CompoundNBT compound = stack.getTag();
			
			if (compound.contains(TEMPERATURE_TAG))
			{
				float tempTag = compound.getFloat(TEMPERATURE_TAG);
				
				return tempTag;
			}
		}
		return 0.0f;
	}

	@Override
	public void removeArmorTemperatureTag(ItemStack stack)
	{
		if(stack.hasTag())
		{
			final CompoundNBT compound = stack.getTag();
			if (compound.contains(TEMPERATURE_TAG))
			{
				compound.remove(TEMPERATURE_TAG);
			}
		}
	}

}
