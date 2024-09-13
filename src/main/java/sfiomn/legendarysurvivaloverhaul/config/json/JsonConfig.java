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
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonBlockFluidThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonEffectParameter;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;

import java.util.*;

public class JsonConfig
{
	public static Map<String, JsonTemperature> dimensionTemperatures = Maps.newHashMap();
	public static Map<String, JsonBiomeIdentity> biomeOverrides = Maps.newHashMap();
	public static Map<String, JsonTemperature> itemTemperatures = Maps.newHashMap();
	public static Map<String, List<JsonBlockFluidTemperature>> blockFluidTemperatures = Maps.newHashMap();
	public static Map<String, JsonTemperature> entityTemperatures = Maps.newHashMap();
	public static Map<String, List<JsonConsumableTemperature>> consumableTemperature = Maps.newHashMap();
	public static Map<String, JsonFuelItem> fuelItems = Maps.newHashMap();
	public static Map<String, List<JsonBlockFluidThirst>> blockFluidThirst = Maps.newHashMap();
	public static Map<String, List<JsonConsumableThirst>> consumableThirst = Maps.newHashMap();
	public static Map<String, JsonConsumableHeal> consumableHeal = Maps.newHashMap();
	public static Map<String, JsonBodyPartsDamageSource> damageSourceBodyParts = Maps.newHashMap();

	public static void registerDimensionTemperature(String registryName, float temperature)
	{
		if(!dimensionTemperatures.containsKey(registryName))
			dimensionTemperatures.put(registryName, new JsonTemperature(temperature));
	}

	public static void registerBlockFluidTemperature(String registryName, float temperature, JsonPropertyValue... properties)
	{
		if (!blockFluidTemperatures.containsKey(registryName))
			blockFluidTemperatures.put(registryName, new ArrayList<>());
		
		final List<JsonBlockFluidTemperature> alreadyDefinedConfig = blockFluidTemperatures.get(registryName);
		
		JsonBlockFluidTemperature newBlockFluidTemp = new JsonBlockFluidTemperature(temperature, properties);
		
		if (properties.length > 0)
		{
			for (int i = 0; i < alreadyDefinedConfig.size(); i++)
			{
				JsonBlockFluidTemperature jpt = alreadyDefinedConfig.get(i);
				if (jpt.matchesDescribedProperties(properties))
				{
					alreadyDefinedConfig.set(i, newBlockFluidTemp);
					return;
				}
			}

		}
		else 
		{
			// Reject config if you want to add empty properties in a block config that has properties
			for (JsonBlockFluidTemperature jpt : alreadyDefinedConfig) {
				if (!jpt.properties.keySet().isEmpty()) {
					return;
				}
			}
			// Replace empty config if it already exists
			for (int i = 0; i < alreadyDefinedConfig.size(); i++)
			{
				JsonBlockFluidTemperature jpt = alreadyDefinedConfig.get(i);
				if (jpt.properties.keySet().isEmpty())
				{
					alreadyDefinedConfig.set(i, newBlockFluidTemp);
					return;
				}
			}

		}
		alreadyDefinedConfig.add(newBlockFluidTemp);
	}

	public static void registerItemTemperature(String registryName, float temperature)
	{
		if(!itemTemperatures.containsKey(registryName))
			itemTemperatures.put(registryName, new JsonTemperature(temperature));
	}

	public static void registerEntityTemperature(String registryName, float temperature) {
		if (!entityTemperatures.containsKey(registryName))
			entityTemperatures.put(registryName, new JsonTemperature(temperature));
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

	public static void registerBlockFluidThirst(String registryName, int hydration, float saturation) {
		registerBlockFluidThirst(registryName, hydration, saturation, new JsonPropertyValue[]{});
	}

	public static void registerBlockFluidThirst(String registryName, int hydration, float saturation, JsonPropertyValue... properties) {
		registerBlockFluidThirst(registryName, hydration, saturation, new JsonEffectParameter[]{}, properties);
	}

	public static void registerBlockFluidThirst(String registryName, int hydration, float saturation, JsonEffectParameter[] effects, JsonPropertyValue... properties) {
		if (!blockFluidThirst.containsKey(registryName))
			blockFluidThirst.put(registryName, new ArrayList<>());

		final List<JsonBlockFluidThirst> alreadyDefinedConfig = blockFluidThirst.get(registryName);

		JsonBlockFluidThirst newBlockFluidThirst = new JsonBlockFluidThirst(hydration, saturation, effects, properties);

		if (properties.length > 0) {
			//  If the same set of nbt already exists, replace it
			for (int i = 0; i < alreadyDefinedConfig.size(); i++) {
				JsonBlockFluidThirst jpt = alreadyDefinedConfig.get(i);
				if (jpt.matchesProperties(properties)) {
					alreadyDefinedConfig.set(i, newBlockFluidThirst);
					return;
				}
			}
		}
		else  {
			// Replace empty config if it already exists
			for (int i = 0; i < alreadyDefinedConfig.size(); i++) {
				JsonBlockFluidThirst jpt = alreadyDefinedConfig.get(i);
				if (jpt.properties.keySet().isEmpty()) {
					alreadyDefinedConfig.set(i, newBlockFluidThirst);
					return;
				}
			}
		}
		alreadyDefinedConfig.add(newBlockFluidThirst);
	}

	public static void registerConsumableThirst(String registryName, int hydration, float saturation) {
		registerConsumableThirst(registryName, hydration, saturation, new JsonPropertyValue[]{});
	}

	public static void registerConsumableThirst(String registryName, int hydration, float saturation, JsonPropertyValue... nbt) {
		registerConsumableThirst(registryName, hydration, saturation, new JsonEffectParameter[]{}, nbt);
	}

	public static void registerConsumableThirst(String registryName, int hydration, float saturation, JsonEffectParameter[] effects, JsonPropertyValue... nbt) {
		if (!consumableThirst.containsKey(registryName)) {
			consumableThirst.put(registryName, new ArrayList<>());
		}

		final List<JsonConsumableThirst> alreadyDefinedConfig = consumableThirst.get(registryName);

		JsonConsumableThirst newThirstConfig = new JsonConsumableThirst(hydration, saturation, effects, nbt);

		if (nbt.length > 0) {
			//  If the same set of nbt already exists, replace it
			for (int i = 0; i < alreadyDefinedConfig.size(); i++) {
				JsonConsumableThirst jct = alreadyDefinedConfig.get(i);
				if (jct.matchesNbt(nbt)) {
					alreadyDefinedConfig.set(i, newThirstConfig);
					return;
				}
			}
		}
		else {
			//  If an empty nbt already exists, replace it
			for (int i = 0; i < alreadyDefinedConfig.size(); i++) {
				JsonConsumableThirst jct = alreadyDefinedConfig.get(i);
				if (jct.nbt.keySet().isEmpty()) {
					alreadyDefinedConfig.set(i, newThirstConfig);
					return;
				}
			}
		}
		alreadyDefinedConfig.add(newThirstConfig);
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
			fuelItems.put(registryName, new JsonFuelItem(thermalType, fuelValue));
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
