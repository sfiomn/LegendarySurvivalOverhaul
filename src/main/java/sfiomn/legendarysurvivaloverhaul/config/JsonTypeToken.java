package sfiomn.legendarysurvivaloverhaul.config;

import com.google.gson.reflect.TypeToken;
import sfiomn.legendarysurvivaloverhaul.api.config.json.bodydamage.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.api.config.json.bodydamage.JsonConsumableHeal;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.*;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonBlockFluidThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonConsumableThirst;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonTypeToken
{
	public static Type get(JsonFileName jcfn)
	{
		switch(jcfn)
		{
			case DIMENSION_TEMP:			return new TypeToken<Map<String, JsonTemperature>>(){}.getType();
			case BIOME_TEMP: 			return new TypeToken<Map<String, JsonBiomeIdentity>>(){}.getType();
			case ITEM_TEMP:				return new TypeToken<Map<String, JsonTemperatureResistance>>(){}.getType();
			case ENTITY_TEMP: 			return new TypeToken<Map<String, JsonTemperature>>(){}.getType();
			case BLOCK_TEMP:				return new TypeToken<Map<String, List<JsonBlockFluidTemperature>>>(){}.getType();
			case ORIGINS_TEMP:			return new TypeToken<Map<String, JsonTemperatureResistance>>(){}.getType();
			case FUEL:				return new TypeToken<Map<String, JsonFuelItem>>(){}.getType();
			case CONSUMABLE_TEMP: 	return new TypeToken<Map<String, List<JsonConsumableTemperature>>>(){}.getType();
			case BLOCK_THIRST:		return new TypeToken<Map<String, List<JsonBlockFluidThirst>>>(){}.getType();
			case CONSUMABLE_THIRST: return new TypeToken<Map<String, List<JsonConsumableThirst>>>(){}.getType();
			case CONSUMABLE_HEAL: 	return new TypeToken<Map<String, JsonConsumableHeal>>(){}.getType();
			case DAMAGE_SOURCE_BODY_PARTS:		return new TypeToken<Map<String, JsonBodyPartsDamageSource>>(){}.getType();
			default: 				return null;
		}
	}
}
