package sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;

import java.util.Objects;

public class AdaptiveCoatModifier extends DynamicModifierBase
{
	public AdaptiveCoatModifier()
	{
		super();
	}
	
	@Override
	public float applyDynamicPlayerInfluence(PlayerEntity player, float currentTemperature)
	{
		float value = 0.0f;
		float diffToAverage = currentTemperature - TemperatureEnum.NORMAL.getMiddle();
		
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.HEAD), diffToAverage + value);
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.CHEST), diffToAverage + value);
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.LEGS), diffToAverage + value);
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.FEET), diffToAverage + value);
		
		return value;
	}
	
	private float checkArmorSlot(ItemStack stack, float remainingDiffToAverage)
	{
		if (stack.isEmpty())
				return 0.0f;

		if (remainingDiffToAverage == 0)
			return 0.0f;


		String coatId = TemperatureUtil.getArmorCoatTag(stack);
		CoatEnum coat = CoatEnum.getFromId(coatId);
		if (coat == null) {
			return 0.0f;
		}

		float sum = 0.0f;

		if (remainingDiffToAverage > 0) {
			if (Objects.equals(coat.type(), "cooling") || Objects.equals(coat.type(), "thermal")) {
				sum -= (float) Math.min(coat.modifier(), Math.abs(remainingDiffToAverage));
			}
		} else if (remainingDiffToAverage < 0) {
			if (Objects.equals(coat.type(), "heating") || Objects.equals(coat.type(), "thermal")) {
				sum += (float) Math.min(coat.modifier(), Math.abs(remainingDiffToAverage));
			}
		}
		return sum;
	}
}
