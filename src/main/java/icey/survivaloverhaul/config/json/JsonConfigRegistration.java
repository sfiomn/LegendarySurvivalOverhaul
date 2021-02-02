package icey.survivaloverhaul.config.json;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.config.json.JsonItemIdentity;
import icey.survivaloverhaul.api.config.json.TemporaryModifierGroup;
import icey.survivaloverhaul.api.config.json.temperature.JsonArmorIdentity;
import icey.survivaloverhaul.api.config.json.temperature.JsonBiomeIdentity;
import icey.survivaloverhaul.api.config.json.temperature.JsonConsumableTemperature;
import icey.survivaloverhaul.api.config.json.temperature.JsonPropertyTemperature;
import icey.survivaloverhaul.api.config.json.temperature.JsonPropertyValue;
import icey.survivaloverhaul.api.config.json.temperature.JsonTemperature;
import icey.survivaloverhaul.common.compat.CompatController;
import icey.survivaloverhaul.config.JsonFileName;
import icey.survivaloverhaul.config.JsonTypeToken;

public class JsonConfigRegistration
{
	public static final JsonItemIdentity DEFAULT_ITEM_IDENTITY = new JsonItemIdentity(null);
	
	public static void init(File configDir)
	{
		registerTemperatures(configDir);
		
		processAllJson(configDir);
	}
	
