package sfiomn.legendarysurvivaloverhaul.config.json;

import com.google.common.collect.Maps;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.*;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonConfig
{
	public static Map<String, JsonTemperature> itemTemperatures = Maps.newHashMap();
	public static Map<String, List<JsonPropertyTemperature>> blockTemperatures = Maps.newHashMap();
	public static Map<String, JsonBiomeIdentity> biomeOverrides = Maps.newHashMap();
	public static Map<String, List<JsonConsumableTemperature>> consumableTemperature = Maps.newHashMap();
	public static Map<String, JsonFuelItemIdentity> fuelItems = Maps.newHashMap();
	
	public static boolean registerBlockTemperature(String registryName, float temperature, JsonPropertyValue... properties)
	{
		if (!blockTemperatures.containsKey(registryName))
			blockTemperatures.put(registryName, new ArrayList<>());
		
		final List<JsonPropertyTemperature> currentList = blockTemperatures.get(registryName);
		
		JsonPropertyTemperature result = new JsonPropertyTemperature(temperature, properties);
		
		if (properties.length > 0)
		{
			for (int i = 0; i < currentList.size(); i++)
			{
				JsonPropertyTemperature jpt = currentList.get(i);
				if (jpt.matchesDescribedProperties(properties))
				{
					currentList.set(i, result);
					return true;
				}
			}

		}
		else 
		{
			for (JsonPropertyTemperature jpt : currentList) {
				if (jpt.properties.keySet().size() > 0) {
					return false;
				}
			}
			for (int i = 0; i < currentList.size(); i++)
			{
				JsonPropertyTemperature jpt = currentList.get(i);
				if (jpt.properties.keySet().size() == 0)
				{
					currentList.set(i, result);
					return true;
				}
			}

		}
		currentList.add(result);
		return true;
	}
	public static void registerArmorTemperature(String registryName, float temperature)
	{
		if(!armorTemperatures.containsKey(registryName))
			armorTemperatures.put(registryName, new JsonTemperature(temperature));
	}
	
	public static boolean registerConsumableTemperature(TemporaryModifierGroupEnum group, String registryName, int temperatureLevel, int duration)
	{
		if (!consumableTemperature.containsKey(registryName)) {
			consumableTemperature.put(registryName, new ArrayList<>());
		}

		final List<JsonConsumableTemperature> currentList = consumableTemperature.get(registryName);

		JsonConsumableTemperature jsonConsumableTemperature = new JsonConsumableTemperature(group, temperatureLevel, duration);

		for (int i = 0; i < currentList.size(); i++)
		{
			JsonConsumableTemperature jct = currentList.get(i);
			if (Objects.equals(jct.group, jsonConsumableTemperature.group))
			{
				currentList.set(i, jsonConsumableTemperature);
				return true;
			}
		}

		currentList.add(jsonConsumableTemperature);
		return true;
	}
	
	public static void registerFluidTemperature(String registryName, float temperature)
	{
		fluidTemperatures.put(registryName, new JsonTemperature(temperature));
	}
	
	public static void registerBiomeOverride(String registryName, float temperature)
	{
		registerBiomeOverride(registryName, temperature, false);
	}
	
	public static void registerBiomeOverride(String registryName, float temperature, boolean isDry)
	{
		if(!biomeOverrides.containsKey(registryName))
			biomeOverrides.put(registryName, new JsonBiomeIdentity(temperature, isDry));
	}

	public static void registerFuelItems(String registryName, ThermalTypeEnum thermalType, int fuelValue)
	{
		if(!fuelItems.containsKey(registryName))
			fuelItems.put(registryName, new JsonFuelItemIdentity(thermalType, fuelValue));
	}
}
