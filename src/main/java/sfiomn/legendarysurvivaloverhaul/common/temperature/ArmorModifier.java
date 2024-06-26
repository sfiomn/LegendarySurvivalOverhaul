package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
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
	public float getPlayerInfluence(Player player)
	{
		float value = 0.0f;
		
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlot.HEAD));
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlot.CHEST));
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlot.LEGS));
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlot.FEET));
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
		ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(stack.getItem());

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
