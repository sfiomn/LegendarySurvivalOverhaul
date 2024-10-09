package sfiomn.legendarysurvivaloverhaul.common.temperature.attribute;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
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
			JsonTemperatureResistance tempConfig = JsonConfig.itemTemperatures.get(itemRegistryName.toString());
			config.temperature += tempConfig.temperature;
			config.heatResistance += tempConfig.heatResistance;
			config.coldResistance += tempConfig.coldResistance;
			config.thermalResistance += tempConfig.thermalResistance;
		}

		return config;
	}
}
