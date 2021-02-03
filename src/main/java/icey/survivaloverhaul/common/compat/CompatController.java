package icey.survivaloverhaul.common.compat;

import icey.survivaloverhaul.api.config.json.JsonItemIdentity;
import icey.survivaloverhaul.api.config.json.TemporaryModifierGroup;
import icey.survivaloverhaul.api.config.json.temperature.JsonPropertyValue;
import icey.survivaloverhaul.config.json.JsonConfig;
import net.minecraftforge.fml.ModList;

/**
 * This class is specifically for implementing default configuration values
 * for mods that don't require us to set up new modifiers; i.e. simply setting
 * relevant values for armor/block temperatures, consumable temperatures, etc.
 * @author Icey
 */
public final class CompatController
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
		
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroup.FOOD.group(), "farmersdelight:beef_stew", 1.5f, 2400, new JsonItemIdentity(null));
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroup.FOOD.group(), "farmersdelight:chicken_soup", 1.5f, 2400, new JsonItemIdentity(null));
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroup.FOOD.group(), "farmersdelight:vegetable_soup", 1.5f, 2400, new JsonItemIdentity(null));
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroup.FOOD.group(), "farmersdelight:fish_stew", 1.5f, 2400, new JsonItemIdentity(null));
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroup.FOOD.group(), "farmersdelight:pumpkin_soup", 1.5f, 2400, new JsonItemIdentity(null));
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroup.FOOD.group(), "farmersdelight:baked_cod_stew", 1.5f, 2400, new JsonItemIdentity(null));
		JsonConfig.registerConsumableTemperature(TemporaryModifierGroup.DRINK.group(), "farmersdelight:hot_cocoa", 3.5f, 3600, new JsonItemIdentity(null));
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
		JsonConfig.registerArmorTemperature("artifacts:villager_hat", -2.5f);
		JsonConfig.registerArmorTemperature("artifacts:lucky_scarf", 2.5f);
		JsonConfig.registerArmorTemperature("artifacts:scarf_of_invisibility", 2.5f);
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
		JsonConfig.registerBiomeOverride("biomesoplenty:volcano", 1.5f, false, true);
		JsonConfig.registerBiomeOverride("biomesoplenty:volcanic_plains", 1.5f, false, true);
		
		JsonConfig.registerBiomeOverride("biomesoplenty:crystalline_chasm", 0.8f, false, false);
		JsonConfig.registerBiomeOverride("biomesoplenty:undergrowth", 0.75f, false, false);
		JsonConfig.registerBiomeOverride("biomesoplenty:visceral_heap", 0.9f, false, false);
		JsonConfig.registerBiomeOverride("biomesoplenty:withered_abyss", 1.5f, false, true);
	}
}
