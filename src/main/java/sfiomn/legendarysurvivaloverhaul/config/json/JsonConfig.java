package sfiomn.legendarysurvivaloverhaul.config.json;

import com.google.common.collect.Maps;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.config.json.JsonPropertyValue;
import sfiomn.legendarysurvivaloverhaul.api.config.json.bodydamage.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.api.config.json.bodydamage.JsonConsumableHeal;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.*;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.*;

public class JsonConfig
{
	public static Map<String, JsonBiomeIdentity> biomeOverrides = Maps.newHashMap();
	public static Map<String, JsonTemperature> itemTemperatures = Maps.newHashMap();
	public static Map<String, List<JsonPropertyTemperature>> blockTemperatures = Maps.newHashMap();
	public static Map<String, JsonFuelItemIdentity> fuelItems = Maps.newHashMap();
	public static Map<String, List<JsonConsumableTemperature>> consumableTemperature = Maps.newHashMap();
	public static Map<String, JsonConsumableThirst> consumableThirst = Maps.newHashMap();
	public static Map<String, JsonConsumableHeal> consumableHeal = Maps.newHashMap();
	public static Map<String, JsonBodyPartsDamageSource> damageSourceBodyParts = Maps.newHashMap();


	public static void registerBiomeOverride(String registryName, float temperature)
	{
		registerBiomeOverride(registryName, temperature, false);
	}

	public static void registerBiomeOverride(String registryName, float temperature, boolean isDry)
	{
		if(!biomeOverrides.containsKey(registryName))
			biomeOverrides.put(registryName, new JsonBiomeIdentity(temperature, isDry));
	}

	public static void registerBlockTemperature(String registryName, float temperature, JsonPropertyValue... properties)
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
					return;
				}
			}

		}
		else 
		{
			for (JsonPropertyTemperature jpt : currentList) {
				if (!jpt.properties.keySet().isEmpty()) {
					return;
				}
			}
			for (int i = 0; i < currentList.size(); i++)
			{
				JsonPropertyTemperature jpt = currentList.get(i);
				if (jpt.properties.keySet().isEmpty())
				{
					currentList.set(i, result);
					return;
				}
			}

		}
		currentList.add(result);
	}

	public static void registerFuelItems(String registryName, ThermalTypeEnum thermalType, int fuelValue) {
		if(!fuelItems.containsKey(registryName))
			fuelItems.put(registryName, new JsonFuelItemIdentity(thermalType, fuelValue));
	}

	public static void registerItemTemperature(String registryName, float temperature)
	{
		if(!itemTemperatures.containsKey(registryName))
			itemTemperatures.put(registryName, new JsonTemperature(temperature));
	}
	
	public static void registerConsumableTemperature(TemporaryModifierGroupEnum group, String registryName, int temperatureLevel, int duration)
	{
		if (!consumableTemperature.containsKey(registryName)) {
			consumableTemperature.put(registryName, new ArrayList<>());
		}

		final List<JsonConsumableTemperature> currentList = consumableTemperature.get(registryName);

		JsonConsumableTemperature jsonConsumableTemperature = new JsonConsumableTemperature(group, temperatureLevel, duration);

		if (temperatureLevel == 0) {
			LegendarySurvivalOverhaul.LOGGER.debug("Error with consumable " + registryName + " : temperature can't be 0");
			return;
		}

		for (int i = 0; i < currentList.size(); i++)
		{
			JsonConsumableTemperature jct = currentList.get(i);
			if (Objects.equals(jct.group, jsonConsumableTemperature.group))
			{
				currentList.set(i, jsonConsumableTemperature);
				return;
			}
		}

		currentList.add(jsonConsumableTemperature);
	}

	public static void registerConsumableThirst(String registryName, int hydration, float saturation) {
		registerConsumableThirst(registryName, hydration, saturation, 0, "");
	}

	public static void registerConsumableThirst(String registryName, int hydration, float saturation, float effectChance, String effect) {
		if (!consumableThirst.containsKey(registryName)) {
			consumableThirst.put(registryName, new JsonConsumableThirst(hydration, saturation, effectChance, effect));
		}
	}

	public static void registerConsumableHeal(String registryName, int healingCharges, float healingValue, int healingTime) {
		if (!consumableHeal.containsKey(registryName))
			if (healingCharges < 0)
				LegendarySurvivalOverhaul.LOGGER.debug("Error with consumable " + registryName + " : healing charges can't be negative");
			else
				consumableHeal.put(registryName, new JsonConsumableHeal(healingCharges, healingValue, healingTime));
	}

	public static void registerDamageSourceBodyParts(String damageSource, DamageDistributionEnum damageDistribution, List<BodyPartEnum> bodyParts) {
		if (!damageSourceBodyParts.containsKey(damageSource))
			damageSourceBodyParts.put(damageSource, new JsonBodyPartsDamageSource(damageDistribution, bodyParts));
	}
}
