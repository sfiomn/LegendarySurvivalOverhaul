package sfiomn.legendarysurvivaloverhaul.config.json;

import com.google.common.collect.Maps;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.config.json.JsonPropertyValue;
import sfiomn.legendarysurvivaloverhaul.api.config.json.bodydamage.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.*;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonThirst;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.*;

public class JsonConfig
{
	public static Map<String, JsonTemperature> itemTemperatures = Maps.newHashMap();
	public static Map<String, List<JsonPropertyTemperature>> blockTemperatures = Maps.newHashMap();
	public static Map<String, JsonBiomeIdentity> biomeOverrides = Maps.newHashMap();
	public static Map<String, List<JsonConsumableTemperature>> consumableTemperature = Maps.newHashMap();
	public static Map<String, JsonThirst> consumableThirst = Maps.newHashMap();
	public static Map<String, JsonFuelItemIdentity> fuelItems = Maps.newHashMap();
	public static Map<String, JsonBodyPartsDamageSource> damageSourceBodyParts = Maps.newHashMap();
	
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
	public static void registerItemTemperature(String registryName, float temperature)
	{
		if(!itemTemperatures.containsKey(registryName))
			itemTemperatures.put(registryName, new JsonTemperature(temperature));
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

	public static void registerConsumableThirst(String registryName, int hydration, float saturation) {
		registerConsumableThirst(registryName, hydration, saturation, 0);
	}

	public static void registerConsumableThirst(String registryName, int hydration, float saturation, float dirtiness) {
		if (!consumableThirst.containsKey(registryName)) {
			consumableThirst.put(registryName, new JsonThirst(hydration, saturation, dirtiness));
		}
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

	public static void registerFuelItems(String registryName, ThermalTypeEnum thermalType, int fuelValue) {
		if(!fuelItems.containsKey(registryName))
			fuelItems.put(registryName, new JsonFuelItemIdentity(thermalType, fuelValue));
	}

	public static void registerDamageSourceBodyParts(String damageSource, DamageDistributionEnum damageDistribution, List<BodyPartEnum> bodyParts) {
		if (!damageSourceBodyParts.containsKey(damageSource))
			damageSourceBodyParts.put(damageSource, new JsonBodyPartsDamageSource(damageDistribution, bodyParts));
	}
}
