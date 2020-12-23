package icey.survivaloverhaul.api.temperature;

import java.util.LinkedHashMap;

public class TemperatureRegistry
{
	public static LinkedHashMap<String, ITemperatureModifier> modifiers = new LinkedHashMap<String, ITemperatureModifier>();
	
	public static LinkedHashMap<String, ITemperatureDynamicModifier> dynamicModifiers = new LinkedHashMap<String, ITemperatureDynamicModifier>();
	
	public static void registerModifier(ITemperatureModifier modifier)
	{
		modifiers.put(modifier.getName(), modifier);
	}
	
	public static void registerDynamicModifier(ITemperatureDynamicModifier modifier)
	{
		dynamicModifiers.put(modifier.getName(), modifier);
	}
}
