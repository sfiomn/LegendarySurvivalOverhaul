package sfiomn.legendarysurvivaloverhaul.config.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.*;
import sfiomn.legendarysurvivaloverhaul.common.integration.IntegrationController;
import sfiomn.legendarysurvivaloverhaul.config.JsonFileName;
import sfiomn.legendarysurvivaloverhaul.config.JsonTypeToken;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonConfigRegistration
{
	public static void init(File configDir)
	{
		registerTemperatures(configDir);

		processAllJson(configDir);

		writeAllToJson(configDir);
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

		JsonConfig.registerBlockTemperature(LegendarySurvivalOverhaul.MOD_ID + ":cooler", -10.0f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockTemperature(LegendarySurvivalOverhaul.MOD_ID + ":heater", 15.0f, new JsonPropertyValue("lit", "true"));

		JsonConfig.registerBlockTemperature(LegendarySurvivalOverhaul.MOD_ID + ":cooler", 0.0f, new JsonPropertyValue("lit", "false"));
		JsonConfig.registerBlockTemperature(LegendarySurvivalOverhaul.MOD_ID + ":heater", 0.0f, new JsonPropertyValue("lit", "false"));

		JsonConfig.registerBlockTemperature("minecraft:magma_block", 7.5f);

		JsonConfig.registerBlockTemperature("minecraft:jack_o_lantern", 3.0f);

		JsonConfig.registerFluidTemperature("minecraft:lava", 10.0f);
		JsonConfig.registerFluidTemperature("minecraft:flowing_lava", 10.0f);

		JsonConfig.registerArmorTemperature(LegendarySurvivalOverhaul.MOD_ID + ":snow_boots", 0.5f);
		JsonConfig.registerArmorTemperature(LegendarySurvivalOverhaul.MOD_ID + ":snow_leggings", 2.5f);
		JsonConfig.registerArmorTemperature(LegendarySurvivalOverhaul.MOD_ID + ":snow_chestplate", 3.0f);
		JsonConfig.registerArmorTemperature(LegendarySurvivalOverhaul.MOD_ID + ":snow_helmet", 1.5f);

		JsonConfig.registerArmorTemperature(LegendarySurvivalOverhaul.MOD_ID + ":desert_boots", -0.5f);
		JsonConfig.registerArmorTemperature(LegendarySurvivalOverhaul.MOD_ID + ":desert_leggings", -2.5f);
		JsonConfig.registerArmorTemperature(LegendarySurvivalOverhaul.MOD_ID + ":desert_chestplate", -3.0f);
		JsonConfig.registerArmorTemperature(LegendarySurvivalOverhaul.MOD_ID + ":desert_helmet", -1.5f);

		JsonConfig.registerArmorTemperature("minecraft:leather_boots", 0.25f);
		JsonConfig.registerArmorTemperature("minecraft:leather_leggings", 0.75f);
		JsonConfig.registerArmorTemperature("minecraft:leather_chestplate", 1.0f);
		JsonConfig.registerArmorTemperature("minecraft:leather_helmet", 0.5f);

		JsonConfig.registerArmorTemperature("minecraft:iron_boots", 0f);
		JsonConfig.registerArmorTemperature("minecraft:iron_leggings", 0f);
		JsonConfig.registerArmorTemperature("minecraft:iron_chestplate", 0f);
		JsonConfig.registerArmorTemperature("minecraft:iron_helmet", 0f);

		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "minecraft:mushroom_stew", 1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "minecraft:mushroom_stew", 1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "minecraft:rabbit_stew", 2, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "minecraft:melon_slice", -1, 1200);

		JsonConfig.registerBiomeOverride("minecraft:crimson_forest", 0.75f, false);
		JsonConfig.registerBiomeOverride("minecraft:warped_forest", 0.75f, false);
		JsonConfig.registerBiomeOverride("minecraft:nether_wastes", 1.0f, false);
		JsonConfig.registerBiomeOverride("minecraft:soul_sand_valley", 1.0f, false);
		JsonConfig.registerBiomeOverride("minecraft:basalt_deltas", 1.45f, false);

		JsonConfig.registerFuelItems("minecraft:coal", ThermalTypeEnum.HEATING, 30);
		JsonConfig.registerFuelItems("minecraft:charcoal", ThermalTypeEnum.HEATING, 30);
		JsonConfig.registerFuelItems("minecraft:coal_block", ThermalTypeEnum.HEATING, 270);
		JsonConfig.registerFuelItems("quark:coal_block", ThermalTypeEnum.HEATING, 270);
		JsonConfig.registerFuelItems("quark:charcoal_block", ThermalTypeEnum.HEATING, 270);
		JsonConfig.registerFuelItems("betterendforge:coal_block", ThermalTypeEnum.HEATING, 270);
		JsonConfig.registerFuelItems("betterendforge:charcoal_block", ThermalTypeEnum.HEATING, 270);
		JsonConfig.registerFuelItems("minecraft:ice", ThermalTypeEnum.COOLING, 30);
		JsonConfig.registerFuelItems("minecraft:snowball", ThermalTypeEnum.COOLING, 30);
		JsonConfig.registerFuelItems("minecraft:snow_block", ThermalTypeEnum.COOLING, 30);
		JsonConfig.registerFuelItems("minecraft:blue_ice", ThermalTypeEnum.COOLING, 30);
		JsonConfig.registerFuelItems("minecraft:packed_ice", ThermalTypeEnum.COOLING, 30);

		IntegrationController.initCompat();
	}
	
	public static void clearContainers()
	{
		JsonConfig.armorTemperatures.clear();
		JsonConfig.blockTemperatures.clear();
		JsonConfig.fluidTemperatures.clear();
		JsonConfig.biomeOverrides.clear();
		JsonConfig.consumableTemperature.clear();
		JsonConfig.fuelItems.clear();
	}

	public static void writeAllToJson(File jsonDir) {
		try {
			manuallyWriteToJson(JsonFileName.ARMOR, JsonConfig.armorTemperatures, jsonDir);
		} catch (Exception e) {
			LegendarySurvivalOverhaul.LOGGER.error("Error writing ARMOR JSON file", e);
		}
		try {
			manuallyWriteToJson(JsonFileName.BLOCK, JsonConfig.blockTemperatures, jsonDir);
		} catch (Exception e) {
			LegendarySurvivalOverhaul.LOGGER.error("Error writing BLOCK JSON file", e);
		}
		try {
			manuallyWriteToJson(JsonFileName.LIQUID, JsonConfig.fluidTemperatures, jsonDir);
		} catch (Exception e) {
			LegendarySurvivalOverhaul.LOGGER.error("Error writing LIQUID JSON file", e);
		}
		try {
			manuallyWriteToJson(JsonFileName.BIOME, JsonConfig.biomeOverrides, jsonDir);
		} catch (Exception e) {
			LegendarySurvivalOverhaul.LOGGER.error("Error writing BIOME JSON file", e);
		}
		try {
			manuallyWriteToJson(JsonFileName.CONSUMABLE, JsonConfig.consumableTemperature, jsonDir);
		} catch (Exception e) {
			LegendarySurvivalOverhaul.LOGGER.error("Error writing CONSUMABLE JSON file", e);
		}
		try {
			manuallyWriteToJson(JsonFileName.FUEL, JsonConfig.fuelItems, jsonDir);
		} catch (Exception e) {
			LegendarySurvivalOverhaul.LOGGER.error("Error writing FUEL JSON file", e);
		}
	}
	
	public static void processAllJson(File jsonDir)
	{
		Map<String, JsonTemperature> jsonArmorTemperatures = processJson(JsonFileName.ARMOR, jsonDir);

		if (jsonArmorTemperatures != null)
		{
			// remove default armor config
			JsonConfig.armorTemperatures.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonArmorTemperatures.size() + " armor temperature values from JSON");
			for (Map.Entry<String, JsonTemperature> entry : jsonArmorTemperatures.entrySet())
			{
				JsonConfig.registerArmorTemperature(entry.getKey(), entry.getValue().temperature);
			}
		}
		
		Map<String, List<JsonPropertyTemperature>> jsonBlockTemperatures = processJson(JsonFileName.BLOCK, jsonDir);
		
		if (jsonBlockTemperatures != null)
		{
			// remove default block config
			JsonConfig.blockTemperatures.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonBlockTemperatures.size() + " block temperature values from JSON");
			for (Map.Entry<String, List<JsonPropertyTemperature>> entry : jsonBlockTemperatures.entrySet())
			{
				for (JsonPropertyTemperature propTemp : entry.getValue())
				{
					JsonConfig.registerBlockTemperature(entry.getKey(), propTemp.temperature, propTemp.getAsPropertyArray());
				}
			}
		}
		
		Map<String, JsonTemperature> jsonFluidTemperatures = processJson(JsonFileName.LIQUID, jsonDir);
		
		if (jsonFluidTemperatures != null)
		{
			// remove default fluid config
			JsonConfig.fluidTemperatures.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonFluidTemperatures.size() + " fluid temperature values from JSON");
			for (Map.Entry<String, JsonTemperature> entry : jsonFluidTemperatures.entrySet())
			{
				JsonConfig.registerFluidTemperature(entry.getKey(), entry.getValue().temperature);
			}
		}
		
		Map<String, JsonBiomeIdentity> jsonBiomeIdentities = processJson(JsonFileName.BIOME, jsonDir);
		
		if (jsonBiomeIdentities != null)
		{
			// remove default biome config
			JsonConfig.biomeOverrides.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonBiomeIdentities.size() + " biome temperature overrides from JSON");
			for (Map.Entry<String, JsonBiomeIdentity> entry : jsonBiomeIdentities.entrySet())
			{
				JsonConfig.registerBiomeOverride(entry.getKey(), entry.getValue().temperature, entry.getValue().isDry);
			}
		}
		
		Map<String, List<JsonConsumableTemperature>> jsonConsumableTemperatures = processJson(JsonFileName.CONSUMABLE, jsonDir);

		if (jsonConsumableTemperatures != null)
		{
			// remove default consumables config
			JsonConfig.consumableTemperature.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonConsumableTemperatures.size() + " consumable temperature values from JSON");
			for (Map.Entry<String, List<JsonConsumableTemperature>> entry : jsonConsumableTemperatures.entrySet())
			{
				for (JsonConsumableTemperature jct: entry.getValue()) {
					JsonConfig.registerConsumableTemperature(jct.group, entry.getKey(), jct.temperatureLevel, jct.duration);
				}
			}
		}

		Map<String, JsonFuelItemIdentity> jsonFuelItemIdentities = processJson(JsonFileName.FUEL, jsonDir);

		if (jsonFuelItemIdentities != null)
		{
			// remove default fuel config
			JsonConfig.fuelItems.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonFuelItemIdentities.size() + " fuel item values from JSON");
			for (Map.Entry<String, JsonFuelItemIdentity> entry : jsonFuelItemIdentities.entrySet())
			{
				JsonConfig.registerFuelItems(entry.getKey(), entry.getValue().thermalType, entry.getValue().fuelValue);
			}
		}
	}
	
	@Nullable
	public static <T> T processJson(JsonFileName jfn, File jsonDir)
	{
		try
		{
			return processUncaughtJson(jfn, jsonDir);
		}
		catch (Exception e)
		{
			LegendarySurvivalOverhaul.LOGGER.error("Error managing JSON file: " + jfn.get(), e);
			
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	public static <T> T processUncaughtJson(JsonFileName jfn, File jsonDir) throws Exception
	{
		String jsonFileName = jfn.get();
		Type type = JsonTypeToken.get(jfn);
		
		File jsonFile = new File(jsonDir, jsonFileName);
		
		if (jsonFile.exists())
		{
			Gson gson = buildNewGson();
			
			return (T) gson.fromJson(new FileReader(jsonFile), type);
		}
		return null;
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
			LegendarySurvivalOverhaul.LOGGER.debug(jsonFile.getName() + " already exists!");
			
			if (forceWrite)
				LegendarySurvivalOverhaul.LOGGER.debug("Overriding...");
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