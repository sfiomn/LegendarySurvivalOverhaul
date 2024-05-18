package sfiomn.legendarysurvivaloverhaul.config;

import com.google.gson.reflect.TypeToken;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.*;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonThirst;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonTypeToken
{
	public static Type get(JsonFileName jcfn)
	{
		switch(jcfn)
		{
			case ITEM:				return new TypeToken<Map<String, JsonTemperature>>(){}.getType();
			case BLOCK:				return new TypeToken<Map<String, List<JsonPropertyTemperature>>>(){}.getType();
			case BIOME: 			return new TypeToken<Map<String, JsonBiomeIdentity>>(){}.getType();
			case CONSUMABLE_TEMP: 		return new TypeToken<Map<String, List<JsonConsumableTemperature>>>(){}.getType();
			case CONSUMABLE_THIRST: 		return new TypeToken<Map<String, JsonThirst>>(){}.getType();
			case FUEL:				return new TypeToken<Map<String, JsonFuelItemIdentity>>(){}.getType();
			default: 		return null;
		}
	}
}
