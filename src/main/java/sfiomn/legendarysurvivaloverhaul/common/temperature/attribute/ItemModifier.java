package sfiomn.legendarysurvivaloverhaul.common.temperature.attribute;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.temperature.AttributeModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;

public class ItemModifier extends AttributeModifierBase
{
	public ItemModifier() {}

	@Override
	public JsonTemperatureResistance getItemAttributes(ItemStack stack)
	{
		ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(stack.getItem());
		JsonTemperatureResistance config = new JsonTemperatureResistance();

		if (itemRegistryName != null && JsonConfig.itemTemperatures.containsKey(itemRegistryName.toString())) {
			config = JsonConfig.itemTemperatures.get(itemRegistryName.toString());
		}

		return config;
	}
}
