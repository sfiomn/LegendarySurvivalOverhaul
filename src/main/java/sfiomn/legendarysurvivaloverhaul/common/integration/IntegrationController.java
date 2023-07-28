package sfiomn.legendarysurvivaloverhaul.common.integration;

import net.minecraftforge.fml.ModList;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonPropertyValue;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;

/**
 * This class is specifically for implementing default configuration values
 * for mods that don't require us to set up new modifiers; i.e. simply setting
 * relevant values for armor/block temperatures, consumable temperatures, etc.
 * @author Icey
 */
public final class IntegrationController
{
	public static void initCompat()
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
		if (mods.isLoaded("artifacts"))
			initArtifacts();
		if (mods.isLoaded("endergetic"))
			initEndergeticExpansion();
		if (mods.isLoaded("infernalexp"))
			initInfernalExpansion();
		if (mods.isLoaded("biomesoplenty"))
			initBiomesOPlenty();
		if (mods.isLoaded("betterendforge"))
			initBetterEndForge();
		
		if (mods.isLoaded("neapolitan"))
		{
			initNeapolitan();
			
			// Since Seasonals and Peculiars depend on Neapolitan,
			// we can skip checking them if Neapolitan isn't installed
			if (mods.isLoaded("seasonals"))
				initSeasonals();
			if (mods.isLoaded("peculiars"))
				initPeculiars();
		}
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
		
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:beef_stew", 2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:chicken_soup", 2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:vegetable_soup", 2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:fish_stew", 2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:pumpkin_soup", 2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:baked_cod_stew", 2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "farmersdelight:hot_cocoa", 3, 3600);
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
	
	private static void initArtifacts()
	{
		JsonConfig.registerItemTemperature("artifacts:villager_hat", -2.5f);
		JsonConfig.registerItemTemperature("artifacts:lucky_scarf", 2.5f);
		JsonConfig.registerItemTemperature("artifacts:scarf_of_invisibility", 2.5f);
	}
	
	private static void initEndergeticExpansion() 
	{
		JsonConfig.registerBlockTemperature("endergetic:ender_fire", -5.0f);
		JsonConfig.registerBlockTemperature("endergetic:ender_campfire", -7.5f);
		JsonConfig.registerBlockTemperature("endergetic:ender_torch", -1.5f);
		JsonConfig.registerBlockTemperature("endergetic:ender_wall_torch", -1.5f);
	}
	
	private static void initInfernalExpansion()
	{
		JsonConfig.registerBlockTemperature("infernalexp:fire_glow", 5.0f);
		JsonConfig.registerBlockTemperature("infernalexp:campfire_glow", 7.5f);
		JsonConfig.registerBlockTemperature("infernalexp:torch_glow", 1.5f);
		JsonConfig.registerBlockTemperature("infernalexp:torch_glow_wall", 1.5f);
	}
	
	private static void initBiomesOPlenty()
	{
		JsonConfig.registerBiomeOverride("biomesoplenty:crystalline_chasm", 0.8f, false);
		JsonConfig.registerBiomeOverride("biomesoplenty:undergrowth", 0.75f, false);
		JsonConfig.registerBiomeOverride("biomesoplenty:visceral_heap", 0.9f, false);
		JsonConfig.registerBiomeOverride("biomesoplenty:withered_abyss", 1.5f, false);
	}
	
	private static void initNeapolitan()
	{
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "neapolitan:ice_cubes", -1, 3600);
		
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "neapolitan:chocolate_ice_cream", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "neapolitan:vanilla_ice_cream", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "neapolitan:strawberry_ice_cream", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "neapolitan:banana_ice_cream", -3, 3600);

		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "neapolitan:chocolate_milkshake", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "neapolitan:vanilla_milkshake", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "neapolitan:strawberry_milkshake", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "neapolitan:banana_milkshake", -3, 3600);
		
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "neapolitan:neapolitan_ice_cream", -3, 3600);
	}
	
	private static void initSeasonals()
	{
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "seasonals:pumpkin_ice_cream", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "seasonals:sweet_berry_ice_cream", -3, 3600);
		
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "seasonals:pumpkin_milkshake", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "seasonals:sweet_berry_milkshake", -3, 3600);
	}
	
	private static void initPeculiars()
	{
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "peculiars:yucca_ice_cream", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "peculiars:aloe_ice_cream", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "peculiars:passionfruit_ice_cream", -3, 3600);
		
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "peculiars:yucca_milkshake", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "peculiars:aloe_milkshake", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "peculiars:passionfruit_milkshake", -3, 3600);
	}
	
	private static void initBetterEndForge()
	{
		JsonConfig.registerBiomeOverride("betterendforge:sulphur_springs", 1.1f);
		JsonConfig.registerBiomeOverride("betterendforge:ice_starfield", 0.1f);
	}
}
