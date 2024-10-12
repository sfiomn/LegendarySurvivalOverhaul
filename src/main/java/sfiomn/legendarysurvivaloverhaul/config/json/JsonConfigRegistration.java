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
import sfiomn.legendarysurvivaloverhaul.api.config.json.bodydamage.JsonConsumableHeal;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.*;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonBlockFluidThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonEffectParameter;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.common.blocks.IceFernBlock;
import sfiomn.legendarysurvivaloverhaul.common.blocks.SunFernBlock;
import sfiomn.legendarysurvivaloverhaul.common.integration.IntegrationController;
import sfiomn.legendarysurvivaloverhaul.config.JsonFileName;
import sfiomn.legendarysurvivaloverhaul.config.JsonTypeToken;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

import static sfiomn.legendarysurvivaloverhaul.util.internal.ThirstUtilInternal.HYDRATION_ENUM_TAG;

public class JsonConfigRegistration
{
	public static void init(File configDir) {
		registerDefaults(configDir);

		processAllJson(configDir);

		writeAllToJson(configDir);
	}
	
	public static void registerDefaults(File configDir)
	{
		JsonConfig.registerDimensionTemperature("minecraft:overworld", 20);
		JsonConfig.registerDimensionTemperature("minecraft:the_end", -15);
		JsonConfig.registerDimensionTemperature("minecraft:the_nether", 28);

		JsonConfig.registerBlockFluidTemperature("minecraft:campfire", 10f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockFluidTemperature("minecraft:soul_campfire", -10f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockFluidTemperature("minecraft:campfire", 0.0f, new JsonPropertyValue("lit", "false"));
		JsonConfig.registerBlockFluidTemperature("minecraft:soul_campfire", 0.0f, new JsonPropertyValue("lit", "false"));

		JsonConfig.registerBlockFluidTemperature("minecraft:torch", 1.5f);
		JsonConfig.registerBlockFluidTemperature("minecraft:soul_torch", -1.5f);

		JsonConfig.registerBlockFluidTemperature("minecraft:wall_torch", 1.5f);
		JsonConfig.registerBlockFluidTemperature("minecraft:soul_wall_torch", 0.75f);

		JsonConfig.registerBlockFluidTemperature("minecraft:fire", 7.0f);
		JsonConfig.registerBlockFluidTemperature("minecraft:soul_fire", -7.0f);

		JsonConfig.registerBlockFluidTemperature("minecraft:furnace", 6.0f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockFluidTemperature("minecraft:blast_furnace", 6.0f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockFluidTemperature("minecraft:smoker", 6.0f, new JsonPropertyValue("lit", "true"));

		JsonConfig.registerBlockFluidTemperature("minecraft:furnace", 0.0f, new JsonPropertyValue("lit", "false"));
		JsonConfig.registerBlockFluidTemperature("minecraft:blast_furnace", 0.0f, new JsonPropertyValue("lit", "false"));
		JsonConfig.registerBlockFluidTemperature("minecraft:smoker", 0.0f, new JsonPropertyValue("false", "true"));

		JsonConfig.registerBlockFluidTemperature(LegendarySurvivalOverhaul.MOD_ID + ":cooler", -15f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockFluidTemperature(LegendarySurvivalOverhaul.MOD_ID + ":heater", 15f, new JsonPropertyValue("lit", "true"));

		JsonConfig.registerBlockFluidTemperature(LegendarySurvivalOverhaul.MOD_ID + ":cooler", 0.0f, new JsonPropertyValue("lit", "false"));
		JsonConfig.registerBlockFluidTemperature(LegendarySurvivalOverhaul.MOD_ID + ":heater", 0.0f, new JsonPropertyValue("lit", "false"));

		JsonConfig.registerBlockFluidTemperature(LegendarySurvivalOverhaul.MOD_ID + ":ice_fern_crop", -1.5f, new JsonPropertyValue(IceFernBlock.AGE.getName(), String.valueOf(IceFernBlock.MAX_AGE)));
		JsonConfig.registerBlockFluidTemperature(LegendarySurvivalOverhaul.MOD_ID + ":sun_fern_crop", 1.5f, new JsonPropertyValue(SunFernBlock.AGE.getName(), String.valueOf(SunFernBlock.MAX_AGE)));

		JsonConfig.registerBlockFluidTemperature("minecraft:magma_block", 12.0f);

		JsonConfig.registerBlockFluidTemperature("minecraft:jack_o_lantern", 3.0f);

		JsonConfig.registerBlockFluidTemperature("minecraft:lava", 12.5f);
		JsonConfig.registerBlockFluidTemperature("minecraft:flowing_lava", 12.5f);

		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":snow_boots", 0, 0, 0.5f, 0);
		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":snow_leggings", 0, 0, 2.5f, 0);
		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":snow_chestplate", 0, 0, 3.0f, 0);
		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":snow_helmet", 0, 0, 1.5f, 0);

		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":desert_boots", 0, 0.5f, 0, 0);
		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":desert_leggings", 0, 2.5f, 0, 0);
		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":desert_chestplate", 0, 3.0f, 0, 0);
		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":desert_helmet", 0, 1.5f, 0, 0);

		JsonConfig.registerItemTemperature(LegendarySurvivalOverhaul.MOD_ID + ":nether_chalice", 2f);
		JsonConfig.registerItemTemperature("minecraft:lava_bucket", 6f);
		JsonConfig.registerItemTemperature("minecraft:magma_block", 6f);

		JsonConfig.registerItemTemperature("minecraft:leather_boots", 0, 0, 1.0f, 0);
		JsonConfig.registerItemTemperature("minecraft:leather_leggings", 0, 0, 1.0f, 0);
		JsonConfig.registerItemTemperature("minecraft:leather_chestplate", 0, 0, 1.5f, 0);
		JsonConfig.registerItemTemperature("minecraft:leather_helmet", 0, 0, 0.5f, 0);

		JsonConfig.registerItemTemperature("minecraft:golden_boots", 0, 0, 0.5f, 0);
		JsonConfig.registerItemTemperature("minecraft:golden_leggings", 0, 0, 1.0f, 0);
		JsonConfig.registerItemTemperature("minecraft:golden_chestplate", 0, 0, 1.0f, 0);
		JsonConfig.registerItemTemperature("minecraft:golden_helmet", 0, 0, 0.5f, 0);

		JsonConfig.registerItemTemperature("minecraft:iron_boots", 0, 0.5f, 0, 0);
		JsonConfig.registerItemTemperature("minecraft:iron_leggings", 0, 1.0f, 0, 0);
		JsonConfig.registerItemTemperature("minecraft:iron_chestplate", 0, 1.0f, 0, 0);
		JsonConfig.registerItemTemperature("minecraft:iron_helmet", 0, 0.5f, 0, 0);

		JsonConfig.registerItemTemperature("minecraft:diamond_boots", 0, 1.0f, 0, 0);
		JsonConfig.registerItemTemperature("minecraft:diamond_leggings", 0, 1.0f, 0, 0);
		JsonConfig.registerItemTemperature("minecraft:diamond_chestplate", 0, 1.5f, 0, 0);
		JsonConfig.registerItemTemperature("minecraft:diamond_helmet", 0, 0.5f, 0, 0);

		JsonConfig.registerItemTemperature("minecraft:netherite_boots", 0, 0, 1.5f, 0);
		JsonConfig.registerItemTemperature("minecraft:netherite_leggings", 0, 0, 1.5f, 0);
		JsonConfig.registerItemTemperature("minecraft:netherite_chestplate", 0, 0, 2.0f, 0);
		JsonConfig.registerItemTemperature("minecraft:netherite_helmet", 0, 0, 1.0f, 0);

		JsonConfig.registerItemTemperature("minecraft:torch", 1.0f);
		JsonConfig.registerItemTemperature("minecraft:soul_torch", -1.0f);
		JsonConfig.registerItemTemperature("minecraft:ice", -1.0f);
		JsonConfig.registerItemTemperature("minecraft:packed_ice", -2.0f);
		JsonConfig.registerItemTemperature("minecraft:blue_ice", -3.0f);

		JsonConfig.registerEntityTemperature("minecraft:strider", -3.0f);

		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "minecraft:mushroom_stew", 1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "minecraft:mushroom_stew", 1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "minecraft:rabbit_stew", 2, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "minecraft:suspicious_stew", 1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "minecraft:melon_slice", -1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, LegendarySurvivalOverhaul.MOD_ID + ":melon_juice", -1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, LegendarySurvivalOverhaul.MOD_ID + ":glistering_melon_juice", -2, 3600);

		JsonConfig.registerBiomeOverride("minecraft:crimson_forest", 0.75f, false);
		JsonConfig.registerBiomeOverride("minecraft:warped_forest", 0.75f, false);
		JsonConfig.registerBiomeOverride("minecraft:nether_wastes", 1.0f, false);
		JsonConfig.registerBiomeOverride("minecraft:soul_sand_valley", 1.0f, false);
		JsonConfig.registerBiomeOverride("minecraft:basalt_deltas", 1.45f, false);
		JsonConfig.registerBiomeOverride("minecraft:frozen_ocean", -0.5f, false);
		JsonConfig.registerBiomeOverride("minecraft:deep_frozen_ocean", -0.5f, false);

		JsonConfig.registerFuelItems("minecraft:coal", ThermalTypeEnum.HEATING, 30);
		JsonConfig.registerFuelItems("minecraft:charcoal", ThermalTypeEnum.HEATING, 30);
		JsonConfig.registerFuelItems("minecraft:coal_block", ThermalTypeEnum.HEATING, 270);
		JsonConfig.registerFuelItems("minecraft:ice", ThermalTypeEnum.COOLING, 30);
		JsonConfig.registerFuelItems("minecraft:snowball", ThermalTypeEnum.COOLING, 30);
		JsonConfig.registerFuelItems("minecraft:snow_block", ThermalTypeEnum.COOLING, 30);
		JsonConfig.registerFuelItems("minecraft:blue_ice", ThermalTypeEnum.COOLING, 30);
		JsonConfig.registerFuelItems("minecraft:packed_ice", ThermalTypeEnum.COOLING, 30);

		JsonConfig.registerBlockFluidThirst("minecraft:rain", 1, 0);
		JsonConfig.registerBlockFluidThirst("minecraft:flowing_water", 3, 0, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)});
		JsonConfig.registerBlockFluidThirst("minecraft:water", 3, 0, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)});
		JsonConfig.registerBlockFluidThirst("minecraft:water_cauldron", 3, 0, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)});

		JsonConfig.registerConsumableThirst("minecraft:melon_slice", 2, 1.0f);
		JsonConfig.registerConsumableThirst("minecraft:apple", 2, 0.5f);
		JsonConfig.registerConsumableThirst("minecraft:beetroot_soup", 4, 2.0f);
		JsonConfig.registerConsumableThirst("minecraft:rabbit_stew", 6, 2.0f);
		JsonConfig.registerConsumableThirst("minecraft:mushroom_stew", 4, 2.0f);
		JsonConfig.registerConsumableThirst("minecraft:suspicious_stew", 4, 2.0f);
		JsonConfig.registerConsumableThirst("minecraft:rotten_flesh", -2, -1.0f, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 1.0f, 600, 0)});
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":apple_juice",6,3.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":beetroot_juice",9,4.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":cactus_juice",9,3.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":carrot_juice",4,2.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":chorus_fruit_juice",12,8.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":golden_apple_juice",20,20.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":golden_carrot_juice",12,12.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":glistering_melon_juice",16,16.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":melon_juice",8,4.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":pumpkin_juice",7,4.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":purified_water_bottle", 6, 1.5f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":water_plant_bag", 3, 0.0f);
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":canteen", 3, 0.0f, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)}, new JsonPropertyValue(HYDRATION_ENUM_TAG, HydrationEnum.NORMAL.getName()));
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":canteen", 6, 1.5f, new JsonPropertyValue(HYDRATION_ENUM_TAG, HydrationEnum.PURIFIED.getName()));
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":large_canteen", 3, 0.0f, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)}, new JsonPropertyValue(HYDRATION_ENUM_TAG, HydrationEnum.NORMAL.getName()));
		JsonConfig.registerConsumableThirst(LegendarySurvivalOverhaul.MOD_ID + ":large_canteen", 6, 1.5f, new JsonPropertyValue(HYDRATION_ENUM_TAG, HydrationEnum.PURIFIED.getName()));

		JsonConfig.registerConsumableThirst("minecraft:potion", 3, 0.0f, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)}, new JsonPropertyValue("Potion", "minecraft:water"));
		JsonConfig.registerConsumableThirst("minecraft:potion", 3, 0.0f, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)}, new JsonPropertyValue("Potion", "minecraft:mundane"));
		JsonConfig.registerConsumableThirst("minecraft:potion", 3, 0.0f, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)}, new JsonPropertyValue("Potion", "minecraft:thick"));
		JsonConfig.registerConsumableThirst("minecraft:potion", 3, 0.0f, new JsonEffectParameter[]{new JsonEffectParameter(LegendarySurvivalOverhaul.MOD_ID + ":thirst", 0.75f, 300, 0)}, new JsonPropertyValue("Potion", "minecraft:awkward"));
		JsonConfig.registerConsumableThirst("minecraft:potion", 0, 0.0f, new JsonEffectParameter[]{}, new JsonPropertyValue("Potion", "minecraft:empty"));
		JsonConfig.registerConsumableThirst("minecraft:potion", 6, 1.5f, new JsonEffectParameter[]{});

		// Body Damage
		JsonConfig.registerConsumableHeal(LegendarySurvivalOverhaul.MOD_ID + ":healing_herbs", 1, 2, 600);
		JsonConfig.registerConsumableHeal(LegendarySurvivalOverhaul.MOD_ID + ":plaster", 1, 3, 400);
		JsonConfig.registerConsumableHeal(LegendarySurvivalOverhaul.MOD_ID + ":bandage", 3, 3, 300);
		JsonConfig.registerConsumableHeal(LegendarySurvivalOverhaul.MOD_ID + ":tonic", 0, 5, 600);
		JsonConfig.registerConsumableHeal(LegendarySurvivalOverhaul.MOD_ID + ":medikit", 0, 8, 400);

		JsonConfig.registerDamageSourceBodyParts("fall", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT));
		JsonConfig.registerDamageSourceBodyParts("hotFloor", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT));
		JsonConfig.registerDamageSourceBodyParts("fallingBlock", DamageDistributionEnum.ALL, Collections.singletonList(BodyPartEnum.HEAD));
		JsonConfig.registerDamageSourceBodyParts("flyIntoWall", DamageDistributionEnum.ALL, Collections.singletonList(BodyPartEnum.HEAD));
		JsonConfig.registerDamageSourceBodyParts("anvil", DamageDistributionEnum.ALL, Collections.singletonList(BodyPartEnum.HEAD));
		JsonConfig.registerDamageSourceBodyParts("lightningBolt", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.values()));
		JsonConfig.registerDamageSourceBodyParts("onFire", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.values()));
		JsonConfig.registerDamageSourceBodyParts("explosion", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.values()));
		JsonConfig.registerDamageSourceBodyParts("bad_respawn_point", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.values()));
		JsonConfig.registerDamageSourceBodyParts("dragonBreath", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.values()));
		JsonConfig.registerDamageSourceBodyParts("inFire", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
		JsonConfig.registerDamageSourceBodyParts("cactus", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
		JsonConfig.registerDamageSourceBodyParts("sweetBerryBush", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
		JsonConfig.registerDamageSourceBodyParts("in_wall", DamageDistributionEnum.NONE, Collections.emptyList());
		JsonConfig.registerDamageSourceBodyParts("drown", DamageDistributionEnum.NONE, Collections.emptyList());
		JsonConfig.registerDamageSourceBodyParts("starve", DamageDistributionEnum.NONE, Collections.emptyList());
		JsonConfig.registerDamageSourceBodyParts("magic", DamageDistributionEnum.NONE, Collections.emptyList());
		JsonConfig.registerDamageSourceBodyParts("wither", DamageDistributionEnum.NONE, Collections.emptyList());
		JsonConfig.registerDamageSourceBodyParts(LegendarySurvivalOverhaul.MOD_ID + ".hypothermia", DamageDistributionEnum.NONE, Collections.emptyList());
		JsonConfig.registerDamageSourceBodyParts(LegendarySurvivalOverhaul.MOD_ID + ".hyperthermia", DamageDistributionEnum.NONE, Collections.emptyList());
		JsonConfig.registerDamageSourceBodyParts(LegendarySurvivalOverhaul.MOD_ID + ".dehydration", DamageDistributionEnum.NONE, Collections.emptyList());

		IntegrationController.initIntegration();
	}

	public static void writeAllToJson(File jsonDir) {
		manuallyWriteToJson(JsonFileName.DIMENSION_TEMP, JsonConfig.dimensionTemperatures, jsonDir);
		manuallyWriteToJson(JsonFileName.BIOME_TEMP, JsonConfig.biomeOverrides, jsonDir);
		manuallyWriteToJson(JsonFileName.ITEM_TEMP, JsonConfig.itemTemperatures, jsonDir);
		manuallyWriteToJson(JsonFileName.BLOCK_TEMP, JsonConfig.blockFluidTemperatures, jsonDir);
		manuallyWriteToJson(JsonFileName.FUEL, JsonConfig.fuelItems, jsonDir);
		manuallyWriteToJson(JsonFileName.CONSUMABLE_TEMP, JsonConfig.consumableTemperature, jsonDir);
		manuallyWriteToJson(JsonFileName.BLOCK_THIRST, JsonConfig.blockFluidThirst, jsonDir);
		manuallyWriteToJson(JsonFileName.CONSUMABLE_THIRST, JsonConfig.consumableThirst, jsonDir);
		manuallyWriteToJson(JsonFileName.CONSUMABLE_HEAL, JsonConfig.consumableHeal, jsonDir);
		manuallyWriteToJson(JsonFileName.DAMAGE_SOURCE_BODY_PARTS, JsonConfig.damageSourceBodyParts, jsonDir);
	}
	
	public static void processAllJson(File jsonDir)
	{
		// Temperature
		Map<String, JsonTemperature> jsonDimensionTemperatures = processJson(JsonFileName.DIMENSION_TEMP, jsonDir);

		if (jsonDimensionTemperatures != null)
		{
			// remove default biome config
			JsonConfig.dimensionTemperatures.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonDimensionTemperatures.size() + " dimension temperature overrides from JSON");
			for (Map.Entry<String, JsonTemperature> entry : jsonDimensionTemperatures.entrySet())
			{
				JsonConfig.registerDimensionTemperature(entry.getKey(), entry.getValue().temperature);
			}
		}

		Map<String, JsonBiomeIdentity> jsonBiomeIdentities = processJson(JsonFileName.BIOME_TEMP, jsonDir);

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

		Map<String, JsonTemperatureResistance> jsonItemTemperatures = processJson(JsonFileName.ITEM_TEMP, jsonDir);

		if (jsonItemTemperatures != null)
		{
			// remove default item config
			JsonConfig.itemTemperatures.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonItemTemperatures.size() + " item temperature values from JSON");
			for (Map.Entry<String, JsonTemperatureResistance> entry : jsonItemTemperatures.entrySet())
			{
				JsonConfig.registerItemTemperature(entry.getKey(), entry.getValue().temperature, entry.getValue().heatResistance, entry.getValue().coldResistance, entry.getValue().thermalResistance);
			}
		}

		Map<String, JsonTemperature> jsonEntityTemperatures = processJson(JsonFileName.ENTITY_TEMP, jsonDir);

		if (jsonEntityTemperatures != null)
		{
			// remove default armor config
			JsonConfig.entityTemperatures.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonEntityTemperatures.size() + " entity temperature values from JSON");
			for (Map.Entry<String, JsonTemperature> entry : jsonEntityTemperatures.entrySet())
			{
				JsonConfig.registerEntityTemperature(entry.getKey(), entry.getValue().temperature);
			}
		}
		
		Map<String, List<JsonBlockFluidTemperature>> jsonBlockFluidTemperatures = processJson(JsonFileName.BLOCK_TEMP, jsonDir);
		
		if (jsonBlockFluidTemperatures != null)
		{
			// remove default block config
			JsonConfig.blockFluidTemperatures.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonBlockFluidTemperatures.size() + " block/fluid temperature values from JSON");
			for (Map.Entry<String, List<JsonBlockFluidTemperature>> entry : jsonBlockFluidTemperatures.entrySet())
			{
				for (JsonBlockFluidTemperature propTemp : entry.getValue())
				{
					JsonConfig.registerBlockFluidTemperature(entry.getKey(), propTemp.temperature, propTemp.getPropertyArray());
				}
			}
		}

		Map<String, JsonFuelItem> jsonFuelItemIdentities = processJson(JsonFileName.FUEL, jsonDir);

		if (jsonFuelItemIdentities != null)
		{
			// remove default fuel config
			JsonConfig.fuelItems.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonFuelItemIdentities.size() + " fuel item values from JSON");
			for (Map.Entry<String, JsonFuelItem> entry : jsonFuelItemIdentities.entrySet())
			{
				JsonConfig.registerFuelItems(entry.getKey(), entry.getValue().thermalType, entry.getValue().fuelValue);
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

		// Thirst

		Map<String, List<JsonBlockFluidThirst>> jsonBlockFluidThirsts = processJson(JsonFileName.BLOCK_THIRST, jsonDir);

		if (jsonBlockFluidThirsts != null)
		{
			// remove default block config
			JsonConfig.blockFluidThirst.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonBlockFluidThirsts.size() + " block/fluid thirst values from JSON");
			for (Map.Entry<String, List<JsonBlockFluidThirst>> entry : jsonBlockFluidThirsts.entrySet())
			{
				for (JsonBlockFluidThirst propThirst : entry.getValue())
				{
					JsonConfig.registerBlockFluidThirst(entry.getKey(), propThirst.hydration, propThirst.saturation, propThirst.effects.toArray(new JsonEffectParameter[0]), propThirst.getPropertyArray());
				}
			}
		}

		Map<String, List<JsonConsumableThirst>> jsonConsumableThirsts = processJson(JsonFileName.CONSUMABLE_THIRST, jsonDir);

		if (jsonConsumableThirsts != null)
		{
			// remove default consumables config
			JsonConfig.consumableThirst.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonConsumableThirsts.size() + " consumable thirst values from JSON");
			for (Map.Entry<String, List<JsonConsumableThirst>> entry : jsonConsumableThirsts.entrySet()) {
				for (JsonConsumableThirst jct: entry.getValue())
					JsonConfig.registerConsumableThirst(entry.getKey(), jct.hydration, jct.saturation, jct.effects.toArray(new JsonEffectParameter[0]), jct.getNbtArray());
			}
		}

		// Healing items
		Map<String, JsonConsumableHeal> jsonConsumableHeal = processJson(JsonFileName.CONSUMABLE_HEAL, jsonDir);

		if (jsonConsumableHeal != null)
		{
			// remove default consumables config
			JsonConfig.consumableHeal.clear();
			LegendarySurvivalOverhaul.LOGGER.debug("Loaded " + jsonConsumableHeal.size() + " consumable heal values from JSON");
			for (Map.Entry<String, JsonConsumableHeal> entry : jsonConsumableHeal.entrySet())
			{
				JsonConfig.registerConsumableHeal(entry.getKey(), entry.getValue().healingCharges, entry.getValue().healingValue, entry.getValue().healingTime);
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
	
	public static <T> void manuallyWriteToJson(JsonFileName jfn, final T container, File jsonDir)
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