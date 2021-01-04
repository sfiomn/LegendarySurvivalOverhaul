package icey.survivaloverhaul.config.json;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import icey.survivaloverhaul.api.config.json.JsonPropertyTemperature;
import net.minecraft.util.ResourceLocation;

public class TemperatureConfig
{
	public static final Map<ResourceLocation, List<JsonPropertyTemperature>> blockTemperature = Maps.newHashMap();
	
	public static void init(File configDir)
	{
		
	}
}