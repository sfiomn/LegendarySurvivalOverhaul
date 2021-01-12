package icey.survivaloverhaul.config.json;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import icey.survivaloverhaul.api.config.json.JsonPropertyTemperature;
import icey.survivaloverhaul.api.config.json.JsonTemperature;
import icey.survivaloverhaul.api.config.json.JsonTemperatureIdentity;
import net.minecraft.util.ResourceLocation;

public class TemperatureConfig
{
	public static Map<String, List<JsonPropertyTemperature>> blockTemperature = Maps.newHashMap();
	public static Map<String, List<JsonTemperatureIdentity>> armorTemperature = Maps.newHashMap();
	public static Map<String, List<JsonTemperature>> fluidTemperatures = Maps.newHashMap();
	
	public static void init(File configDir)
	{
		
	}
}