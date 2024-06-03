package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DebugStickItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;


public class TemperatureUtilInternal implements ITemperatureUtil
{
	private final String COAT_TAG = "ArmorPadding";
	
	@Override
	public float getPlayerTargetTemperature(PlayerEntity player)
	{
		float sum = 0.0f;
		World world = player.getCommandSenderWorld();
		BlockPos pos = WorldUtil.getSidedBlockPos(world, player);
		
		for(ModifierBase modifier : GameRegistry.findRegistry(ModifierBase.class).getValues())
		{
			float worldInfluence = modifier.getWorldInfluence(world, pos);
			float playerInfluence = modifier.getPlayerInfluence(player);
			if (player.getMainHandItem().getItem() == Items.DEBUG_STICK) {
				LegendarySurvivalOverhaul.LOGGER.debug(modifier.getRegistryName() + " : world influence=" + worldInfluence + ", player influence=" + playerInfluence);
			}

			sum += worldInfluence + playerInfluence;
		}
		
		for (DynamicModifierBase dynamicModifier : GameRegistry.findRegistry(DynamicModifierBase.class).getValues())
		{
			float worldInfluence = dynamicModifier.applyDynamicWorldInfluence(world, pos, sum);
			float playerInfluence = dynamicModifier.applyDynamicPlayerInfluence(player, sum);
			if (player.getMainHandItem().getItem() == Items.DEBUG_STICK) {
				LegendarySurvivalOverhaul.LOGGER.debug(dynamicModifier.getRegistryName() + " : world influence=" + worldInfluence + ", player influence=" + playerInfluence);
			}

			sum += worldInfluence + playerInfluence;
		}
		return MathUtil.round(sum, 1);
	}

	@Override
	public float getWorldTemperature(World world, BlockPos pos)
	{
		float sum = 0.0f;

		for(ModifierBase modifier : GameRegistry.findRegistry(ModifierBase.class).getValues())
		{
			// LegendarySurvivalOverhaul.LOGGER.debug("tmp influence : " + modifier.getRegistryName() + ", " + modifier.getWorldInfluence(world, pos));
			sum += modifier.getWorldInfluence(world, pos);
		}
		for (DynamicModifierBase dynamicModifier : GameRegistry.findRegistry(DynamicModifierBase.class).getValues())
		{
			// LegendarySurvivalOverhaul.LOGGER.debug("tmp influence : " + dynamicModifier.getRegistryName() + ", " + dynamicModifier.applyDynamicWorldInfluence(world, pos, sum));
			sum += dynamicModifier.applyDynamicWorldInfluence(world, pos, sum);
		}

		return MathUtil.round(sum, 1);
	}

	@Override
	public float clampTemperature(float temperature)
	{
		return MathHelper.clamp(temperature, TemperatureEnum.FROSTBITE.getLowerBound(), TemperatureEnum.HEAT_STROKE.getUpperBound());
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
			stack.setTag(new CompoundNBT());
		}
		
		final CompoundNBT compound = stack.getTag();

		if (compound != null) {
			compound.putString(COAT_TAG, coatId);
		}
	}

	@Override
	public String getArmorCoatTag(ItemStack stack)
	{
		if (stack.hasTag())
		{
			final CompoundNBT compound = stack.getTag();
			
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
			final CompoundNBT compound = stack.getTag();
			if (compound != null && compound.contains(COAT_TAG))
			{
				compound.remove(COAT_TAG);
			}
		}
	}
}
