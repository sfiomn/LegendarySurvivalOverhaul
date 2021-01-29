package icey.survivaloverhaul.config;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import icey.survivaloverhaul.api.config.json.temperature.JsonArmorIdentity;
import icey.survivaloverhaul.api.config.json.temperature.JsonBiomeIdentity;
import icey.survivaloverhaul.api.config.json.temperature.JsonPropertyTemperature;
import icey.survivaloverhaul.api.config.json.temperature.JsonTemperature;
import icey.survivaloverhaul.api.config.json.temperature.JsonTemperatureIdentity;

public class JsonTypeToken
{
	public static Type get(JsonFileName jcfn)
	{
		switch(jcfn)
		{
			case ARMOR:				return new TypeToken<Map<String, List<JsonArmorIdentity>>>(){}.getType();
			case BLOCK:				return new TypeToken<Map<String, List<JsonPropertyTemperature>>>(){}.getType();
			case LIQUID: 			return new TypeToken<Map<String, JsonTemperature>>(){}.getType();
			case BIOME: 			return new TypeToken<Map<String, JsonBiomeIdentity>>(){}.getType();
			default: 		return null;
		}
	}
}
