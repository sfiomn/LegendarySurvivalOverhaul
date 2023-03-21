package sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
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
		
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.HEAD), currentTemperature);
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.CHEST), currentTemperature);
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.LEGS), currentTemperature);
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.FEET), currentTemperature);
		
		return value;
	}
	
	private float checkArmorSlot(ItemStack stack, float currentTemperature)
	{
		if (stack.isEmpty())
				return 0.0f;

		float sum = 0.0f;

		String coatId = TemperatureUtil.getArmorCoatTag(stack);
		CoatEnum coat = CoatEnum.getFromId(coatId);
		if (coat == null) {
			return sum;
		}

		int diff = (int) (currentTemperature - middleTemperature);

		if (diff > 0) {
			if (Objects.equals(coat.type(), "cooling") || Objects.equals(coat.type(), "thermal")) {
				sum -= Math.min(coat.modifier(), Math.abs(diff));
			}
		} else if (diff < 0) {
			if (Objects.equals(coat.type(), "heating") || Objects.equals(coat.type(), "thermal")) {
				sum += Math.min(coat.modifier(), Math.abs(diff));
			}
		}
		return sum;
	}
}