	public static void registerTemperatures(File configDir)
	{
		JsonConfig.registerBlockTemperature("minecraft:campfire", 7.5f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockTemperature("minecraft:soul_campfire", 5.0f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockTemperature("minecraft:campfire", 0.0f, new JsonPropertyValue("lit", "false"));
		JsonConfig.registerBlockTemperature("minecraft:soul_campfire", 0.0f, new JsonPropertyValue("lit", "false"));
		
		JsonConfig.registerBlockTemperature("minecraft:torch", 1.5f);
		JsonConfig.registerBlockTemperature("minecraft:soul_torch", 0.75f);
		
		JsonConfig.registerBlockTemperature("minecraft:wall_torch", 1.5f);
		JsonConfig.registerBlockTemperature("minecraft:soul_wall_torch", 0.75f);
		
		JsonConfig.registerBlockTemperature("minecraft:fire", 5.0f);
		JsonConfig.registerBlockTemperature("minecraft:soul_fire", 2.5f);
		
		JsonConfig.registerBlockTemperature("minecraft:furnace", 5.0f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockTemperature("minecraft:blast_furnace", 5.0f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockTemperature("minecraft:smoker", 5.0f, new JsonPropertyValue("lit", "true"));
		
		JsonConfig.registerBlockTemperature("minecraft:furnace", 0.0f, new JsonPropertyValue("lit", "false"));
		JsonConfig.registerBlockTemperature("minecraft:blast_furnace", 0.0f, new JsonPropertyValue("lit", "false"));
		JsonConfig.registerBlockTemperature("minecraft:smoker", 0.0f, new JsonPropertyValue("false", "true"));
		
		JsonConfig.registerBlockTemperature("survivaloverhaul:cooling_coil", -10.0f, new JsonPropertyValue("powered", "true"));
		JsonConfig.registerBlockTemperature("survivaloverhaul:heating_coil", 10.0f, new JsonPropertyValue("powered", "true"));
		
		JsonConfig.registerBlockTemperature("survivaloverhaul:cooling_coil", 0.0f, new JsonPropertyValue("powered", "false"));
		JsonConfig.registerBlockTemperature("survivaloverhaul:heating_coil", 0.0f, new JsonPropertyValue("powered", "false"));
		
		JsonConfig.registerBlockTemperature("minecraft:magma_block", 7.5f);
		
		JsonConfig.registerBlockTemperature("minecraft:jack_o_lantern", 3.0f);
		
		JsonConfig.registerFluidTemperature("minecraft:lava", 10.0f);
		JsonConfig.registerFluidTemperature("minecraft:flowing_lava", 10.0f);
		
		JsonConfig.registerArmorTemperature("survivaloverhaul:snow_feet", 0.5f);
		JsonConfig.registerArmorTemperature("survivaloverhaul:snow_legs", 2.5f);
		JsonConfig.registerArmorTemperature("survivaloverhaul:snow_chest", 3.0f);
		JsonConfig.registerArmorTemperature("survivaloverhaul:snow_head", 1.5f);
		
		JsonConfig.registerArmorTemperature("survivaloverhaul:desert_feet", -0.5f);
		JsonConfig.registerArmorTemperature("survivaloverhaul:desert_legs", -2.5f);
		JsonConfig.registerArmorTemperature("survivaloverhaul:desert_chest", -3.0f);
		JsonConfig.registerArmorTemperature("survivaloverhaul:desert_head", -1.5f);
		
		JsonConfig.registerArmorTemperature("minecraft:leather_boots", 0.25f, 0.9f);
		JsonConfig.registerArmorTemperature("minecraft:leather_leggings", 0.75f, 0.9f);
		JsonConfig.registerArmorTemperature("minecraft:leather_chestplate", 1.0f, 0.9f);
		JsonConfig.registerArmorTemperature("minecraft:leather_helmet", 0.5f, 0.9f);
		
		JsonConfig.registerArmorTemperature("minecraft:iron_boots", 0f, 1.2f);
		JsonConfig.registerArmorTemperature("minecraft:iron_leggings", 0f, 1.2f);
		JsonConfig.registerArmorTemperature("minecraft:iron_chestplate", 0f, 1.2f);
		JsonConfig.registerArmorTemperature("minecraft:iron_helmet", 0f, 1.2f);
		
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroup.FOOD.group(), "minecraft:mushroom_stew", 1.0f, 1200, DEFAULT_ITEM_IDENTITY);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroup.FOOD.group(), "minecraft:rabbit_stew", 1.0f, 1200, DEFAULT_ITEM_IDENTITY);
		
		JsonConfig.registerBiomeOverride("minecraft:crimson_forest", 0.75f, false);
		JsonConfig.registerBiomeOverride("minecraft:warped_forest", 0.75f, false);
		JsonConfig.registerBiomeOverride("minecraft:nether_wastes", 1.0f, false);
		JsonConfig.registerBiomeOverride("minecraft:soul_sand_valley", 1.0f, false);
		JsonConfig.registerBiomeOverride("minecraft:basalt_deltas", 1.15f, false);
		
		CompatController.initCompat(configDir);
	}
	
	public static void clearContainers()
	{
		JsonConfig.armorTemperatures.clear();
		JsonConfig.blockTemperatures.clear();
		JsonConfig.fluidTemperatures.clear();
		JsonConfig.biomeOverrides.clear();
		JsonConfig.consumableTemperature.clear();
	}
	
	public static void processAllJson(File jsonDir)
	{
		Map<String, List<JsonArmorIdentity>> jsonArmorTemperatures = processJson(JsonFileName.ARMOR, JsonConfig.armorTemperatures, jsonDir, true);
		Main.LOGGER.debug("Loaded " + jsonArmorTemperatures.size() + " armor temperature values");
		
		if (jsonArmorTemperatures != null)
		{
			for (Map.Entry<String, List<JsonArmorIdentity>> entry : jsonArmorTemperatures.entrySet())
			{
				for (JsonArmorIdentity jtm : entry.getValue())
				{
					if (jtm.identity != null)
							jtm.identity.tryPopulateCompound();
					
					JsonConfig.registerArmorTemperature(entry.getKey(), jtm.temperature, jtm.insulation, jtm.identity == null ? DEFAULT_ITEM_IDENTITY : jtm.identity);
				}
			}
		}
		
		Map<String, List<JsonPropertyTemperature>> jsonBlockTemperatures = processJson(JsonFileName.BLOCK, JsonConfig.blockTemperatures, jsonDir, true);
		Main.LOGGER.debug("Loaded " + jsonBlockTemperatures.size() + " block temperature values");
		
		if (jsonBlockTemperatures != null)
		{
			for (Map.Entry<String, List<JsonPropertyTemperature>> entry : jsonBlockTemperatures.entrySet())
			{
				for (JsonPropertyTemperature propTemp : entry.getValue())
				{
					JsonConfig.registerBlockTemperature(entry.getKey(), propTemp.temperature, propTemp.getAsPropertyArray());
				}
			}
			
			try
			{
				manuallyWriteToJson(JsonFileName.BLOCK, JsonConfig.blockTemperatures, jsonDir);
			}
			catch (Exception e)
			{
				Main.LOGGER.error("Error writing merged JSON file", e);
			}
		}
		
		Map<String, JsonTemperature> jsonFluidTemperatures = processJson(JsonFileName.LIQUID, JsonConfig.fluidTemperatures, jsonDir, true);
		Main.LOGGER.debug("Loaded " + jsonFluidTemperatures.size() + " fluid temperature values");
		
		if (jsonFluidTemperatures != null)
		{
			for (Map.Entry<String, JsonTemperature> entry : jsonFluidTemperatures.entrySet())
			{
				JsonConfig.registerFluidTemperature(entry.getKey(), entry.getValue().temperature);
			}
			
			try
			{
				manuallyWriteToJson(JsonFileName.LIQUID, JsonConfig.fluidTemperatures, jsonDir);
			}
			catch (Exception e)
			{
				Main.LOGGER.error("Error writing merged JSON file", e);
			}
		}
		
		Map<String, JsonBiomeIdentity> jsonBiomeIdentities = processJson(JsonFileName.BIOME, JsonConfig.biomeOverrides, jsonDir, true);
		Main.LOGGER.debug("Loaded " + jsonBiomeIdentities.size() + " biome temperature overrides");
		
		if (jsonBiomeIdentities != null)
		{
			for (Map.Entry<String, JsonBiomeIdentity> entry : jsonBiomeIdentities.entrySet())
			{
				JsonConfig.registerBiomeOverride(entry.getKey(), entry.getValue().temperature, entry.getValue().isDry);
			}
			
			try
			{
				manuallyWriteToJson(JsonFileName.BIOME, JsonConfig.biomeOverrides, jsonDir);
			}
			catch (Exception e)
			{
				Main.LOGGER.error("Error writing merged JSON file", e);
			}
		}
		
		Map<String, List<JsonConsumableTemperature>> jsonConsumableTemperatures = processJson(JsonFileName.CONSUMABLE, JsonConfig.consumableTemperature, jsonDir, true);
		Main.LOGGER.debug("Loaded " + jsonConsumableTemperatures.size() + " consumable temperature values");
		
		if (jsonConsumableTemperatures != null)
		{
			for (Map.Entry<String, List<JsonConsumableTemperature>> entry : jsonConsumableTemperatures.entrySet())
			{
				for (JsonConsumableTemperature jct : entry.getValue())
				{
					if (jct.identity != null)
						jct.identity.tryPopulateCompound();
					
					JsonConfig.registerConsumableTemperature(jct.group, entry.getKey(), jct.temperature, jct.duration, jct.identity);
				}
			}
			
			try
			{
				manuallyWriteToJson(JsonFileName.CONSUMABLE, JsonConfig.consumableTemperature, jsonDir);
			}
			catch (Exception e)
			{
				Main.LOGGER.error("Error writing merged JSON file", e);
			}
		}
	}
	
	@Nullable
	public static <T> T processJson(JsonFileName jfn, final T container, File jsonDir, boolean forMerging)
	{
		try
		{
			return processUncaughtJson(jfn, container, jsonDir, forMerging);
		}
		catch (Exception e)
		{
			Main.LOGGER.error("Error managing JSON file: " + jfn.get(), e);
			
			if (forMerging)
			{
				return null;
			}
			else
			{
				return container;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	public static <T> T processUncaughtJson(JsonFileName jfn, final T container, File jsonDir, boolean forMerging) throws Exception
	{
		String jsonFileName = jfn.get();
		Type type = JsonTypeToken.get(jfn);
		
		File jsonFile = new File(jsonDir, jsonFileName);
		
		if (jsonFile.exists())
		{
			Gson gson = buildNewGson();
			
			return (T) gson.fromJson(new FileReader(jsonFile), type);
		}
		else
		{
			Gson gson = buildNewGson();
			
			FileUtils.write(jsonFile, gson.toJson(container, type), (String) null);
			
			if (forMerging)
			{
				return null;
			}
			else
			{
				return container;
			}
		}
	}
	
	private static <T> void manuallyWriteToJson(JsonFileName jfn, final T container, File jsonDir) throws Exception
	{
		manuallyWriteToJson(jfn, container, jsonDir, false);
	}
	
	private static <T> void manuallyWriteToJson(JsonFileName jfn, final T container, File jsonDir, boolean forceWrite) throws Exception
	{
		String jsonFileName = jfn.get();
		Type type = JsonTypeToken.get(jfn);
		
		Gson gson = buildNewGson();
		File jsonFile = new File(jsonDir, jsonFileName);
		if (jsonFile.exists())
		{
			if (forceWrite)
				Main.LOGGER.debug("Overriding...");
			else
				return;
		}
		FileUtils.write(jsonFile, gson.toJson(container, type), (String) null);
	}
	
	private static Gson buildNewGson()
	{
		return new GsonBuilder().setPrettyPrinting().excludeFieldsWithModifiers(Modifier.PRIVATE).create();
	}
}