package sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.api.item.PaddingEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;

import java.util.Objects;

public class AdaptivePaddingModifier extends DynamicModifierBase
{
	public AdaptivePaddingModifier()
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

		String paddingId = TemperatureUtil.getArmorPaddingTag(stack);
		PaddingEnum padding = PaddingEnum.getFromId(paddingId);
		if (padding == null) {
			return sum;
		}

		int diff = (int) (currentTemperature - middleTemperature);

		if (diff > 0) {
			if (Objects.equals(padding.type(), "cooling") || Objects.equals(padding.type(), "thermal")) {
				sum -= Math.min(padding.modifier(), Math.abs(diff));
			}
		} else if (diff < 0) {
			if (Objects.equals(padding.type(), "heating") || Objects.equals(padding.type(), "thermal")) {
				sum += Math.min(padding.modifier(), Math.abs(diff));
			}
		}
		return sum;
	}
}
