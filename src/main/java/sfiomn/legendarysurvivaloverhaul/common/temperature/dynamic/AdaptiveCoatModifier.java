package sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;

public class AdaptiveCoatModifier extends DynamicModifierBase
{
	public AdaptiveCoatModifier()
	{
		super();
	}
	
	@Override
	public float applyDynamicPlayerInfluence(Player player, float currentTemperature)
	{
		float value = 0.0f;
		float diffToAverage = currentTemperature - TemperatureEnum.NORMAL.getMiddle();
		
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlot.HEAD), diffToAverage + value);
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlot.CHEST), diffToAverage + value);
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlot.LEGS), diffToAverage + value);
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlot.FEET), diffToAverage + value);
		
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
			if (coat.type().equals("cooling") || coat.type().equals("thermal")) {
				sum -= (float) Math.min(coat.modifier(), Math.abs(remainingDiffToAverage));
			}
		} else if (remainingDiffToAverage < 0) {
			if (coat.type().equals("heating") || coat.type().equals("thermal")) {
				sum += (float) Math.min(coat.modifier(), Math.abs(remainingDiffToAverage));
			}
		}
		return sum;
	}
}
