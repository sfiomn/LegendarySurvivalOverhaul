package sfiomn.legendarysurvivaloverhaul.common.integration;

import net.minecraftforge.fml.ModList;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;
import sfiomn.legendarysurvivaloverhaul.api.config.json.JsonPropertyValue;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfigRegistration;

import java.util.Arrays;
import java.util.Collections;

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
		if (mods.isLoaded("atmospheric"))
			initAtmospheric();
		if (mods.isLoaded("legendarycreatures"))
			initLegendaryCreatures();
		
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

		if (mods.isLoaded("supplementaries"))
			initSupplementaries();
		if (mods.isLoaded("crockpot"))
			initCrockpot();
		if (mods.isLoaded("quark"))
			initQuark();
		if ((mods.isLoaded("beachparty")))
			initBeachParty();
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

		JsonConfig.registerConsumableThirst("farmersdelight:chicken_soup", 2, 1.0f);
		JsonConfig.registerConsumableThirst("farmersdelight:vegetable_soup", 2, 1.0f);
		JsonConfig.registerConsumableThirst("farmersdelight:pumpkin_soup", 2, 1.0f);
		JsonConfig.registerConsumableThirst("farmersdelight:hot_cocoa", 4, 1.0f);
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
		JsonConfig.registerBlockTemperature("byg:boric_campfire", 10.0f, new JsonPropertyValue("lit", "true"));
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
		JsonConfig.registerBlockTemperature("endergetic:ender_fire", -7.0f);
		JsonConfig.registerBlockTemperature("endergetic:ender_campfire", -10.0f);
		JsonConfig.registerBlockTemperature("endergetic:ender_torch", -1.5f);
		JsonConfig.registerBlockTemperature("endergetic:ender_wall_torch", -1.5f);
	}
	
	private static void initInfernalExpansion()
	{
		JsonConfig.registerBlockTemperature("infernalexp:fire_glow", 7.0f);
		JsonConfig.registerBlockTemperature("infernalexp:campfire_glow", 10.0f);
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

		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "neapolitan:neapolitan_ice_cream", -3, 3600);

		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "neapolitan:chocolate_milkshake", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "neapolitan:vanilla_milkshake", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "neapolitan:strawberry_milkshake", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "neapolitan:banana_milkshake", -3, 3600);

		JsonConfig.registerConsumableThirst("neapolitan:chocolate_milkshake", 4, 1.0f);
		JsonConfig.registerConsumableThirst("neapolitan:vanilla_milkshake", 4, 1.0f);
		JsonConfig.registerConsumableThirst("neapolitan:strawberry_milkshake", 4, 1.0f);
		JsonConfig.registerConsumableThirst("neapolitan:banana_milkshake", 4, 1.0f);
	}
	
	private static void initSeasonals()
	{
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "seasonals:pumpkin_ice_cream", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "seasonals:sweet_berry_ice_cream", -3, 3600);
		
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "seasonals:pumpkin_milkshake", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "seasonals:sweet_berry_milkshake", -3, 3600);
		JsonConfig.registerConsumableThirst("seasonals:pumpkin_milkshake", 4, 1.0f);
		JsonConfig.registerConsumableThirst("seasonals:sweet_berry_milkshake", 4, 1.0f);
	}
	
	private static void initPeculiars() {
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "peculiars:yucca_ice_cream", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "peculiars:aloe_ice_cream", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "peculiars:passionfruit_ice_cream", -3, 3600);
		
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "peculiars:yucca_milkshake", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "peculiars:aloe_milkshake", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "peculiars:passionfruit_milkshake", -3, 3600);
		JsonConfig.registerConsumableThirst("peculiars:yucca_milkshake", 6, 2.0f);
		JsonConfig.registerConsumableThirst("peculiars:aloe_milkshake", 6, 2.0f);
		JsonConfig.registerConsumableThirst("peculiars:passionfruit_milkshake", 6, 2.0f);
	}
	
	private static void initBetterEndForge() {
		JsonConfig.registerBiomeOverride("betterendforge:sulphur_springs", 1.1f);
		JsonConfig.registerBiomeOverride("betterendforge:ice_starfield", 0.1f);
		JsonConfig.registerFuelItems("betterendforge:coal_block", ThermalTypeEnum.HEATING, 270);
		JsonConfig.registerFuelItems("betterendforge:charcoal_block", ThermalTypeEnum.HEATING, 270);
	}

	private static void initAtmospheric() {
		JsonConfig.registerDamageSourceBodyParts("atmospheric.yuccaSapling", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
		JsonConfig.registerDamageSourceBodyParts("atmospheric.yuccaFlower", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
		JsonConfig.registerDamageSourceBodyParts("atmospheric.yuccaBranch", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
		JsonConfig.registerDamageSourceBodyParts("atmospheric.yuccaLeaves", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
		JsonConfig.registerDamageSourceBodyParts("atmospheric.barrelCactus", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
		JsonConfig.registerDamageSourceBodyParts("atmospheric.aloeLeaves", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
	}

	private static void initLegendaryCreatures() {
		JsonConfig.registerDamageSourceBodyParts("legendarycreatures.root_attack", DamageDistributionEnum.ONE_OF, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
	}

	private static void initSupplementaries() {
		JsonConfig.registerDamageSourceBodyParts("supplementaries.bamboo_spikes", DamageDistributionEnum.ALL, Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT));
	}

	private static void initCrockpot() {
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:iced_tea", -2, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:ice_cream", -3, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:tea", 3, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:bone_stew", 1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:bunny_stew", 1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:bone_soup", 2, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:gazpacho", -2, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:fruit_medley", -1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:hot_chili", 2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:hot_cocoa", 3, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:moqueca", 1, 1800);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:pepper_popper", 3, 6000);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:salsa", -1, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:turkey_dinner", 2, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:veg_stinger", -1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:watermelon_icle", -4, 1800);
	}

	private static void initQuark() {
		JsonConfig.registerFuelItems("quark:coal_block", ThermalTypeEnum.HEATING, 270);
		JsonConfig.registerFuelItems("quark:charcoal_block", ThermalTypeEnum.HEATING, 270);
	}

	private static void initBeachParty() {
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:sweetberry_icecream", -2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:coconut_icecream", -2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:chocolate_icecream", -3, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:icecream_coconut", -2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:icecream_cactus", -2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:icecream_chocolate", -2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:icecream_sweetberries", -2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:icecream_melon", -2, 2400);

		JsonConfig.registerConsumableThirst("beachparty:coconut_open", 3, 0.0f);

		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:coconut_cocktail", -1, 1800);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:sweetberries_cocktail", -1, 1800);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:cocoa_cocktail", -2, 1800);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:pumpkin_cocktail", -1, 1800);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:melon_cocktail", -1, 1800);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:honey_cocktail", -1, 1800);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:refreshing_drink", -1, 1800);
		JsonConfig.registerConsumableThirst("beachparty:coconut_cocktail", 5, 3.0f);
		JsonConfig.registerConsumableThirst("beachparty:sweetberries_cocktail", 6, 3.0f);
		JsonConfig.registerConsumableThirst("beachparty:cocoa_cocktail", 8, 4.0f);
		JsonConfig.registerConsumableThirst("beachparty:pumpkin_cocktail", 8, 4.0f);
		JsonConfig.registerConsumableThirst("beachparty:melon_cocktail", 9, 5.0f);
		JsonConfig.registerConsumableThirst("beachparty:honey_cocktail", 12, 7.0f);
		JsonConfig.registerConsumableThirst("beachparty:refreshing_drink", 9, 3.0f);

		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:sweetberry_milkshake", -3, 3000);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:coconut_milkshake", -3, 3000);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:chocolate_milkshake", -3, 3000);
		JsonConfig.registerConsumableThirst("beachparty:sweetberry_milkshake", 8, 5.0f);
		JsonConfig.registerConsumableThirst("beachparty:coconut_milkshake", 7, 4.0f);
		JsonConfig.registerConsumableThirst("beachparty:chocolate_milkshake", 10, 6.0f);

		JsonConfig.registerItemTemperature("beachparty:beach_hat", -2.5f);
		JsonConfig.registerItemTemperature("beachparty:trunks", -3.0f);
		JsonConfig.registerItemTemperature("beachparty:bikine", -3.0f);
		JsonConfig.registerItemTemperature("beachparty:crocs", -0.5f);
	}
}
