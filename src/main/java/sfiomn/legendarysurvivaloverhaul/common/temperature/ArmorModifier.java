package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperature;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;

public class ArmorModifier extends ModifierBase
{
	public ArmorModifier()
	{
		super();
	}
	
	@Override
	public float getPlayerInfluence(PlayerEntity player)
	{
		float value = 0.0f;
		
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.HEAD));
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.CHEST));
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.LEGS));
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.FEET));
		// LegendarySurvivalOverhaul.LOGGER.debug("Armor temp influence : " + value);
		return value;
	}
	
	private float checkArmorSlot(ItemStack stack)
	{
		if (stack.isEmpty())
				return 0.0f;
		
		float sum = 0.0f;

		sum += processStackJson(stack);
		return sum;
	}
	
	private float processStackJson(ItemStack stack)
	{
		ResourceLocation itemRegistryName = stack.getItem().getRegistryName();

		JsonTemperature jsonTemperature = null;
		if (itemRegistryName != null)
			jsonTemperature = JsonConfig.itemTemperatures.get(itemRegistryName.toString());
		
		if (jsonTemperature != null)
		{
			return jsonTemperature.temperature;
		}
		
		return 0.0f;
	}
}
