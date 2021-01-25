package icey.survivaloverhaul.common.compat;

import java.io.File;

import icey.survivaloverhaul.api.config.json.temperature.JsonPropertyValue;
import icey.survivaloverhaul.config.json.JsonConfig;
import net.minecraftforge.fml.ModList;

public final class CompatController
{
	
	public static void initCompat(File configDir)
	{
		ModList mods = ModList.get();
		
		if (mods.isLoaded("create"))
			initCreate();
		if (mods.isLoaded("farmersdelight"))
			initFarmersDelight();
		if (mods.isLoaded("realistictorches"))
			initRealisticTorches();
		if (mods.isLoaded("byg"))
			initBYG();
		
		/*
		 * This bloats the size of the config file considerably
		 * from ~2 KB all the way up to 49 KB
		 * Plus it probably wouldn't be fun dealing with a ton of candles in a small area
		 * 
		if (mods.isLoaded("quark"))
				initQuarkCandles();
		if (mods.isLoaded("buzzier_bees"))
				initBuzzierBeesCandles();
		*/
	}
	
	private static void initCreate()
	{
		JsonConfig.registerBlockTemperature("create:blaze_burner", 2.5f, new JsonPropertyValue("blaze", "smouldering"));
		JsonConfig.registerBlockTemperature("create:blaze_burner", 5.0f, new JsonPropertyValue("blaze", "kindled"));
		JsonConfig.registerBlockTemperature("create:blaze_burner", 7.5f, new JsonPropertyValue("blaze", "seething"));
	}
	
	private static void initFarmersDelight()
	{
		JsonConfig.registerBlockTemperature("farmersdelight:stove", 7.5f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockTemperature("farmersdelight:stove", 0.0f, new JsonPropertyValue("lit", "false"));
	}
	
	private static void initRealisticTorches()
	{  
		JsonConfig.registerBlockTemperature("realistictorches:torch", 1.5f, new JsonPropertyValue("litstate", "2"));
		JsonConfig.registerBlockTemperature("realistictorches:torch", 0.75f, new JsonPropertyValue("litstate", "1"));
		JsonConfig.registerBlockTemperature("realistictorches:torch", 0.0f, new JsonPropertyValue("litstate", "0"));
		
		JsonConfig.registerBlockTemperature("realistictorches:torch_wall", 1.5f, new JsonPropertyValue("litstate", "2"));
		JsonConfig.registerBlockTemperature("realistictorches:torch_wall", 0.75f, new JsonPropertyValue("litstate", "1"));
		JsonConfig.registerBlockTemperature("realistictorches:torch_wall", 0.0f, new JsonPropertyValue("litstate", "0"));
	}
	
	private static void initBYG()
	{
		JsonConfig.registerBlockTemperature("byg:boric_fire", 5.0f);
		JsonConfig.registerBlockTemperature("byg:boric_campfire", 7.5f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockTemperature("byg:boric_campfire", 0.0f, new JsonPropertyValue("lit", "false"));

		JsonConfig.registerBlockTemperature("byg:cryptic_fire", 5.0f);
		JsonConfig.registerBlockTemperature("byg:cryptic_campfire", 7.5f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockTemperature("byg:cryptic_campfire", 0.0f, new JsonPropertyValue("lit", "false"));
	}
}
