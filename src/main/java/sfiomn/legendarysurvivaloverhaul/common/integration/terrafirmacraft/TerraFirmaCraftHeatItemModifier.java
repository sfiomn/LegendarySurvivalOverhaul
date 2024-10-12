package sfiomn.legendarysurvivaloverhaul.common.integration.terrafirmacraft;

import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.temperature.AttributeModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;


public class TerraFirmaCraftHeatItemModifier extends AttributeModifierBase
{
	public TerraFirmaCraftHeatItemModifier()
	{

	}

	@Override
	public JsonTemperatureResistance getItemAttributes(ItemStack stack) {
		JsonTemperatureResistance config = new JsonTemperatureResistance();

		if (!LegendarySurvivalOverhaul.terraFirmaCraftLoaded)
			return config;

		if (Config.Baked.tfcItemHeatMultiplier == 0)
			return config;

		try
		{
			config.temperature = MathUtil.round(getUncaughtTemperature(stack), 1);
			// Inside a try/catch to make sure something weird
			// hasn't happened with the terrafirma code
			return config;
		}
		catch (Exception e)
		{
			// If an error somehow occurs, disable compatibility
			LegendarySurvivalOverhaul.LOGGER.error("An error has occurred with TerraFirmaCraft compatibility, disabling integration", e);
			LegendarySurvivalOverhaul.terraFirmaCraftLoaded = false;

			return config;
		}
	}

	private static float getUncaughtTemperature(ItemStack stack) {
		return (float) (HeatCapability.getTemperature(stack) * Config.Baked.tfcItemHeatMultiplier);
	}
}
