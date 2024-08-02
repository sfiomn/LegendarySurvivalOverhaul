package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

import static sfiomn.legendarysurvivaloverhaul.registry.TemperatureModifierRegistry.*;


public class TemperatureUtilInternal implements ITemperatureUtil
{
	private final String COAT_TAG = "ArmorPadding";
	
	@Override
	public float getPlayerTargetTemperature(Player player)
	{
		float sum = 0.0f;
		Level world = player.getCommandSenderWorld();
		BlockPos pos = WorldUtil.getSidedBlockPos(world, player);
		
		for(ModifierBase modifier : MODIFIERS_REGISTRY.get().getValues())
		{
			float worldInfluence = modifier.getWorldInfluence(world, pos);
			float playerInfluence = modifier.getPlayerInfluence(player);
			if (player.getMainHandItem().is(Items.DEBUG_STICK)) {
				LegendarySurvivalOverhaul.LOGGER.debug(MODIFIERS_REGISTRY.get().getKey(modifier) + " : world influence=" + worldInfluence + ", player influence=" + playerInfluence);
			}

			sum += worldInfluence + playerInfluence;
		}
		
		for (DynamicModifierBase dynamicModifier : DYNAMIC_MODIFIERS_REGISTRY.get().getValues())
		{
			float worldInfluence = dynamicModifier.applyDynamicWorldInfluence(world, pos, sum);
			float playerInfluence = dynamicModifier.applyDynamicPlayerInfluence(player, sum);
			if (player.getMainHandItem().is(Items.DEBUG_STICK)) {
				LegendarySurvivalOverhaul.LOGGER.debug(DYNAMIC_MODIFIERS_REGISTRY.get().getKey(dynamicModifier) + " : world influence=" + worldInfluence + ", player influence=" + playerInfluence);
			}

			sum += worldInfluence + playerInfluence;
		}
		return MathUtil.round(sum, 1);
	}

	@Override
	public float getWorldTemperature(Level world, BlockPos pos)
	{
		float sum = 0.0f;

		for(ModifierBase modifier : MODIFIERS_REGISTRY.get().getValues())
		{
			// LegendarySurvivalOverhaul.LOGGER.debug("tmp influence : " + modifier.getRegistryName() + ", " + modifier.getWorldInfluence(world, pos));
			sum += modifier.getWorldInfluence(world, pos);
		}
		for (DynamicModifierBase dynamicModifier : DYNAMIC_MODIFIERS_REGISTRY.get().getValues())
		{
			// LegendarySurvivalOverhaul.LOGGER.debug("tmp influence : " + dynamicModifier.getRegistryName() + ", " + dynamicModifier.applyDynamicWorldInfluence(world, pos, sum));
			sum += dynamicModifier.applyDynamicWorldInfluence(world, pos, sum);
		}

		return MathUtil.round(sum, 1);
	}

	@Override
	public float clampTemperature(float temperature)
	{
		return Mth.clamp(temperature, TemperatureEnum.FROSTBITE.getLowerBound(), TemperatureEnum.HEAT_STROKE.getUpperBound());
	}

	@Override
	public TemperatureEnum getTemperatureEnum(float temperature)
	{
		return TemperatureEnum.get(temperature);
	}

	@Override
	public void setArmorCoatTag(ItemStack stack, String coatId)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundTag());
		}
		
		final CompoundTag compound = stack.getTag();

		if (compound != null) {
			compound.putString(COAT_TAG, coatId);
		}
	}

	@Override
	public String getArmorCoatTag(ItemStack stack)
	{
		if (stack.hasTag())
		{
			final CompoundTag compound = stack.getTag();
			
			if (compound != null && compound.contains(COAT_TAG))
			{
				String tempTag = compound.getString(COAT_TAG);
				
				return tempTag;
			}
		}
		return "";
	}

	@Override
	public void removeArmorCoatTag(ItemStack stack)
	{
		if(stack.hasTag())
		{
			final CompoundTag compound = stack.getTag();
			if (compound != null && compound.contains(COAT_TAG))
			{
				compound.remove(COAT_TAG);
			}
		}
	}
}
