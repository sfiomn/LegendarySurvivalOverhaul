package sfiomn.legendarysurvivaloverhaul.common.integration.json;

import com.google.common.collect.Maps;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperatureResistance;

import java.util.Map;

public class JsonIntegrationConfig
{
	public static Map<String, JsonTemperatureResistance> originsTemperatures = Maps.newHashMap();

	public static void registerOriginsTemperature(String registryName, float temperature, float heatResistance, float coldResistance, float thermalResistance) {
		if(!originsTemperatures.containsKey(registryName))
			originsTemperatures.put(registryName, new JsonTemperatureResistance(temperature, heatResistance, coldResistance, thermalResistance));
	}
}
