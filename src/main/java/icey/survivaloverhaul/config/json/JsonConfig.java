package icey.survivaloverhaul.config.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import icey.survivaloverhaul.api.config.json.JsonItemIdentity;
import icey.survivaloverhaul.api.config.json.temperature.JsonBiomeIdentity;
import icey.survivaloverhaul.api.config.json.temperature.JsonPropertyTemperature;
import icey.survivaloverhaul.api.config.json.temperature.JsonPropertyValue;
import icey.survivaloverhaul.api.config.json.temperature.JsonTemperature;
import icey.survivaloverhaul.api.config.json.temperature.JsonTemperatureIdentity;

public class JsonConfig
{
	public static Map<String, List<JsonTemperatureIdentity>> armorTemperatures = Maps.newHashMap();
	public static Map<String, List<JsonPropertyTemperature>> blockTemperatures = Maps.newHashMap();
	public static Map<String, JsonTemperature> fluidTemperatures = Maps.newHashMap();
	public static Map<String, JsonBiomeIdentity> biomeOverrides = Maps.newHashMap();
	
	public static boolean registerBlockTemperature(String registryName, float temperature, JsonPropertyValue... properties)
	{
		if (!blockTemperatures.containsKey(registryName))
			blockTemperatures.put(registryName, new ArrayList<JsonPropertyTemperature>());
		
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
			for (int i = 0; i < currentList.size(); i++)
			{
				JsonPropertyTemperature jpt = currentList.get(i);
				if (jpt.properties.keySet().size() > 0)
				{
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

	
	public static void registerArmorTemperature(String registryName, float temperature)
	{
		registerArmorTemperature(registryName, temperature, new JsonItemIdentity(null));
	}
	
	public static void registerArmorTemperature(String registryName, float temperature, JsonItemIdentity identity)
	{
		if(!armorTemperatures.containsKey(registryName))
				armorTemperatures.put(registryName, new ArrayList<JsonTemperatureIdentity>());
		
		final List<JsonTemperatureIdentity> currentList = armorTemperatures.get(registryName);
		
		JsonTemperatureIdentity result = new JsonTemperatureIdentity(temperature, identity);
		
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
	
	public static void registerFluidTemperature(String registryName, float temperature)
	{
		fluidTemperatures.put(registryName, new JsonTemperature(temperature));
	}
	
	public static void registerBiomeOverride(String registryName, float temperature, boolean isDry)
	{
		if(!biomeOverrides.containsKey(registryName))
				biomeOverrides.put(registryName, new JsonBiomeIdentity(temperature, isDry));
	}
}
