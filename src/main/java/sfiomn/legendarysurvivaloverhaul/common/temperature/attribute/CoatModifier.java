package sfiomn.legendarysurvivaloverhaul.common.temperature.attribute;

import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.AttributeModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;

public class CoatModifier extends AttributeModifierBase
{
	public CoatModifier() {}

	@Override
	public JsonTemperatureResistance getItemAttributes(ItemStack stack)
	{
		JsonTemperatureResistance config = new JsonTemperatureResistance();
		if (!TemperatureUtil.getArmorCoatTag(stack).isEmpty()) {
			CoatEnum coat = CoatEnum.getFromId(TemperatureUtil.getArmorCoatTag(stack));
			if (coat != null) {
				if (coat.type().equals("cooling"))
					config.heatResistance += (float) coat.modifier();
				if (coat.type().equals("heating"))
					config.coldResistance += (float) coat.modifier();
				if (coat.type().equals("thermal"))
					config.thermalResistance += (float) coat.modifier();
			}
		}
		return config;
	}
}
