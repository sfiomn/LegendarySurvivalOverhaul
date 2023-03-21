package sfiomn.legendarysurvivaloverhaul.config.json;

import com.google.common.collect.Maps;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.config.json.JsonItemIdentity;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonConfig
{
	public static Map<String, List<JsonArmorIdentity>> armorTemperatures = Maps.newHashMap();
	public static Map<String, List<JsonPropertyTemperature>> blockTemperatures = Maps.newHashMap();
	public static Map<String, JsonTemperature> fluidTemperatures = Maps.newHashMap();
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
			
			currentList.add(result);
			return true;
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
			
			currentList.add(result);
			return true;
		}
	}
	// JsonArmorIdentity
	
	public static void registerArmorTemperature(String registryName, float temperature)
	{
		registerArmorTemperature(registryName, temperature, new JsonItemIdentity(null));
	}
	
	public static void registerArmorTemperature(String registryName, float temperature, JsonItemIdentity identity)
	{
		if(!armorTemperatures.containsKey(registryName))
				armorTemperatures.put(registryName, new ArrayList<JsonArmorIdentity>());
		
		final List<JsonArmorIdentity> currentList = armorTemperatures.get(registryName);
		
		JsonArmorIdentity result = new JsonArmorIdentity(temperature, identity);
		
		for (int i = 0; i < currentList.size(); i++)
		{
			JsonTemperatureIdentity jtm = currentList.get(i);
			
			if (jtm.matches(identity))
			{
				currentList.set(i, result);
				return;
			}
		}
		
		currentList.add(result);
	}
	
	public static void registerConsumableTemperature(String group, String registryName, float temperature, int duration, JsonItemIdentity identity)
	{
		if (!consumableTemperature.containsKey(registryName))
			consumableTemperature.put(registryName, new ArrayList<JsonConsumableTemperature>());
		
		final List<JsonConsumableTemperature> currentList = consumableTemperature.get(registryName);
		
		JsonConsumableTemperature result = new JsonConsumableTemperature(group, temperature, duration, identity);
		
		for (int i = 0; i < currentList.size(); i++)
		{
			JsonConsumableTemperature jct = currentList.get(i);
			if (jct.matches(identity))
			{
				currentList.set(i, result);
				return;
			}
		}
		
		currentList.add(result);
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
