package sfiomn.legendarysurvivaloverhaul.common.integration;

import net.minecraftforge.fml.ModList;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemporaryModifierGroupEnum;
import sfiomn.legendarysurvivaloverhaul.api.config.json.JsonPropertyValue;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;

import java.util.Arrays;

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
		if (mods.isLoaded("betterend"))
			initBetterEnd();
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
		if (mods.isLoaded("beachparty"))
			initBeachParty();
		if (mods.isLoaded("create_confectionery"))
			initCreateConfectionery();
		if (mods.isLoaded("wardrobe"))
			initWardrobe();
		if (mods.isLoaded("iceandfire"))
			initIceAndFire();
		if (mods.isLoaded("alexsmobs"))
			initAlexsMobs();
		if (mods.isLoaded("aquamirae"))
			initAquamirae();
		if (mods.isLoaded("call_of_yucutan"))
			initCallOfYucutan();
		if (mods.isLoaded("cataclysm"))
			initCataclysm();
		if (mods.isLoaded("endermanoverhaul"))
			initEndermanOverhaul();
		if (mods.isLoaded("irons_spellbooks"))
			initIronsSpellbooks();
		if (mods.isLoaded("born_in_chaos_v1"))
			initBornInChaos();
	}

	private static void initCreate()
	{
		JsonConfig.registerBlockFluidTemperature("create:blaze_burner", 2.5f, new JsonPropertyValue("blaze", "smouldering"));
		JsonConfig.registerBlockFluidTemperature("create:blaze_burner", 5.0f, new JsonPropertyValue("blaze", "kindled"));
		JsonConfig.registerBlockFluidTemperature("create:blaze_burner", 7.5f, new JsonPropertyValue("blaze", "seething"));
	}

	private static void initFarmersDelight()
	{
		JsonConfig.registerBlockFluidTemperature("farmersdelight:stove", 7.5f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockFluidTemperature("farmersdelight:stove", 0.0f, new JsonPropertyValue("lit", "false"));

		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:beef_stew", 2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:chicken_soup", 2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:vegetable_soup", 2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:fish_stew", 2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:pumpkin_soup", 2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "farmersdelight:baked_cod_stew", 2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "farmersdelight:hot_cocoa", 3, 3600);

		JsonConfig.registerConsumableThirst("farmersdelight:chicken_soup", 4, 2.0f);
		JsonConfig.registerConsumableThirst("farmersdelight:vegetable_soup", 4, 2.0f);
		JsonConfig.registerConsumableThirst("farmersdelight:pumpkin_soup", 4, 2.0f);
		JsonConfig.registerConsumableThirst("farmersdelight:hot_cocoa", 4, 1.0f);
	}

	private static void initRealisticTorches()
	{
		JsonConfig.registerBlockFluidTemperature("realistictorches:torch", 1.5f, new JsonPropertyValue("litstate", "2"));
		JsonConfig.registerBlockFluidTemperature("realistictorches:torch", 0.75f, new JsonPropertyValue("litstate", "1"));
		JsonConfig.registerBlockFluidTemperature("realistictorches:torch", 0.0f, new JsonPropertyValue("litstate", "0"));

		JsonConfig.registerBlockFluidTemperature("realistictorches:torch_wall", 1.5f, new JsonPropertyValue("litstate", "2"));
		JsonConfig.registerBlockFluidTemperature("realistictorches:torch_wall", 0.75f, new JsonPropertyValue("litstate", "1"));
		JsonConfig.registerBlockFluidTemperature("realistictorches:torch_wall", 0.0f, new JsonPropertyValue("litstate", "0"));
	}

	private static void initBYG()
	{
		JsonConfig.registerBlockFluidTemperature("byg:boric_fire", 5.0f);
		JsonConfig.registerBlockFluidTemperature("byg:boric_campfire", 10.0f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockFluidTemperature("byg:boric_campfire", 0.0f, new JsonPropertyValue("lit", "false"));

		JsonConfig.registerBlockFluidTemperature("byg:cryptic_fire", 5.0f);
		JsonConfig.registerBlockFluidTemperature("byg:cryptic_campfire", 7.5f, new JsonPropertyValue("lit", "true"));
		JsonConfig.registerBlockFluidTemperature("byg:cryptic_campfire", 0.0f, new JsonPropertyValue("lit", "false"));
	}

	private static void initArtifacts()
	{
		JsonConfig.registerItemTemperature("artifacts:villager_hat", -2.5f);
		JsonConfig.registerItemTemperature("artifacts:lucky_scarf", 2.5f);
		JsonConfig.registerItemTemperature("artifacts:scarf_of_invisibility", 2.5f);
	}

	private static void initEndergeticExpansion()
	{
		JsonConfig.registerBlockFluidTemperature("endergetic:ender_fire", -7.0f);
		JsonConfig.registerBlockFluidTemperature("endergetic:ender_campfire", -10.0f);
		JsonConfig.registerBlockFluidTemperature("endergetic:ender_torch", -1.5f);
		JsonConfig.registerBlockFluidTemperature("endergetic:ender_wall_torch", -1.5f);
	}

	private static void initInfernalExpansion()
	{
		JsonConfig.registerBlockFluidTemperature("infernalexp:fire_glow", 7.0f);
		JsonConfig.registerBlockFluidTemperature("infernalexp:campfire_glow", 10.0f);
		JsonConfig.registerBlockFluidTemperature("infernalexp:torch_glow", 1.5f);
		JsonConfig.registerBlockFluidTemperature("infernalexp:torch_glow_wall", 1.5f);
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

	private static void initBetterEnd() {
		JsonConfig.registerFuelItems("betterend:charcoal_block", ThermalTypeEnum.HEATING, 270);

		JsonConfig.registerItemTemperature("betterend:aeternium_helmet", -2.0f);
		JsonConfig.registerItemTemperature("betterend:aeternium_chestplate", -3.0f);
		JsonConfig.registerItemTemperature("betterend:aeternium_leggings", -2.5f);
		JsonConfig.registerItemTemperature("betterend:aeternium_boots", -2.0f);
		JsonConfig.registerItemTemperature("betterend:crystalite_helmet", -1.5f);
		JsonConfig.registerItemTemperature("betterend:crystalite_chestplate", -2.0f);
		JsonConfig.registerItemTemperature("betterend:crystalite_leggings", -2.0f);
		JsonConfig.registerItemTemperature("betterend:crystalite_boots", -1.0f);
		JsonConfig.registerItemTemperature("betterend:terminite_helmet", -1.0f);
		JsonConfig.registerItemTemperature("betterend:terminite_chestplate", -1.5f);
		JsonConfig.registerItemTemperature("betterend:terminite_leggings", -1.0f);
		JsonConfig.registerItemTemperature("betterend:terminite_boots", -1.0f);
		JsonConfig.registerItemTemperature("betterend:thallasium_helmet", -0.5f);
		JsonConfig.registerItemTemperature("betterend:thallasium_chestplate", -1.0f);
		JsonConfig.registerItemTemperature("betterend:thallasium_leggings", -0.5f);
		JsonConfig.registerItemTemperature("betterend:thallasium_boots", -0.5f);
		JsonConfig.registerItemTemperature("betternether:cincinnasite_helmet", -1.0f);
		JsonConfig.registerItemTemperature("betternether:cincinnasite_chestplate", -2.0f);
		JsonConfig.registerItemTemperature("betternether:cincinnasite_leggings", -2.0f);
		JsonConfig.registerItemTemperature("betternether:cincinnasite_boots", -1.0f);
		JsonConfig.registerItemTemperature("betternether:nether_ruby_helmet", 1.0f);
		JsonConfig.registerItemTemperature("betternether:nether_ruby_chestplate", 2.0f);
		JsonConfig.registerItemTemperature("betternether:nether_ruby_leggings", 2.0f);
		JsonConfig.registerItemTemperature("betternether:nether_ruby_boots", 1.0f);
		JsonConfig.registerItemTemperature("betternether:flaming_ruby_helmet", 1.5f);
		JsonConfig.registerItemTemperature("betternether:flaming_ruby_chestplate", 2.5f);
		JsonConfig.registerItemTemperature("betternether:flaming_ruby_leggings", 2.5f);
		JsonConfig.registerItemTemperature("betternether:flaming_ruby_boots", 1.5f);
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
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "crockpot:asparagus_soup", 2, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "crockpot:iced_tea", -2, 4800);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:ice_cream", -3, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "crockpot:tea", 3, 4800);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:bacon_eggs", 1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:bone_stew", 3, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:bunny_stew", 1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:bone_soup", 2, 1800);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:california_roll", -1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:flower_salad", -3, 6000);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "crockpot:fruit_medley", -1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "crockpot:gazpacho", -2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:hot_chili", 2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "crockpot:hot_cocoa", 3, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:kabobs", 1, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:meat_balls", 1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:moqueca", 1, 1800);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:plain_omelette", 1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:pepper_popper", 3, 6000);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:pierogi", 2, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:ratatouille", 3, 6000);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:salmon_sushi", -1, 1800);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:salsa", -2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:turkey_dinner", 2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "crockpot:veg_stinger", -1, 1200);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "crockpot:watermelon_icle", -4, 1800);

		JsonConfig.registerConsumableThirst("crockpot:asparagus_soup", 4, 2.0f);
		JsonConfig.registerConsumableThirst("crockpot:bone_soup", 4, 2.0f);
		JsonConfig.registerConsumableThirst("crockpot:iced_tea", 8, 5.0f);
		JsonConfig.registerConsumableThirst("crockpot:tea", 5, 2.0f);
		JsonConfig.registerConsumableThirst("crockpot:gazpacho", 4, 2.0f);
		JsonConfig.registerConsumableThirst("crockpot:hot_cocoa", 2, 1.0f);
		JsonConfig.registerConsumableThirst("crockpot:fruit_medley", 3, 1.0f);
		JsonConfig.registerConsumableThirst("crockpot:veg_stinger", 5, 2.0f);
	}

	private static void initQuark() {
		JsonConfig.registerFuelItems("quark:coal_block", ThermalTypeEnum.HEATING, 270);
		JsonConfig.registerFuelItems("quark:charcoal_block", ThermalTypeEnum.HEATING, 270);
	}

	private static void initBeachParty() {
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:sweetberry_icecream", -2, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:coconut_icecream", -2, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:chocolate_icecream", -3, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:icecream_coconut", -2, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:icecream_cactus", -2, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:icecream_chocolate", -2, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:icecream_sweetberries", -2, 3600);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.FOOD, "beachparty:icecream_melon", -2, 3600);

		JsonConfig.registerConsumableThirst("beachparty:coconut_open", 3, 0.0f);

		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:coconut_cocktail", -1, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:sweetberries_cocktail", -1, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:cocoa_cocktail", -2, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:pumpkin_cocktail", -1, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:melon_cocktail", -1, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:honey_cocktail", -1, 2400);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:refreshing_drink", -1, 2400);
		JsonConfig.registerConsumableThirst("beachparty:coconut_cocktail", 4, 2.0f);
		JsonConfig.registerConsumableThirst("beachparty:sweetberries_cocktail", 4, 2.0f);
		JsonConfig.registerConsumableThirst("beachparty:cocoa_cocktail", 5, 2.0f);
		JsonConfig.registerConsumableThirst("beachparty:pumpkin_cocktail", 5, 2.0f);
		JsonConfig.registerConsumableThirst("beachparty:melon_cocktail", 5, 2.0f);
		JsonConfig.registerConsumableThirst("beachparty:honey_cocktail", 7, 3.0f);
		JsonConfig.registerConsumableThirst("beachparty:refreshing_drink", 7, 3.0f);

		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:sweetberry_milkshake", -3, 4800);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:coconut_milkshake", -3, 4800);
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroupEnum.DRINK, "beachparty:chocolate_milkshake", -3, 4800);
		JsonConfig.registerConsumableThirst("beachparty:sweetberry_milkshake", 4, 2.0f);
		JsonConfig.registerConsumableThirst("beachparty:coconut_milkshake", 5, 3.0f);
		JsonConfig.registerConsumableThirst("beachparty:chocolate_milkshake", 7, 4.0f);

		JsonConfig.registerItemTemperature("beachparty:beach_hat", -2.5f);
		JsonConfig.registerItemTemperature("beachparty:trunks", -3.0f);
		JsonConfig.registerItemTemperature("beachparty:bikine", -3.0f);
		JsonConfig.registerItemTemperature("beachparty:crocs", -0.5f);
	}

	private static void initCreateConfectionery() {
		JsonConfig.registerConsumableThirst("create_confectionery:hot_chocolate_bottle", 3, 2.0f);
	}

	private static void initWardrobe() {
		JsonConfig.registerItemTemperature("wardrobe:taiga_helmet", 2.0f);
		JsonConfig.registerItemTemperature("wardrobe:taiga_chestplate", 3.5f);
		JsonConfig.registerItemTemperature("wardrobe:taiga_leggings", 3.0f);
		JsonConfig.registerItemTemperature("wardrobe:taiga_boots", 1.0f);
		JsonConfig.registerItemTemperature("wardrobe:snowy_helmet", 2.0f);
		JsonConfig.registerItemTemperature("wardrobe:snowy_chestplate", 3.5f);
		JsonConfig.registerItemTemperature("wardrobe:snowy_leggings", 3.0f);
		JsonConfig.registerItemTemperature("wardrobe:snowy_boots", 1.0f);
		JsonConfig.registerItemTemperature("wardrobe:desert_helmet", -2.0f);
		JsonConfig.registerItemTemperature("wardrobe:desert_chestplate", -3.0f);
		JsonConfig.registerItemTemperature("wardrobe:desert_leggings", -2.0f);
		JsonConfig.registerItemTemperature("wardrobe:desert_boots", -1.0f);
		JsonConfig.registerItemTemperature("wardrobe:jungle_chestplate", -5.0f);
		JsonConfig.registerItemTemperature("wardrobe:jungle_leggings", -3.0f);
		JsonConfig.registerItemTemperature("wardrobe:jungle_boots", -2.0f);
		JsonConfig.registerItemTemperature("wardrobe:savanna_chestplate", -4.0f);
		JsonConfig.registerItemTemperature("wardrobe:savanna_leggings", -2.5f);
		JsonConfig.registerItemTemperature("wardrobe:savanna_boots", -1.5f);
		JsonConfig.registerItemTemperature("wardrobe:wool_vest_chestplate", 2.0f);
		JsonConfig.registerItemTemperature("wardrobe:chiton", -2.0f);
		JsonConfig.registerItemTemperature("wardrobe:farmers_hat_helmet", -3.5f);
	}

	private static void initIceAndFire() {
		JsonConfig.registerEntityTemperature("iceandfire:hippocampus", 3.0f);

		JsonConfig.registerItemTemperature("iceandfire:armor_silver_metal_helmet", -0.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_silver_metal_chestplate", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_silver_metal_leggings", -1.0f);
		JsonConfig.registerItemTemperature("iceandfire:armor_silver_metal_boots", -1.0f);
		JsonConfig.registerItemTemperature("iceandfire:armor_copper_metal_helmet", -0.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_copper_metal_chestplate", -0.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_copper_metal_leggings", -0.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_copper_metal_boots", -0.5f);
		JsonConfig.registerItemTemperature("iceandfire:sheep_helmet", 2.0f);
		JsonConfig.registerItemTemperature("iceandfire:sheep_chestplate", 3.5f);
		JsonConfig.registerItemTemperature("iceandfire:sheep_leggings", 3.0f);
		JsonConfig.registerItemTemperature("iceandfire:sheep_boots", 1.0f);
		JsonConfig.registerItemTemperature("iceandfire:deathworm_yellow_helmet", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:deathworm_yellow_chestplate", -2.5f);
		JsonConfig.registerItemTemperature("iceandfire:deathworm_yellow_leggings", -2.0f);
		JsonConfig.registerItemTemperature("iceandfire:deathworm_yellow_boots", -1.0f);
		JsonConfig.registerItemTemperature("iceandfire:deathworm_white_helmet", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:deathworm_white_chestplate", -2.5f);
		JsonConfig.registerItemTemperature("iceandfire:deathworm_white_leggings", -2.0f);
		JsonConfig.registerItemTemperature("iceandfire:deathworm_white_boots", -1.0f);
		JsonConfig.registerItemTemperature("iceandfire:deathworm_red_helmet", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:deathworm_red_chestplate", -2.5f);
		JsonConfig.registerItemTemperature("iceandfire:deathworm_red_leggings", -2.0f);
		JsonConfig.registerItemTemperature("iceandfire:deathworm_red_boots", -1.0f);
		JsonConfig.registerItemTemperature("iceandfire:armor_red_helmet", 3.0f);
		JsonConfig.registerItemTemperature("iceandfire:armor_red_chestplate", 5.0f);
		JsonConfig.registerItemTemperature("iceandfire:armor_red_leggings", 4.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_red_boots", 2.5f);
		JsonConfig.registerItemTemperature("iceandfire:dragonsteel_ice_helmet", -3.0f);
		JsonConfig.registerItemTemperature("iceandfire:dragonsteel_ice_chestplate", -5.0f);
		JsonConfig.registerItemTemperature("iceandfire:dragonsteel_ice_leggings", -4.5f);
		JsonConfig.registerItemTemperature("iceandfire:dragonsteel_ice_boots", -2.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_bronze_helmet", 1.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_bronze_chestplate", 3.0f);
		JsonConfig.registerItemTemperature("iceandfire:armor_bronze_leggings", 2.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_bronze_boots", 1.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_green_helmet", 1.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_green_chestplate", 3.0f);
		JsonConfig.registerItemTemperature("iceandfire:armor_green_leggings", 2.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_green_boots", 1.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_gray_helmet", 1.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_gray_chestplate", 3.0f);
		JsonConfig.registerItemTemperature("iceandfire:armor_gray_leggings", 2.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_gray_boots", 1.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_blue_helmet", 1.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_blue_chestplate", 3.0f);
		JsonConfig.registerItemTemperature("iceandfire:armor_blue_leggings", 2.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_blue_boots", 1.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_silver_helmet", 1.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_silver_chestplate", 3.0f);
		JsonConfig.registerItemTemperature("iceandfire:armor_silver_leggings", 2.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_silver_boots", 1.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_amythest_helmet", 1.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_amythest_chestplate", 3.0f);
		JsonConfig.registerItemTemperature("iceandfire:armor_amythest_leggings", 2.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_amythest_boots", 1.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_copper_helmet", 1.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_copper_chestplate", 3.0f);
		JsonConfig.registerItemTemperature("iceandfire:armor_copper_leggings", 2.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_copper_boots", 1.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_black_helmet", 1.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_black_chestplate", 3.0f);
		JsonConfig.registerItemTemperature("iceandfire:armor_black_leggings", 2.5f);
		JsonConfig.registerItemTemperature("iceandfire:armor_black_boots", 1.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_red_helmet", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_red_chestplate", -3.0f);
		JsonConfig.registerItemTemperature("iceandfire:tide_red_leggings", -2.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_red_boots", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_blue_helmet", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_blue_chestplate", -3.0f);
		JsonConfig.registerItemTemperature("iceandfire:tide_blue_leggings", -2.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_blue_boots", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_bronze_helmet", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_bronze_chestplate", -3.0f);
		JsonConfig.registerItemTemperature("iceandfire:tide_bronze_leggings", -2.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_bronze_boots", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_green_helmet", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_green_chestplate", -3.0f);
		JsonConfig.registerItemTemperature("iceandfire:tide_green_leggings", -2.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_green_boots", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_deepblue_helmet", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_deepblue_chestplate", -3.0f);
		JsonConfig.registerItemTemperature("iceandfire:tide_deepblue_leggings", -2.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_deepblue_boots", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_purple_helmet", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_purple_chestplate", -3.0f);
		JsonConfig.registerItemTemperature("iceandfire:tide_purple_leggings", -2.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_purple_boots", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_teal_helmet", -1.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_teal_chestplate", -3.0f);
		JsonConfig.registerItemTemperature("iceandfire:tide_teal_leggings", -2.5f);
		JsonConfig.registerItemTemperature("iceandfire:tide_teal_boots", -1.5f);
	}

	private static void initAlexsMobs() {
		JsonConfig.registerItemTemperature("alexsmobs:roadrunner_boots", -1.0f);
		JsonConfig.registerItemTemperature("alexsmobs:crocodile_chestplate", 1.0f);
		JsonConfig.registerItemTemperature("alexsmobs:centipede_leggings", -1.0f);
		JsonConfig.registerItemTemperature("alexsmobs:frontier_cap", 4.0f);
		JsonConfig.registerItemTemperature("alexsmobs:sombrero", -4.0f);
		JsonConfig.registerItemTemperature("alexsmobs:emu_leggings", -1.5f);
		JsonConfig.registerItemTemperature("alexsmobs:froststalker_helmet", -3.0f);
		JsonConfig.registerItemTemperature("alexsmobs:unsettling_kimono", -2.0f);
	}

	private static void initAquamirae() {
		JsonConfig.registerItemTemperature("aquamirae:terrible_helmet", -1.0f);
		JsonConfig.registerItemTemperature("aquamirae:terrible_chestplate", -1.5f);
		JsonConfig.registerItemTemperature("aquamirae:terrible_leggings", -1.5f);
		JsonConfig.registerItemTemperature("aquamirae:terrible_boots", -1.0f);
		JsonConfig.registerItemTemperature("aquamirae:abyssal_heaume", 1.0f);
		JsonConfig.registerItemTemperature("aquamirae:abyssal_brigantine", 2.0f);
		JsonConfig.registerItemTemperature("aquamirae:abyssal_leggings", 1.0f);
		JsonConfig.registerItemTemperature("aquamirae:abyssal_boots", 1.0f);
		JsonConfig.registerItemTemperature("aquamirae:three_bolt_helmet", 1.0f);
		JsonConfig.registerItemTemperature("aquamirae:three_bolt_suit", 1.0f);
		JsonConfig.registerItemTemperature("aquamirae:three_bolt_leggings", 1.0f);
		JsonConfig.registerItemTemperature("aquamirae:three_bolt_boots", 1.0f);
	}

	private static void initCallOfYucutan() {
		JsonConfig.registerItemTemperature("call_of_yucutan:jades_helmet", -0.5f);
		JsonConfig.registerItemTemperature("call_of_yucutan:jades_chestplate", -1.5f);
		JsonConfig.registerItemTemperature("call_of_yucutan:jades_leggings", -1.5f);
		JsonConfig.registerItemTemperature("call_of_yucutan:jades_boots", -0.5f);
	}

	private static void initCataclysm() {
		JsonConfig.registerItemTemperature("cataclysm:bone_reptile_helmet", 1.5f);
		JsonConfig.registerItemTemperature("cataclysm:bone_reptile_chestplate", 2.0f);
		JsonConfig.registerItemTemperature("cataclysm:ignitium_helmet", 1.5f);
		JsonConfig.registerItemTemperature("cataclysm:ignitium_chestplate", 3.0f);
		JsonConfig.registerItemTemperature("cataclysm:ignitium_elytra_chestplate", 3.0f);
		JsonConfig.registerItemTemperature("cataclysm:ignitium_leggings", 2.0f);
		JsonConfig.registerItemTemperature("cataclysm:ignitium_boots", 1.5f);
		JsonConfig.registerItemTemperature("cataclysm:monstrous_helm", 1.0f);
		JsonConfig.registerItemTemperature("cataclysm:bloom_stone_pauldrons", -1.0f);
	}

	private static void initEndermanOverhaul() {
		JsonConfig.registerItemTemperature("endermanoverhaul:badlands_hood", 2.5f);
		JsonConfig.registerItemTemperature("endermanoverhaul:savanna_hood", 2.5f);
		JsonConfig.registerItemTemperature("endermanoverhaul:snowy_hood", 2.5f);
	}

	private static void initIronsSpellbooks() {
		JsonConfig.registerItemTemperature("irons_spellbooks:pyromancer_helmet", 1.5f);
		JsonConfig.registerItemTemperature("irons_spellbooks:pyromancer_chestplate", 2.5f);
		JsonConfig.registerItemTemperature("irons_spellbooks:pyromancer_leggings", 2.0f);
		JsonConfig.registerItemTemperature("irons_spellbooks:pyromancer_boots", 1.5f);
		JsonConfig.registerItemTemperature("irons_spellbooks:cryomancer_helmet", -1.5f);
		JsonConfig.registerItemTemperature("irons_spellbooks:cryomancer_chestplate", -2.5f);
		JsonConfig.registerItemTemperature("irons_spellbooks:cryomancer_leggings", -2.0f);
		JsonConfig.registerItemTemperature("irons_spellbooks:cryomancer_boots", -1.5f);
		JsonConfig.registerItemTemperature("irons_spellbooks:netherite_mage_helmet", -2.0f);
		JsonConfig.registerItemTemperature("irons_spellbooks:netherite_mage_chestplate", -3.5f);
		JsonConfig.registerItemTemperature("irons_spellbooks:netherite_mage_leggings", -2.5f);
		JsonConfig.registerItemTemperature("irons_spellbooks:netherite_mage_boots", -2.0f);
	}

	private static void initBornInChaos() {
		JsonConfig.registerItemTemperature("born_in_chaos_v1:dark_metal_armor_helmet", -1.0f);
		JsonConfig.registerItemTemperature("born_in_chaos_v1:dark_metal_armor_chestplate", -2.0f);
		JsonConfig.registerItemTemperature("born_in_chaos_v1:dark_metal_armor_leggings", -1.0f);
		JsonConfig.registerItemTemperature("born_in_chaos_v1:dark_metal_armor_boots", -0.5f);
		JsonConfig.registerItemTemperature("born_in_chaos_v1:spiritual_guide_sombrero_helmet", -3.5f);
		JsonConfig.registerItemTemperature("born_in_chaos_v1:nightmare_mantleofthe_night_helmet", 1.0f);
		JsonConfig.registerItemTemperature("born_in_chaos_v1:nightmare_mantleofthe_night_chestplate", 2.0f);
		JsonConfig.registerItemTemperature("born_in_chaos_v1:nightmare_mantleofthe_night_leggings", 1.0f);
		JsonConfig.registerItemTemperature("born_in_chaos_v1:nightmare_mantleofthe_night_boots", 0.5f);
	}
}
