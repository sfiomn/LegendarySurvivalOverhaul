package sfiomn.legendarysurvivaloverhaul.config.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.config.json.JsonPropertyValue;
import sfiomn.legendarysurvivaloverhaul.api.config.json.bodydamage.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.*;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonThirst;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;
import sfiomn.legendarysurvivaloverhaul.common.integration.IntegrationController;
import sfiomn.legendarysurvivaloverhaul.config.JsonFileName;
import sfiomn.legendarysurvivaloverhaul.config.JsonTypeToken;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

public class JsonConfigRegistration
{
	public static void init(File configDir)
	{
		registerDefaults(configDir);

		processAllJson(configDir);

		writeAllToJson(configDir);
	}
	
	public static void registerDefaults(File configDir)
	{
		JsonConfig.registerBlockTemperature("minecraft:campfire", 16f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockTemperature("minecraft:soul_campfire", -15f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockTemperature("minecraft:campfire", 0.0f, new JsonPropertyValue("lit", "false"));
		JsonConfig.registerBlockTemperature("minecraft:soul_campfire", 0.0f, new JsonPropertyValue("lit", "false"));

		JsonConfig.registerBlockTemperature("minecraft:torch", 3.0f);
		JsonConfig.registerBlockTemperature("minecraft:soul_torch", 0.75f);

		JsonConfig.registerBlockTemperature("minecraft:wall_torch", 3.0f);
		JsonConfig.registerBlockTemperature("minecraft:soul_wall_torch", 0.75f);

		JsonConfig.registerBlockTemperature("minecraft:fire", 10.0f);
		JsonConfig.registerBlockTemperature("minecraft:soul_fire", 2.5f);

		JsonConfig.registerBlockTemperature("minecraft:furnace", 5.0f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockTemperature("minecraft:blast_furnace", 5.0f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockTemperature("minecraft:smoker", 5.0f, new JsonPropertyValue("lit", "true"));

		JsonConfig.registerBlockTemperature("minecraft:furnace", 0.0f, new JsonPropertyValue("lit", "false"));
		JsonConfig.registerBlockTemperature("minecraft:blast_furnace", 0.0f, new JsonPropertyValue("lit", "false"));
		JsonConfig.registerBlockTemperature("minecraft:smoker", 0.0f, new JsonPropertyValue("false", "true"));

		JsonConfig.registerBlockTemperature(LegendarySurvivalOverhaul.MOD_ID + ":cooler", -17.5f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockTemperature(LegendarySurvivalOverhaul.MOD_ID + ":heater", 17.5f, new JsonPropertyValue("lit", "true"));

		JsonConfig.registerBlockTemperature(LegendarySurvivalOverhaul.MOD_ID + ":cooler", 0.0f, new JsonPropertyValue("lit", "false"));
		JsonConfig.registerBlockTemperature(LegendarySurvivalOverhaul.MOD_ID + ":heater", 0.0f, new JsonPropertyValue("lit", "false"));

		JsonConfig.registerBlockTemperature("minecraft:magma_block", 10.0f);

		JsonConfig.registerBlockTemperature("minecraft:jack_o_lantern", 3.0f);

		JsonConfig.registerBlockTemperature("minecraft:lava", 12.5f);
		JsonConfig.registerBlockTemperature("minecraft:flowing_lava", 12.5f);

		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":snow_boots", 0.5f);
		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":snow_leggings", 2.5f);
		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":snow_chestplate", 3.0f);
		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":snow_helmet", 1.5f);

		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":desert_boots", -0.5f);
		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":desert_leggings", -2.5f);
		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":desert_chestplate", -3.0f);
		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":desert_helmet", -1.5f);

		JsonConfig.registerItemTemperature("minecraft:leather_boots", 0.25f);
		JsonConfig.registerItemTemperature("minecraft:leather_leggings", 0.75f);
		JsonConfig.registerItemTemperature("minecraft:leather_chestplate", 1.0f);
		JsonConfig.registerItemTemperature("minecraft:leather_helmet", 0.5f);

		JsonConfig.registerItemTemperature("minecraft:iron_boots", 0f);
		JsonConfig.registerItemTemperature("minecraft:iron_leggings", 0f);
		JsonConfig.registerItemTemperature("minecraft:iron_chestplate", 0f);
		JsonConfig.registerItemTemperature("minecraft:iron_helmet", 0f);

		JsonConfig.registerItemTemperature("minecraft:torch", 1.0f);

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

		JsonConfig.registerConsumableThirst("minecraft:melon_slice", 2, 1.0f, 0);
		JsonConfig.registerConsumableThirst("minecraft:rotten_flesh", -1, 0.0f, 1.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":apple_juice",6,3.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":beetroot_juice",10,8.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":cactus_juice",9,3.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":carrot_juice",4,2.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":chorus_fruit_juice",12,10.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":golden_apple_juice",20,20.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":golden_carrot_juice",12,12.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":glistering_melon_juice",16,16.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":melon_juice",8,5.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":pumpkin_juice",7,4.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":purified_water_bottle", 6, 1.5f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":water_plant_bag", 2, 0.0f);

		// Body Damage
		JsonConfig.registerDamageSourceBodyParts("fall", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT));
		JsonConfig.registerDamageSourceBodyParts("hotFloor", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT));
		JsonConfig.registerDamageSourceBodyParts("fallingBlock", DamageDistributionEnum.ALL, Collections.singletonList(BodyPartEnum.HEAD));
		JsonConfig.registerDamageSourceBodyParts("flyIntoWall", DamageDistributionEnum.ALL, Collections.singletonList(BodyPartEnum.HEAD));
		JsonConfig.registerDamageSourceBodyParts("anvil", DamageDistributionEnum.ALL, Collections.singletonList(BodyPartEnum.HEAD));
		JsonConfig.registerDamageSourceBodyParts("lightningBolt", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.values()));
		JsonConfig.registerDamageSourceBodyParts("onFire", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.values()));
		JsonConfig.registerDamageSourceBodyParts("dragonBreath", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.values()));
		JsonConfig.registerDamageSourceBodyParts("inFire", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
		JsonConfig.registerDamageSourceBodyParts("cactus", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
		JsonConfig.registerDamageSourceBodyParts("sweetBerryBush", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
		JsonConfig.registerDamageSourceBodyParts("drown", DamageDistributionEnum.ALL, Collections.singletonList(BodyPartEnum.CHEST));
		JsonConfig.registerDamageSourceBodyParts("starve", DamageDistributionEnum.ALL, Collections.singletonList(BodyPartEnum.CHEST));
		JsonConfig.registerDamageSourceBodyParts(LegendarySurvivalOverhaul.MOD_ID + ".hypothermia", DamageDistributionEnum.ALL, Collections.singletonList(BodyPartEnum.CHEST));
		JsonConfig.registerDamageSourceBodyParts(LegendarySurvivalOverhaul.MOD_ID + ".hyperthermia", DamageDistributionEnum.ALL, Collections.singletonList(BodyPartEnum.CHEST));
		JsonConfig.registerDamageSourceBodyParts(LegendarySurvivalOverhaul.MOD_ID + ".dehydration", DamageDistributionEnum.ALL, Collections.singletonList(BodyPartEnum.CHEST));

		IntegrationController.initCompat();
	}

	
	public static void clearContainers()
	{
		JsonConfig.itemTemperatures.clear();
		JsonConfig.blockTemperatures.clear();
		JsonConfig.biomeOverrides.clear();
		JsonConfig.consumableTemperature.clear();
		JsonConfig.fuelItems.clear();
		JsonConfig.consumableThirst.clear();
		JsonConfig.damageSourceBodyParts.clear();
	}

	public static void writeAllToJson(File jsonDir) {
		manuallyWriteToJson(JsonFileName.ITEM, JsonConfig.itemTemperatures, jsonDir);
		manuallyWriteToJson(JsonFileName.BLOCK, JsonConfig.blockTemperatures, jsonDir);
		manuallyWriteToJson(JsonFileName.BIOME, JsonConfig.biomeOverrides, jsonDir);
		manuallyWriteToJson(JsonFileName.CONSUMABLE_TEMP, JsonConfig.consumableTemperature, jsonDir);
		manuallyWriteToJson(JsonFileName.FUEL, JsonConfig.fuelItems, jsonDir);
		manuallyWriteToJson(JsonFileName.CONSUMABLE_THIRST, JsonConfig.consumableThirst, jsonDir);
		manuallyWriteToJson(JsonFileName.DAMAGE_SOURCE_BODY_PARTS, JsonConfig.damageSourceBodyParts, jsonDir);
	}
	
	public static void processAllJson(File jsonDir)
	{
		// Temperature
		Map<String, JsonTemperature> jsonArmorTemperatures = processJson(JsonFileName.ITEM, jsonDir);

		if (jsonArmorTemperatures != null)
		{
			// remove default armor config
			JsonConfig.itemTemperatures.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonArmorTemperatures.size() + " armor temperature values from JSON");
			for (Map.Entry<String, JsonTemperature> entry : jsonArmorTemperatures.entrySet())
			{
				JsonConfig.registerItemTemperature(entry.getKey(), entry.getValue().temperature);
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
		
		Map<String, List<JsonConsumableTemperature>> jsonConsumableTemperatures = processJson(JsonFileName.CONSUMABLE_TEMP, jsonDir);

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

		// Thirst
		Map<String, JsonThirst> jsonConsumableThirst = processJson(JsonFileName.CONSUMABLE_THIRST, jsonDir);

		if (jsonConsumableThirst != null)
		{
			// remove default consumables config
			JsonConfig.consumableThirst.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonConsumableThirst.size() + " consumable thirst values from JSON");
			for (Map.Entry<String, JsonThirst> entry : jsonConsumableThirst.entrySet())
			{
				JsonConfig.registerConsumableThirst(entry.getKey(), entry.getValue().hydration, entry.getValue().saturation, entry.getValue().dirty);
			}
		}

		// Damage Sources Body Parts
		Map<String, JsonBodyPartsDamageSource> jsonDamageSourceBodyParts = processJson(JsonFileName.DAMAGE_SOURCE_BODY_PARTS, jsonDir);

		if (jsonDamageSourceBodyParts != null)
		{
			// remove default consumables config
			JsonConfig.damageSourceBodyParts.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonDamageSourceBodyParts.size() + " damage source body part values from JSON");
			for (Map.Entry<String, JsonBodyPartsDamageSource> entry : jsonDamageSourceBodyParts.entrySet())
			{
				JsonConfig.registerDamageSourceBodyParts(entry.getKey(), entry.getValue().damageDistribution, entry.getValue().bodyParts);
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
	
	private static <T> void manuallyWriteToJson(JsonFileName jfn, final T container, File jsonDir)
	{
		try {
			manuallyWriteToJson(jfn, container, jsonDir, false);
		} catch (Exception e) {
			LegendarySurvivalOverhaul.LOGGER.error("Error writing " + jfn + " JSON file", e);
		}
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