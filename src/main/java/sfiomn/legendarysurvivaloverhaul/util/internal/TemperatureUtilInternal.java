package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;


public class TemperatureUtilInternal implements ITemperatureUtil
{
	private final String PADDING_TAG = "ArmorPadding";
	
	@Override
	public int getPlayerTargetTemperature(PlayerEntity player)
	{
		float sum = 0.0f;
		World world = player.getCommandSenderWorld();
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
		
		return Math.round(sum);
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
	public void setArmorPaddingTag(ItemStack stack, String paddingId)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundNBT());
		}
		
		final CompoundNBT compound = stack.getTag();

		if (compound != null) {
			compound.putString(PADDING_TAG, paddingId);
		}
	}

	@Override
	public String getArmorPaddingTag(ItemStack stack)
	{
		if (stack.hasTag())
		{
			final CompoundNBT compound = stack.getTag();
			
			if (compound.contains(PADDING_TAG))
			{
				String tempTag = compound.getString(PADDING_TAG);
				
				return tempTag;
			}
		}
		return "";
	}

	@Override
	public void removeArmorPaddingTag(ItemStack stack)
	{
		if(stack.hasTag())
		{
			final CompoundNBT compound = stack.getTag();
			if (compound.contains(PADDING_TAG))
			{
				compound.remove(PADDING_TAG);
			}
		}
	}
}
