package sfiomn.legendarysurvivaloverhaul.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.client.render.TemperatureDisplayEnum;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessMode;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfigRegistration;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Config
{
	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Config.Common COMMON;
	
	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Config.Client CLIENT;
	
	static
	{
		final Pair<Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(Config.Common::new);
		COMMON_SPEC = common.getRight();
		COMMON = common.getLeft();
		
		final Pair<Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(Config.Client::new);
		CLIENT_SPEC = client.getRight();
		CLIENT = client.getLeft();
	}
	
	public static void register()
	{
		try
		{
			Files.createDirectory(LegendarySurvivalOverhaul.modConfigPath);
			Files.createDirectory(LegendarySurvivalOverhaul.modConfigJsons);
		}
		catch (FileAlreadyExistsException ignored) {}
		catch (IOException e)
		{
			LegendarySurvivalOverhaul.LOGGER.error("Failed to create Legendary Survival Overhaul config directories");
			e.printStackTrace();
		}
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC, "legendarysurvivaloverhaul/legendarysurvivaloverhaul-client.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC, "legendarysurvivaloverhaul/legendarysurvivaloverhaul-common.toml");
		
		JsonConfigRegistration.init(LegendarySurvivalOverhaul.modConfigJsons.toFile());
	}
	
	public static class Common
	{
		// Core/Advanced
		// public final ForgeConfigSpec.ConfigValue<Boolean> forceDisableFlightKick;
		public final ForgeConfigSpec.ConfigValue<Integer> routinePacketSync;
		public final ForgeConfigSpec.ConfigValue<Boolean> hideInfoFromDebug;
		public final ForgeConfigSpec.ConfigValue<Double> baseFoodExhaustion;
		
		// Temperature
		public final ForgeConfigSpec.ConfigValue<Boolean> temperatureEnabled;
		public final ForgeConfigSpec.ConfigValue<Integer> tickRate;
		public final ForgeConfigSpec.ConfigValue<Double> minTemperatureModification;
		public final ForgeConfigSpec.ConfigValue<Double> maxTemperatureModification;
		public final ForgeConfigSpec.ConfigValue<Boolean> showPotionEffectParticles;
		public final ForgeConfigSpec.ConfigValue<Boolean> dangerousTemperature;
		public final ForgeConfigSpec.ConfigValue<Boolean> temperatureResistanceOnDeathEnabled;
		public final ForgeConfigSpec.ConfigValue<Integer> temperatureResistanceOnDeathTime;

		public final ForgeConfigSpec.ConfigValue<Boolean> temperatureSecondaryEffects;
		public final ForgeConfigSpec.ConfigValue<Double> heatThirstEffectModifier;
		public final ForgeConfigSpec.ConfigValue<Double> coldHungerEffectModifier;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> biomeEffectsEnabled;
		public final ForgeConfigSpec.ConfigValue<Boolean> biomeDrynessEffectEnabled;
		public final ForgeConfigSpec.ConfigValue<Double> biomeTemperatureMultiplier;

		public final ForgeConfigSpec.ConfigValue<Double> overworldDefaultTemperature;
		public final ForgeConfigSpec.ConfigValue<Double> netherDefaultTemperature;
		public final ForgeConfigSpec.ConfigValue<Double> endDefaultTemperature;
		
		public final ForgeConfigSpec.ConfigValue<Double> timeModifier;
		public final ForgeConfigSpec.ConfigValue<Double> biomeTimeMultiplier;
		public final ForgeConfigSpec.ConfigValue<Integer> shadeTimeModifier;

		public final ForgeConfigSpec.ConfigValue<Double> altitudeModifier;
		public final ForgeConfigSpec.ConfigValue<Double> sprintModifier;
		public final ForgeConfigSpec.ConfigValue<Double> onFireModifier;
		public final ForgeConfigSpec.ConfigValue<Double> enchantmentMultiplier;
		
		public final ForgeConfigSpec.ConfigValue<String> wetnessMode;
		public final ForgeConfigSpec.ConfigValue<Double> wetMultiplier;
		public final ForgeConfigSpec.ConfigValue<Integer> wetnessDecrease;
		public final ForgeConfigSpec.ConfigValue<Integer> wetnessRainIncrease;
		public final ForgeConfigSpec.ConfigValue<Integer> wetnessFluidIncrease;

		public final ForgeConfigSpec.ConfigValue<Integer> tempInfluenceMaximumDist;
		public final ForgeConfigSpec.ConfigValue<Double> tempInfluenceUpDistMultiplier;
		public final ForgeConfigSpec.ConfigValue<Double> tempInfluenceOutsideDistMultiplier;

		public final ForgeConfigSpec.ConfigValue<Double> rainTemperatureModifier;
		public final ForgeConfigSpec.ConfigValue<Double> snowTemperatureModifier;

		public final ForgeConfigSpec.ConfigValue<Double> playerHuddlingModifier;
		public final ForgeConfigSpec.ConfigValue<Integer> playerHuddlingRadius;

		public final ForgeConfigSpec.ConfigValue<Double> heatingCoat1Modifier;
		public final ForgeConfigSpec.ConfigValue<Double> heatingCoat2Modifier;
		public final ForgeConfigSpec.ConfigValue<Double> heatingCoat3Modifier;

		public final ForgeConfigSpec.ConfigValue<Double> coolingCoat1Modifier;
		public final ForgeConfigSpec.ConfigValue<Double> coolingCoat2Modifier;
		public final ForgeConfigSpec.ConfigValue<Double> coolingCoat3Modifier;

		public final ForgeConfigSpec.ConfigValue<Double> thermalCoat1Modifier;
		public final ForgeConfigSpec.ConfigValue<Double> thermalCoat2Modifier;
		public final ForgeConfigSpec.ConfigValue<Double> thermalCoat3Modifier;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> seasonTemperatureEffects;
		public final ForgeConfigSpec.ConfigValue<Boolean> tropicalSeasonsEnabled;
		public final ForgeConfigSpec.ConfigValue<Boolean> seasonCardsEnabled;
		
		public final ForgeConfigSpec.ConfigValue<Integer> earlySpringModifier;
		public final ForgeConfigSpec.ConfigValue<Integer> midSpringModifier;
		public final ForgeConfigSpec.ConfigValue<Integer> lateSpringModifier;
		
		public final ForgeConfigSpec.ConfigValue<Integer> earlySummerModifier;
		public final ForgeConfigSpec.ConfigValue<Integer> midSummerModifier;
		public final ForgeConfigSpec.ConfigValue<Integer> lateSummerModifier;
		
		public final ForgeConfigSpec.ConfigValue<Integer> earlyAutumnModifier;
		public final ForgeConfigSpec.ConfigValue<Integer> midAutumnModifier;
		public final ForgeConfigSpec.ConfigValue<Integer> lateAutumnModifier;
		
		public final ForgeConfigSpec.ConfigValue<Integer> earlyWinterModifier;
		public final ForgeConfigSpec.ConfigValue<Integer> midWinterModifier;
		public final ForgeConfigSpec.ConfigValue<Integer> lateWinterModifier;
		
		public final ForgeConfigSpec.ConfigValue<Integer> earlyWetSeasonModifier;
		public final ForgeConfigSpec.ConfigValue<Integer> midWetSeasonModifier;
		public final ForgeConfigSpec.ConfigValue<Integer> lateWetSeasonModifier;
		
		public final ForgeConfigSpec.ConfigValue<Integer> earlyDrySeasonModifier;
		public final ForgeConfigSpec.ConfigValue<Integer> midDrySeasonModifier;
		public final ForgeConfigSpec.ConfigValue<Integer> lateDrySeasonModifier;

		public final ForgeConfigSpec.ConfigValue<List<String>> sunFernBiomeNames;
		public final ForgeConfigSpec.ConfigValue<List<String>> sunFernBiomeCategories;
		public final ForgeConfigSpec.ConfigValue<List<String>> iceFernBiomeNames;
		public final ForgeConfigSpec.ConfigValue<List<String>> iceFernBiomeCategories;
		public final ForgeConfigSpec.ConfigValue<List<String>> waterPlantBiomeNames;
		public final ForgeConfigSpec.ConfigValue<List<String>> waterPlantBiomeCategories;

		// Thirst
		public final ForgeConfigSpec.ConfigValue<Boolean> thirstEnabled;
		public final ForgeConfigSpec.ConfigValue<Boolean> dangerousDehydration;
		public final ForgeConfigSpec.ConfigValue<Double> dehydrationDamageScaling;
		public final ForgeConfigSpec.ConfigValue<Double> thirstEffectModifier;
		public final ForgeConfigSpec.ConfigValue<Double> baseThirstExhaustion;
		public final ForgeConfigSpec.ConfigValue<Double> sprintingThirstExhaustion;
		public final ForgeConfigSpec.ConfigValue<Double> onJumpThirstExhaustion;
		public final ForgeConfigSpec.ConfigValue<Double> onBlockBreakThirstExhaustion;
		public final ForgeConfigSpec.ConfigValue<Double> onAttackThirstExhaustion;
		public final ForgeConfigSpec.ConfigValue<Integer> canteenCapacity;
		public final ForgeConfigSpec.ConfigValue<Integer> largeCanteenCapacity;
		public final ForgeConfigSpec.ConfigValue<Boolean> allowOverridePurifiedWater;
		public final ForgeConfigSpec.ConfigValue<Boolean> drinkFromRain;
		public final ForgeConfigSpec.ConfigValue<Integer> hydrationRain;
		public final ForgeConfigSpec.ConfigValue<Double> saturationRain;
		public final ForgeConfigSpec.ConfigValue<Double> dirtyRain;
		public final ForgeConfigSpec.ConfigValue<Boolean> drinkFromWater;
		public final ForgeConfigSpec.ConfigValue<Integer> hydrationWater;
		public final ForgeConfigSpec.ConfigValue<Double> saturationWater;
		public final ForgeConfigSpec.ConfigValue<Double> dirtyWater;
		public final ForgeConfigSpec.ConfigValue<Integer> hydrationPotion;
		public final ForgeConfigSpec.ConfigValue<Double> saturationPotion;
		public final ForgeConfigSpec.ConfigValue<Double> dirtyPotion;
		public final ForgeConfigSpec.ConfigValue<Integer> hydrationPurified;
		public final ForgeConfigSpec.ConfigValue<Double> saturationPurified;
		public final ForgeConfigSpec.ConfigValue<Double> dirtyPurified;
		public final ForgeConfigSpec.ConfigValue<Boolean> glassBottleLootAfterDrink;

		// Heart Fruits
		public final ForgeConfigSpec.ConfigValue<Boolean> heartFruitsEnabled;
		
		public final ForgeConfigSpec.ConfigValue<Integer> heartsLostOnDeath;
		public final ForgeConfigSpec.ConfigValue<Integer> maxAdditionalHearts;
		
		public final ForgeConfigSpec.ConfigValue<Integer> additionalHeartsPerFruit;
		public final ForgeConfigSpec.ConfigValue<Boolean> heartFruitsGiveRegen;

		// Localized Body Damage
		public final ForgeConfigSpec.ConfigValue<Boolean> localizedBodyDamageEnabled;
		public final ForgeConfigSpec.ConfigValue<Double> headCriticalShotMultiplier;
		public final ForgeConfigSpec.ConfigValue<Double> bodyDamageMultiplier;
		public final ForgeConfigSpec.ConfigValue<Double> bodyHealthRatioRecoveredFromSleep;
		public final ForgeConfigSpec.ConfigValue<Double> healthRatioRecoveredFromSleep;

		public final ForgeConfigSpec.ConfigValue<String> bodyPartHealthMode;
		public final ForgeConfigSpec.ConfigValue<Double> headPartHealth;
		public final ForgeConfigSpec.ConfigValue<Double> armsPartHealth;
		public final ForgeConfigSpec.ConfigValue<Double> chestPartHealth;
		public final ForgeConfigSpec.ConfigValue<Double> legsPartHealth;
		public final ForgeConfigSpec.ConfigValue<Double> feetPartHealth;

		public final ForgeConfigSpec.ConfigValue<List<String>> headPartEffects;
		public final ForgeConfigSpec.ConfigValue<List<Integer>> headPartEffectAmplifiers;
		public final ForgeConfigSpec.ConfigValue<List<Float>> headPartEffectThresholds;

		public final ForgeConfigSpec.ConfigValue<List<String>> armsPartEffects;
		public final ForgeConfigSpec.ConfigValue<List<Integer>> armsPartEffectAmplifiers;
		public final ForgeConfigSpec.ConfigValue<List<Float>> armsPartEffectThresholds;
		public final ForgeConfigSpec.ConfigValue<List<String>> bothArmsPartEffects;
		public final ForgeConfigSpec.ConfigValue<List<Integer>> bothArmsPartEffectAmplifiers;
		public final ForgeConfigSpec.ConfigValue<List<Float>> bothArmsPartEffectThresholds;

		public final ForgeConfigSpec.ConfigValue<List<String>> chestPartEffects;
		public final ForgeConfigSpec.ConfigValue<List<Integer>> chestPartEffectAmplifiers;
		public final ForgeConfigSpec.ConfigValue<List<Float>> chestPartEffectThresholds;

		public final ForgeConfigSpec.ConfigValue<List<String>> legsPartEffects;
		public final ForgeConfigSpec.ConfigValue<List<Integer>> legsPartEffectAmplifiers;
		public final ForgeConfigSpec.ConfigValue<List<Float>> legsPartEffectThresholds;
		public final ForgeConfigSpec.ConfigValue<List<String>> bothLegsPartEffects;
		public final ForgeConfigSpec.ConfigValue<List<Integer>> bothLegsPartEffectAmplifiers;
		public final ForgeConfigSpec.ConfigValue<List<Float>> bothLegsPartEffectThresholds;

		public final ForgeConfigSpec.ConfigValue<List<String>> feetPartEffects;
		public final ForgeConfigSpec.ConfigValue<List<Integer>> feetPartEffectAmplifiers;
		public final ForgeConfigSpec.ConfigValue<List<Float>> feetPartEffectThresholds;
		public final ForgeConfigSpec.ConfigValue<List<String>> bothFeetPartEffects;
		public final ForgeConfigSpec.ConfigValue<List<Integer>> bothFeetPartEffectAmplifiers;
		public final ForgeConfigSpec.ConfigValue<List<Float>> bothFeetPartEffectThresholds;

		public final ForgeConfigSpec.ConfigValue<Double> healingHerbsHealingValue;
		public final ForgeConfigSpec.ConfigValue<Integer> healingHerbsHealingTime;
		public final ForgeConfigSpec.ConfigValue<Integer> healingHerbsUseTime;
		public final ForgeConfigSpec.ConfigValue<Integer> healingHerbsHealingCharges;
		public final ForgeConfigSpec.ConfigValue<Double> plasterHealingValue;
		public final ForgeConfigSpec.ConfigValue<Integer> plasterHealingTime;
		public final ForgeConfigSpec.ConfigValue<Integer> plasterUseTime;
		public final ForgeConfigSpec.ConfigValue<Integer> plasterHealingCharges;
		public final ForgeConfigSpec.ConfigValue<Double> bandageHealingValue;
		public final ForgeConfigSpec.ConfigValue<Integer> bandageHealingTime;
		public final ForgeConfigSpec.ConfigValue<Integer> bandageUseTime;
		public final ForgeConfigSpec.ConfigValue<Integer> bandageHealingCharges;
		public final ForgeConfigSpec.ConfigValue<Double> tonicHealingValue;
		public final ForgeConfigSpec.ConfigValue<Integer> tonicHealingTime;
		public final ForgeConfigSpec.ConfigValue<Integer> tonicUseTime;
		public final ForgeConfigSpec.ConfigValue<Double> medikitHealingValue;
		public final ForgeConfigSpec.ConfigValue<Integer> medikitHealingTime;
		public final ForgeConfigSpec.ConfigValue<Integer> medikitUseTime;
		
		Common(ForgeConfigSpec.Builder builder)
		{
			builder.comment(new String [] {
						" Options related to enabling/disabling specific features",
						" See the jsons folder to customize the temperature of specific blocks, liquids, armors, etc."
					}).push("core");
			temperatureEnabled = builder
					.comment(" Whether the temperature system is enabled.")
					.define("Temperature Enabled", true);
			thirstEnabled = builder
					.comment(" Whether the thirst system is enabled.")
					.define("Thirst Enabled", true);
			heartFruitsEnabled = builder
					.comment(" Whether heart fruits are functional and generate in-world.")
					.define("Heart Fruits Enabled", true);
			localizedBodyDamageEnabled = builder
					.comment(" Whether body members receive localized damages.")
					.define("Localized Body Damage Enabled", true);
			hideInfoFromDebug = builder
					.comment(" If enabled, information like position and direction will be hidden from the debug screen (F3).")
					.define("Hide Info From Debug", true);

			builder.push("advanced");
			routinePacketSync = builder
					.comment(" How often player temperature is regularly synced between the client and server, in ticks.",
							" Lower values will increase accuracy at the cost of performance")
					.defineInRange("Routine Packet Sync", 30, 1, Integer.MAX_VALUE);

			builder.pop();
			builder.pop();

			builder.comment(" Options related to the player food data").push("food");
			baseFoodExhaustion = builder
					.comment(" Food exhausted every 10 ticks. Increase the base minecraft food exhaustion.")
					.defineInRange("Base Food Exhaustion", 0.03d, 0, 1000.0D);
			builder.pop();
			
			builder.comment(" Options related to the temperature system").push("temperature");
			dangerousTemperature = builder
					.comment(" If enabled, players will take damage from the effects of temperature.")
					.define("Dangerous Temperature Effects", true);

			builder.push("on-death");
			temperatureResistanceOnDeathEnabled = builder
					.comment(" If enabled, players will be immune to temperature effects after death.")
					.define("Temperature Resistance Enabled", true);
			temperatureResistanceOnDeathTime = builder
					.comment(" Temperature resistance period in ticks while the player is immune to temperature effects.")
					.defineInRange("Temperature Resistance Time", 1800, 0, 100000);
			builder.pop();

			builder.push("secondary_effects");
			temperatureSecondaryEffects = builder
					.comment(" If enabled, players will also receive other effects from their current temperature state.",
							" If the player is too hot, hydration will deplete faster. If the player is too cold, hunger will deplete faster.")
					.define("Secondary Temperature Effects", true);
			heatThirstEffectModifier = builder
					.comment(" How much thirst exhaustion will be added every 50 ticks with no amplification effect.")
					.defineInRange("Heat Thirst Effect Modifier", 0.1d, 0, 1000.0d);
			coldHungerEffectModifier = builder
					.comment(" How much food exhaustion will be added every 50 ticks with no amplification effect.",
							" As reference, the hunger effect add 0.025 food exhaustion every 50 ticks.")
					.defineInRange("Cold Hunger Modifier", 0.05d, 0, 1000.0d);
			builder.pop();

			onFireModifier = builder
					.comment(" How much of an effect being on fire has on a player's temperature.")
					.define("Player On Fire Modifier", 12.5d);
			sprintModifier = builder
					.comment(" How much of an effect sprinting has on a player's temperature.")
					.define("Player Sprint Modifier", 1.5d);
			altitudeModifier = builder
					.comment(" How much the effects of the player's altitude on temperature are multiplied starting at Y 64.",
							" Each 64 blocks further from Y 64 will reduce player's temperature by this value.")
					.define("Altitude Modifier", -3.0d);
			enchantmentMultiplier = builder
					.comment(" Increases/decreases the effect that cooling/heating enchantments have on a player's temperature.")
					.define("Enchantment Modifier", 1.0d);
			showPotionEffectParticles = builder
					.comment(" If enabled, players will see particles on them when temperature resistance effect active.",
							" If disabled, players won't see particles but the potion color will turn black due to forge weird behavior.")
					.define("Show Temperature Potion Effect Particles", true);
			
			builder.push("wetness");
			wetnessMode = builder
					.comment(" How a player's \"wetness\" is determined. Accepted values are as follows:",
							"   DISABLE - Disable wetness and any effects on temperature it might have.",
							"   SIMPLE - Wetness is only based on whether you're in water/rain or not. Slightly better in terms of performance.",
							"   DYNAMIC - Wetness can change dynamically based on various conditions, and does not instantly go away when moving out of water.",
							" Any other value will default to DISABLE.")
					.define("Wetness Mode", "DYNAMIC");
			
			wetMultiplier = builder
					.comment(" How much being wet influences the player's temperature.")
					.define("Wetness Modifier", -10.0d);

			wetnessDecrease = builder
					.comment(" How much the wetness decrease when out of water, in case of dynamic wetness.")
					.defineInRange("Wetness Decrease", -5, -1000, 0);
			wetnessRainIncrease = builder
					.comment(" How much the wetness increase when under rain, in case of dynamic wetness.")
					.defineInRange("Wetness Under Rain Increase", 5, 0, 1000);
			wetnessFluidIncrease = builder
					.comment(" How much the wetness increase when the player is in a fluid, scale by the amount of fluid in the block, in case of dynamic wetness.",
							" The defined value is for a full block of fluid, and goes up to 2 times this value when fully immerge.")
					.defineInRange("Wetness In Fluid Increase", 10, 0, 1000);
			builder.pop();

			builder.comment(" Default temperature added to the player, based on the dimension.")
					.push("dimension-default");
			overworldDefaultTemperature = builder.define( "Default Overworld Modifier", 20.0d);
			netherDefaultTemperature = builder.define( "Default Nether Modifier", 30.0d);
			endDefaultTemperature = builder.define( "Default The End Modifier", 5.0d);
			builder.pop();
			
			builder.push("huddling");
			playerHuddlingModifier = builder
					.comment(" How much nearby players increase the ambient temperature by.", " Note that this value stacks!")
					.define("Player Huddling Modifier", 0.5d);
			playerHuddlingRadius = builder
					.comment(" The radius, in blocks, around which players will add to each other's temperature.")
					.defineInRange("Player Huddling Radius", 1, 0, 10);
			builder.pop();

			builder.push("biomes");
			biomeTemperatureMultiplier = builder
					.comment(" How much a biome's temperature effects are multiplied.")
					.defineInRange("Biome Temperature Multiplier", 16.0d, 0.0d, Double.POSITIVE_INFINITY);
			biomeEffectsEnabled = builder
					.comment(" Whether biomes will have an effect on a player's temperature.")
					.define("Biomes affect Temperature", true);
			biomeDrynessEffectEnabled = builder
					.comment(" Whether hot biome's dryness will make days really hot and nights really cold.")
					.define("Biome's dryness affects Temperature", false);
			builder.pop();
			
			builder.push("weather");
			rainTemperatureModifier = builder
					.comment(" How much of an effect rain has on temperature.")
					.define("Rain Temperature Modifier", -2.0d);
			snowTemperatureModifier = builder
					.comment(" How much of an effect snow has on temperature.")
					.define("Snow Temperature Modifier", -6.0d);
			builder.pop();
			
			builder.push("time");
			timeModifier = builder
					.comment(" How much Time has effect on Temperature.",
							" Maximum effect at noon (positive) and midnight (negative), following a sinusoidal")
					.defineInRange("Time Based Temperature Modifier", 2.0d, 0.0d, Double.POSITIVE_INFINITY);
			biomeTimeMultiplier = builder
					.comment(" How strongly time in extreme temperature biomes affect player's temperature.",
							" Extreme temperature biomes (like snowy taiga, deserts, ...) will multiply the time based temperature by this value, while temperate biome won't be affected by this value, following a linear.")
					.defineInRange("Biome Time Multiplier", 1.75d, 1.0d, Double.POSITIVE_INFINITY);
			shadeTimeModifier = builder
					.comment(" Staying in the shade or during cloudy weather will reduce player's temperature by this amount based on time of the day (maximum effect at noon, following sinusoidal).",
							" Only effective in hot biomes and during day time!")
					.define("Shade Time Modifier", -6);
			builder.pop();

			builder.comment(" Temperature coat adds temperature effects on armors by using the sewing table.",
					" Adaptive means the coating will maintain the player's temperature temperate.")
					.push("coat");

			builder.comment(" Add an adaptive heating effect on armors.").push("heating");
			heatingCoat1Modifier = builder.defineInRange("Heating Coat I", 1.0d, 0, 1000.0d);
			heatingCoat2Modifier = builder.defineInRange("Heating Coat II", 2.0d, 0, 1000.0d);
			heatingCoat3Modifier = builder.defineInRange("Heating Coat III", 3.0d, 0, 1000.0d);
			builder.pop();

			builder.comment(" Add an adaptive cooling effect on armors.").push("cooling");
			coolingCoat1Modifier = builder.defineInRange("Cooling Coat I", 1.0d, 0, 1000.0d);
			coolingCoat2Modifier = builder.defineInRange("Cooling Coat II", 2.0d, 0, 1000.0d);
			coolingCoat3Modifier = builder.defineInRange("Cooling Coat III", 3.0d, 0, 1000.0d);
			builder.pop();

			builder.comment(" Add an adaptive temperature effect on armors that can both heat and cool the player.")
					.push("thermal");
			thermalCoat1Modifier = builder.defineInRange("Thermal Coat I", 1.0d, 0, 1000.0d);
			thermalCoat2Modifier = builder.defineInRange("Thermal Coat II", 2.0d, 0, 1000.0d);
			thermalCoat3Modifier = builder.defineInRange("Thermal Coat III", 3.0d, 0, 1000.0d);
			builder.pop();
			builder.pop();
			
			builder.push("advanced");
			tempInfluenceMaximumDist = builder
					.comment(" Maximum distance, in blocks, where thermal sources will have an effect on temperature.")
					.defineInRange("Temperature Influence Maximum Distance", 20, 1, 40);
			tempInfluenceUpDistMultiplier = builder
					.comment(" How strongly distance above the player is reduced where thermal sources will have an effect on temperature.")
					.comment(" Example max dist is 10, up mult is 0.75 -> max distance is 10 * 0.75 = 7.5 blocks above the player.",
							" Logic is as the heat goes up, the strength of the heat source above the player is decreased faster with distance.")
					.defineInRange("Temperature Influence Up Distance Multiplier", 0.75, 0.0, 1.0);
			tempInfluenceOutsideDistMultiplier = builder
					.comment(" How strongly distance outside a structure is reduced where thermal sources will have an effect on temperature.",
							" The outside maximum distance is defined as the maximum distance * this value")
					.defineInRange("Temperature Influence Outside Distance Multiplier", 0.5, 0.0, 1.0);
			builder
					.comment(" The player's temperature will be adjusted each temperature tick rate," ,
							" by an amount of temperature defined between the minimum and the maximum temperature modification adjusted linearly.")
					.push("temperature-modification");
			tickRate = builder
					.comment(" Amount of time in ticks between 2 player temperature modification.")
					.defineInRange("Temperature Tick Rate", 10, 5, Integer.MAX_VALUE);
			maxTemperatureModification = builder
					.comment(" Maximum amount of temperature the player's temperature can be modified at each temperature tick rate.",
							" Correspond to the amount of temperature given when temperature difference is maximum.")
					.defineInRange("Maximum Temperature Modification", 2, 0.1, Integer.MAX_VALUE);
			minTemperatureModification = builder
					.comment(" Minimum amount of temperature the player's temperature can be modified at each temperature tick rate.",
							" Correspond to the amount of temperature given when there is no temperature difference")
					.defineInRange("Minimum Temperature Modification", 0.2, 0.1, Integer.MAX_VALUE);
			builder.pop();
			builder.pop();
			
			builder.push("integration");
			
			builder.push("seasons");
			seasonTemperatureEffects = builder
					.comment(" If Serene Seasons is installed, then seasons", " will have an effect on the player's temperature.")
					.define("Seasons affect Temperature", true);
			tropicalSeasonsEnabled = builder
					.comment(" If the tropical seasons are disabled, the normal summer-autumn-winter-spring seasons are applied.",
							" If disabled, dry and wet seasons are applied for hot biomes.")
					.define("Tropical Seasons Enabled", true);
			seasonCardsEnabled = builder
					.comment(" If season cards are enabled, season cards will appear at every season changes.")
					.define("Season Cards Enabled", true);

			builder.comment(" Temperature modifiers per season in temperate biomes.").push("temperate");
			builder.push("spring");
			earlySpringModifier = builder.define("Early Spring Modifier", -3);
			midSpringModifier = builder.define("Mid Spring Modifier", 0);
			lateSpringModifier = builder.define("Late Spring Modifier", 3);
			builder.pop();
			
			builder.push("summer");
			earlySummerModifier = builder.define("Early Summer Modifier", 5);
			midSummerModifier = builder.define("Mid Summer Modifier", 8);
			lateSummerModifier = builder.define("Late Summer Modifier", 5);
			builder.pop();
			
			builder.push("autumn");
			earlyAutumnModifier = builder.define("Early Autumn Modifier", 3);
			midAutumnModifier = builder.define("Mid Autumn Modifier", 0);
			lateAutumnModifier = builder.define("Late Autumn Modifier", -3);
			builder.pop();
			
			builder.push("winter");
			earlyWinterModifier = builder.define("Early Winter Modifier", -7);
			midWinterModifier = builder.define("Mid Winter Modifier", -12);
			lateWinterModifier = builder.define("Late Winter Modifier", -7);
			builder.pop();
			builder.pop();
			
			builder.comment(" Temperature modifiers per season in tropical biomes.").push("tropical");
			builder.push("wet-season");
			earlyWetSeasonModifier = builder.define("Early Wet Season Modifier", -1);
			midWetSeasonModifier = builder.define("Mid Wet Season Modifier", -5);
			lateWetSeasonModifier = builder.define("Late Wet Season Modifier", -1);
			builder.pop();
			
			builder.push("dry-season");
			earlyDrySeasonModifier = builder.define("Early Dry Season Modifier", 3);
			midDrySeasonModifier = builder.define("Mid Dry Season Modifier", 7);
			lateDrySeasonModifier = builder.define("Late Dry Season Modifier", 3);
			builder.pop();
			builder.pop();

			builder.pop();
			builder.pop();
			builder.pop();

			builder.push("environment");

			builder.push("flowers");
			sunFernBiomeNames = builder.comment(" In which biome names the Sun Fern will spawn.")
					.define("Sun Fern Biome Names Spawn List", new ArrayList<>());
			sunFernBiomeCategories = builder.comment(" In which biome categories the Sun Fern will spawn.")
					.define("Sun Fern Biome Categories Spawn List", Arrays.asList("DESERT", "SAVANNA"));
			iceFernBiomeNames = builder.comment(" In which biome names the Ice Fern will spawn.")
					.define("Ice Fern Biome Names Spawn List", new ArrayList<>());
			iceFernBiomeCategories = builder.comment(" In which biome categories the Ice Fern will spawn.")
					.define("Ice Fern Biome Categories Spawn List", Arrays.asList("TAIGA", "ICY"));
			waterPlantBiomeNames = builder.comment(" In which biome names the Water Plant will spawn.")
					.define("Water Plant Biome Names Spawn List", new ArrayList<>());
			waterPlantBiomeCategories = builder.comment(" In which biome categories the Water Plant will spawn.")
					.define("Water Plant Biome Categories Spawn List", Collections.singletonList("DESERT"));
			builder.pop();

			builder.pop();

			builder.comment(" Options related to thirst").push("thirst");
			dangerousDehydration = builder
					.comment(" If enabled, players will take damage from the complete dehydration.")
					.define("Dangerous Dehydration", true);
			builder.push("exhaustion");
			baseThirstExhaustion = builder
					.comment(" Thirst exhausted every 10 ticks.")
					.defineInRange("Base Thirst Exhaustion", 0.03d, 0, 1000.0d);
			sprintingThirstExhaustion = builder
					.comment(" Thirst exhausted when sprinting, replacing the base thirst exhausted.")
					.defineInRange("Sprinting Thirst Exhaustion", 0.15d, 0, 1000.0d);
			onJumpThirstExhaustion = builder
					.comment(" Thirst exhausted on every jump.")
					.defineInRange("On Jump Thirst Exhaustion", 0.3d, 0, 1000.0d);
			onBlockBreakThirstExhaustion = builder
					.comment(" Thirst exhausted on every block break.")
					.defineInRange("On Block Break Thirst Exhaustion", 0.1d, 0, 1000.0d);
			onAttackThirstExhaustion = builder
					.comment(" Thirst exhausted on every attack.")
					.defineInRange("On Attack Thirst Exhaustion", 0.5d, 0, 1000.0d);
			builder.pop();
			dehydrationDamageScaling = builder
					.comment(" Scaling of the damages dealt when completely dehydrated. Each tick damage will be increased by this value.")
					.defineInRange("Dehydration Damage Scaling", 0.3d, 0, 1000.0d);
			thirstEffectModifier = builder
					.comment(" How many thirst exhaustion will be added every 50 ticks when the player suffers from not amplified Thirst Effect.",
							" The player will suffer Thirst Effect from dirty water by example.")
					.defineInRange("Thirst Effect Modifier", 0.25d, 0, 1000);
			builder.push("canteen");
			canteenCapacity = builder
					.comment(" Capacity of the canteen used to store water.")
					.defineInRange("Canteen Capacity", 5, 0, 1000);
			largeCanteenCapacity = builder
					.comment(" Capacity of the large canteen used to store water.")
					.defineInRange("Large Canteen Capacity", 10, 0, 1000);
			allowOverridePurifiedWater = builder
					.comment(" Allow override of purified water stored in canteen with normal water.")
					.define("Allow Override Purified Water", true);
			builder.pop();
			builder.push("rain");
			drinkFromRain = builder
					.comment(" Whether players can drink from rain or not.")
					.define("Drink From Rain", true);
			hydrationRain = builder
					.comment(" Amount of hydration recovered when drinking from the rain.")
					.defineInRange("Hydration", 1, 0, 20);
			saturationRain = builder
					.comment(" Amount of saturation recovered when drinking from the rain.")
					.defineInRange("Saturation", 0, 0, 20.0d);
			dirtyRain = builder
					.comment(" Chance of getting a thirst effect while drinking from the rain.")
					.defineInRange("Dirty", 0, 0, 1.0d);
			builder.pop();
			builder.push("water");
			drinkFromWater = builder
					.comment(" Whether players can drink from water (or flowing water) block or not.")
					.define("Drink From Water", true);
			hydrationWater = builder
					.comment(" Amount of hydration recovered while drinking water.")
					.defineInRange("Hydration", 3, 0, 20);
			saturationWater = builder
					.comment(" Amount of saturation recovered while drinking water.")
					.defineInRange("Saturation", 0, 0, 20.0d);
			dirtyWater = builder
					.comment(" Chance of getting a thirst effect while drinking water.")
					.defineInRange("Dirty", 0.75d, 0, 1.0d);
			builder.pop();
			builder.comment(" Amount recovered by potions with effects").push("potion");
			hydrationPotion = builder
					.comment(" Amount of hydration recovered while drinking a potion.")
					.defineInRange("Hydration", 3, 0, 20);
			saturationPotion = builder
					.comment(" Amount of saturation recovered while drinking a potion.")
					.defineInRange("Saturation", 0.3d, 0, 20.0d);
			dirtyPotion = builder
					.comment(" Chance of getting a thirst effect while drinking a potion.")
					.defineInRange("Dirty", 0, 0, 1.0d);
			builder.pop();
			builder.push("purified-water");
			hydrationPurified = builder
					.comment(" Amount of hydration recovered while drinking purified water.")
					.defineInRange("Hydration", 6, 0, 20);
			saturationPurified = builder
					.comment(" Amount of saturation recovered while drinking purified water.")
					.defineInRange("Saturation", 1.5d, 0, 20.0d);
			dirtyPurified = builder
					.comment(" Chance of getting a thirst effect while drinking purified water.")
					.defineInRange("Dirty", 0, 0, 1.0d);
			builder.pop();
			builder.push("juices");
			glassBottleLootAfterDrink = builder
					.comment(" Whether the player retrieves a glass bottle after drinking a juice.")
					.define("Glass Bottle Loot After Drinking A Juice", true);
			builder.pop();
			builder.pop();
			
			builder.comment(" Options related to heart fruits").push("heart-fruits");
			maxAdditionalHearts = builder
					.comment(" Maximum number of additional hearts that can be given by Heart Fruits.")
					.defineInRange("Maximum Additional Hearts", 10, 1, Integer.MAX_VALUE);
			heartsLostOnDeath = builder
					.comment(" The number of additional hearts lost on death.",
							" Set to -1 to force loss of all additional hearts on death.",
							" Set to 0 to make additional hearts permanent.")
					.defineInRange("Hearts Lost On Death", -1, -1, Integer.MAX_VALUE);
			
			builder.push("effects");
			additionalHeartsPerFruit = builder
					.comment(" Amount of hearts gained from eating a Heart Fruit.")
					.defineInRange("Additional Hearts Per Heart Fruit", 1, 1, Integer.MAX_VALUE);
			heartFruitsGiveRegen = builder
					.comment(" Whether Heart Fruits give a strong regeneration effect.")
					.define("Heart Fruits Give Regen", true);
			builder.pop();
			builder.pop();

			builder.comment(" Options related to localized body damage",
					" The damageSourceBodyParts.json allows you to define for specific damage source, the damage spread across specified body parts.",
					" The damage distribution can either be ONE_OF or ALL. ALL means the damage are equally divided across all body parts.").push("body-damage");
			headCriticalShotMultiplier = builder
					.comment(" Multiply the damage taken by the player when shot in the head.")
					.defineInRange("Headshot Multiplier", 2.0d, 1.0d, 1000.0d);
			bodyDamageMultiplier = builder
					.comment(" How much of the hurt player's damage is assigned to the body parts.")
					.defineInRange("Body Damage Multiplier", 1.0d, 0.0d, 1000.0d);
			bodyHealthRatioRecoveredFromSleep = builder
					.comment(" How much health ratio are recovered in all body parts from bed sleeping.")
					.defineInRange("Body Part Health Ratio Recovered", 0.3d, 0.0d, 1.0d);
			healthRatioRecoveredFromSleep = builder
					.comment(" How much health ratio are recovered from bed sleeping.")
					.defineInRange("Health Ratio Recovered", 0.3d, 0.0d, 1.0d);

			builder.push("healing-items");

			builder.push("healing-herbs");
			healingHerbsHealingValue = builder
					.comment(" Total Healing Value recovered during the healing time.")
					.defineInRange("Healing Herbs Healing Value", 2.0d, 0.0d, 1000.0d);
			healingHerbsHealingTime = builder
					.comment(" Healing Time in ticks along which the body part recovers its health.")
					.defineInRange("Healing Herbs Healing Time", 600, 0, 1000);
			healingHerbsUseTime = builder
					.comment(" Item use time is ticks.")
					.defineInRange("Healing Herbs Use Time", 20, 0, 1000);
			healingHerbsHealingCharges = builder
					.comment(" Healing Charges, each charge used when selecting a body part to heal.")
					.defineInRange("Healing Herbs Healing Charges", 1, 0, 1000);
			builder.pop();
			builder.push("plaster");
			plasterHealingValue = builder
					.defineInRange("Plaster Healing Value", 3.0d, 0.0d, 1000.0d);
			plasterHealingTime = builder
					.defineInRange("Plaster Healing Time", 400, 0, 1000);
			plasterUseTime = builder
					.defineInRange("Plaster Use Time", 20, 0, 1000);
			plasterHealingCharges = builder
					.defineInRange("Plaster Healing Charges", 1, 0, 1000);
			builder.pop();
			builder.push("bandage");
			bandageHealingValue = builder
					.defineInRange("Bandage Healing Value", 3.0d, 0.0d, 1000.0d);
			bandageHealingTime = builder
					.defineInRange("Bandage Healing Time", 300, 0, 1000);
			bandageUseTime = builder
					.defineInRange("Bandage Use Time", 30, 0, 1000);
			bandageHealingCharges = builder
					.defineInRange("Bandage Healing Charges", 3, 0, 1000);
			builder.pop();
			builder.comment(" Tonic heals all body parts.").push("tonic");
			tonicHealingValue = builder
					.defineInRange("Tonic Healing Value", 5.0d, 0.0d, 1000.0d);
			tonicHealingTime = builder
					.defineInRange("Tonic Healing Time", 600, 0, 1000);
			tonicUseTime = builder
					.defineInRange("Tonic Use Time", 50, 0, 1000);
			builder.pop();
			builder.comment(" Medikit heals all body parts.").push("medikit");
			medikitHealingValue = builder
					.defineInRange("Medikit Healing Value", 8.0d, 0.0d, 1000.0d);
			medikitHealingTime = builder
					.defineInRange("Medikit Healing Time", 400, 0, 1000);
			medikitUseTime = builder
					.defineInRange("Medikit Use Time", 50, 0, 1000);
			builder.pop();

			builder.pop();

			builder.push("body-parts-health");
			bodyPartHealthMode = builder
					.comment(" How a player's body part health is determined. Accepted values are as follows:",
							"   SIMPLE - Body parts will have initial fixed values. The body parts health define the health value.",
							"       In this case, if the 'headPartHeath = 10', the head will have '10' health.",
							"   DYNAMIC - Body parts will have dynamic values based on the player's max health. In this case, the body parts health is a multiplier of the player's max health.",
							"       In this case, if the 'headPartHeath = 0.3', the head will have '0.3' * 'player max health' health.",
							" Any other value will default to SIMPLE.")
					.define("Body Part Health Mode", "DYNAMIC");

			headPartHealth = builder.defineInRange("Head Part Health", 0.2d, 0.0d, 1000.0d);
			armsPartHealth = builder.comment(" Both arms will have this health.")
					.defineInRange("Arms Part Health", 0.2d, 0.0d, 1000.0d);;
			chestPartHealth = builder.defineInRange("Chest Part Health", 0.3d, 0.0d, 1000.0d);;
			legsPartHealth = builder.comment(" Both legs will have this health.")
					.defineInRange("Legs Part Health", 0.3d, 0.0d, 1000.0d);;
			feetPartHealth = builder.comment(" Both feet will have this health.")
					.defineInRange("Feet Part Health", 0.2d, 0.0d, 1000.0d);;
			builder.pop();

			builder.push("body-parts-effects");
			builder.comment(" Each effect, threshold and amplifier lists must have the same number of items.",
					" The first effect will be triggered with the first amplifier value when the first threshold is reach.")
					.push("head");
			headPartEffects = builder
					.comment(" The list of effects that will be triggered when the head is damaged by the percentage of remaining head health defined in the thresholds.")
					.define("Head Part Effects", Collections.singletonList(LegendarySurvivalOverhaul.MOD_ID + ":headache"));
			headPartEffectAmplifiers = builder
					.comment(" The list of amplifiers the effect will have.",
							" 0 means the basic effect, 1 means the effect is amplified once.")
					.define(" Head Part Effect Amplifiers", Collections.singletonList(0));
			headPartEffectThresholds = builder
					.comment(" The list of thresholds for which each effect will be triggered. A threshold is a percentage of remaining head health.",
							" 0 means the head is fully damaged.")
					.define(" Head Part Effect Thresholds", Collections.singletonList(0.2f));
			builder.pop();
			builder.push("arms");
			armsPartEffects = builder.define("Arms Part Effects", Collections.singletonList("minecraft:mining_fatigue"));
			armsPartEffectAmplifiers = builder.define("Arms Part Effect Amplifiers", Collections.singletonList(0));
			armsPartEffectThresholds = builder.define("Arms Part Effect Thresholds", Collections.singletonList(0.2f));
			bothArmsPartEffects = builder
					.comment(" These effects will be triggered when both arms reach the thresholds.",
							" If a same effect is used with a higher amplifier, the higher prevails (normal Minecraft behaviour).")
					.define("Both Arms Part Effects", Collections.singletonList("minecraft:weakness"));
			bothArmsPartEffectAmplifiers = builder.define("Both Arms Part Effect Amplifiers", Collections.singletonList(0));
			bothArmsPartEffectThresholds = builder.define("Both Arms Part Effect Thresholds", Collections.singletonList(0.2f));
			builder.pop();
			builder.push("chest");
			chestPartEffects = builder.define(" Chest Part Effects", Collections.singletonList(LegendarySurvivalOverhaul.MOD_ID + ":vulnerability"));
			chestPartEffectAmplifiers = builder.define(" Chest Part Effect Amplifier", Collections.singletonList(0));
			chestPartEffectThresholds = builder.define(" Chest Part Effect Thresholds", Collections.singletonList(0.2f));
			builder.pop();
			builder.push("legs");
			legsPartEffects = builder.define("Legs Part Effects", Collections.singletonList(LegendarySurvivalOverhaul.MOD_ID + ":hard_falling"));
			legsPartEffectAmplifiers = builder.define("Legs Part Effect Amplifiers", Collections.singletonList(0));
			legsPartEffectThresholds = builder.define("Legs Part Effect Thresholds", Collections.singletonList(0.2f));
			bothLegsPartEffects = builder.define("Both Legs Part Effects", Collections.singletonList(LegendarySurvivalOverhaul.MOD_ID + ":hard_falling"));
			bothLegsPartEffectAmplifiers = builder.define("Both Legs Part Effect Amplifiers", Collections.singletonList(1));
			bothLegsPartEffectThresholds = builder.define("Both Legs Part Effect Thresholds", Collections.singletonList(0.2f));
			builder.pop();
			builder.push("feet");
			feetPartEffects = builder.define("Feet Part Effects", Collections.singletonList("minecraft:slowness"));
			feetPartEffectAmplifiers = builder.define("Feet Part Effect Amplifiers", Collections.singletonList(0));
			feetPartEffectThresholds = builder.define("Feet Part Effect Thresholds", Collections.singletonList(0.2f));
			bothFeetPartEffects = builder.define("Both Feet Part Effects", Collections.singletonList("minecraft:slowness"));
			bothFeetPartEffectAmplifiers = builder.define("Both Feet Part Effect Amplifiers", Collections.singletonList(1));
			bothFeetPartEffectThresholds = builder.define("Both Feet Part Effect Thresholds", Collections.singletonList(0.2f));
			builder.pop();

			builder.pop();
			builder.pop();
		}
	}
	
	public static class Client
	{
		public final ForgeConfigSpec.ConfigValue<Boolean> showVanillaAnimationOverlay;
		public final ForgeConfigSpec.ConfigValue<String> temperatureDisplayMode;
		public final ForgeConfigSpec.ConfigValue<Integer> temperatureDisplayOffsetX;
		public final ForgeConfigSpec.ConfigValue<Integer> temperatureDisplayOffsetY;
		public final ForgeConfigSpec.ConfigValue<Boolean> foodSaturationDisplayed;
		
		public final ForgeConfigSpec.ConfigValue<Integer> wetnessIndicatorOffsetX;
		public final ForgeConfigSpec.ConfigValue<Integer> wetnessIndicatorOffsetY;

		public final ForgeConfigSpec.ConfigValue<Integer> bodyDamageIndicatorOffsetX;
		public final ForgeConfigSpec.ConfigValue<Integer> bodyDamageIndicatorOffsetY;
		public final ForgeConfigSpec.ConfigValue<Boolean> alwaysShowBodyDamageIndicator;

		public final ForgeConfigSpec.ConfigValue<Integer> seasonCardsOffsetX;
		public final ForgeConfigSpec.ConfigValue<Integer> seasonCardsOffsetY;
		public final ForgeConfigSpec.ConfigValue<Integer> seasonCardsSpawnDimensionDelayInTicks;
		public final ForgeConfigSpec.ConfigValue<Integer> seasonCardsDisplayTimeInTicks;
		public final ForgeConfigSpec.ConfigValue<Integer> seasonCardsFadeInInTicks;
		public final ForgeConfigSpec.ConfigValue<Integer> seasonCardsFadeOutInTicks;

		public final ForgeConfigSpec.ConfigValue<Boolean> showHydrationTooltip;
		public final ForgeConfigSpec.ConfigValue<Boolean> mergeHydrationAndSaturationTooltip;
		public final ForgeConfigSpec.ConfigValue<Boolean> thirstSaturationDisplayed;

		Client(ForgeConfigSpec.Builder builder)
		{
			
			builder.comment(new String[] {" Options related to the heads up display.",
					" These options will automatically update upon being saved."
					}).push("hud");
			builder.push("general");

			showVanillaAnimationOverlay = builder
					.comment(" Whether the vanilla animation of the Food bar and Hydration bar is rendered. The bar shakes more the lower they are.",
							" This mod render a new food bar as a secondary effect of a cold temperature.",
							" Disable this animation if the temperature secondary effect is enabled to allow a compatibility with other mods rendering the food bar (by example Appleskin).")
					.define("Show Vanilla Animation Overlay", true);
			builder.pop();
			
			builder.push("temperature");
			temperatureDisplayMode = builder
					.comment(" How temperature is displayed. Accepted values are as follows:",
							"    SYMBOL - Display the player's current temperature as a symbol above the hotbar.",
							"    NONE - Disable the temperature indicator.")
					.define("Temperature Display Mode", "SYMBOL");
			temperatureDisplayOffsetX = builder
					.comment(" The X and Y offset of the temperature indicator. Set both to 0 for no offset.")
					.define("Temperature Display X Offset", 0);
			temperatureDisplayOffsetY = builder
					.define("Temperature Display Y Offset", 0);
			foodSaturationDisplayed = builder
					.comment(" If enabled, the food saturation will be rendered on the Food Bar while the player suffers Cold Hunger Effect (secondary temperature effect).")
					.define("Show Food Saturation Bar", true);
			builder.push("wetness");
			wetnessIndicatorOffsetX = builder
					.comment(" The X and Y offset of the wetness indicator. Set both to 0 for no offset.")
					.define("Wetness Indicator X Offset", 0);
			wetnessIndicatorOffsetY = builder
					.define("Wetness Indicator Y Offset", 0);
			builder.pop();
			builder.pop();

			builder.pop();

			builder.push("body-damage");
			bodyDamageIndicatorOffsetX = builder
					.comment(" The X and Y offset of the body damage indicator. Set both to 0 for no offset.", " By default, render next to the inventory bar.")
					.define("Body Damage Indicator X Offset", 0);
			bodyDamageIndicatorOffsetY = builder.define("Body Damage Indicator Y Offset", 0);
			alwaysShowBodyDamageIndicator = builder
					.comment(" If true, the body damage indicator will always be rendered", " By default, the body damage indicator disappears when no wounded body limbs.")
					.define("Body Damage indicator Always Rendered", false);
			builder.pop();

			builder.push("season-cards");
			seasonCardsOffsetX = builder
					.comment(" The X and Y offset of the season cards. Set both to 0 for no offset.", " By default, render first top quarter vertically and centered horizontally.")
					.define("Season Cards X Offset", 0);
			seasonCardsOffsetY = builder
					.define("Season Cards Y Offset", 0);
			seasonCardsSpawnDimensionDelayInTicks = builder
					.comment(" The delay before rendering the season card at first player spawn or player dimension change.")
					.defineInRange("Season Cards Delay In Ticks", 40, 0, Integer.MAX_VALUE);
			seasonCardsDisplayTimeInTicks = builder
					.comment(" The display time in ticks that the season card will be fully rendered.")
					.defineInRange("Season Cards Display Time In Ticks", 40, 0, Integer.MAX_VALUE);
			seasonCardsFadeInInTicks = builder
					.comment(" The fade in time in ticks that the season card will appear.")
					.defineInRange("Season Cards Fade in In Ticks", 20, 0, Integer.MAX_VALUE);
			seasonCardsFadeOutInTicks = builder
					.comment(" The fade out time in ticks that the season card will disappear.")
					.defineInRange("Season Cards Fade Out In Ticks", 20, 0, Integer.MAX_VALUE);
			builder.pop();

			builder.push("thirst");
			showHydrationTooltip = builder
					.comment(" If enabled, show the hydration values in the item tooltip.")
					.define("Show Hydration Tooltip", true);
			mergeHydrationAndSaturationTooltip = builder
					.comment(" If enabled, show the hydration and the saturation values on the same line in the tooltip.")
					.define("Merge Hydration And Saturation Tooltip", true);
			thirstSaturationDisplayed = builder
					.comment(" Whether the Thirst Saturation is displayed or not.")
					.define("Render the thirst saturation", true);
			builder.pop();
		}
	}
	
	public static class Server
	{
		Server(ForgeConfigSpec.Builder builder)
		{
			
		}
	}
	
	public static class Baked
	{
		// Core
		public static int routinePacketSync;
		public static boolean hideInfoFromDebug;
		public static double baseFoodExhaustion;

		// Temperature
		public static boolean temperatureEnabled;
		public static int tickRate;
		public static double minTemperatureModification;
		public static double maxTemperatureModification;
		public static boolean showPotionEffectParticles;
		public static boolean temperatureResistanceOnDeathEnabled;
		public static int temperatureResistanceOnDeathTime;

		public static boolean dangerousTemperature;
		public static boolean temperatureSecondaryEffects;
		public static double heatThirstEffectModifier;
		public static double coldHungerEffectModifier;
		public static boolean foodSaturationDisplayed;
		public static boolean showVanillaAnimationOverlay;
		
		public static boolean biomeEffectsEnabled;
		public static boolean biomeDrynessEffectEnabled;
		public static double biomeTemperatureMultiplier;

		public static double overworldDefaultTemperature;
		public static double netherDefaultTemperature;
		public static double endDefaultTemperature;
		
		public static double rainTemperatureModifier;
		public static double snowTemperatureModifier;
		
		public static double altitudeModifier;
		
		public static boolean seasonTemperatureEffects;
		public static boolean tropicalSeasonsEnabled;
		public static boolean seasonCardsEnabled;
		
		public static double timeModifier;
		public static double biomeTimeMultiplier;
		public static int shadeTimeModifier;
		public static int tempInfluenceMaximumDist;
		public static double tempInfluenceUpDistMultiplier;
		public static double tempInfluenceOutsideDistMultiplier;
		public static double sprintModifier;
		public static double onFireModifier;
		public static double enchantmentMultiplier;
		
		public static double playerHuddlingModifier;
		public static int playerHuddlingRadius;
		
		public static WetnessMode wetnessMode;
		public static double wetMultiplier;
		public static int wetnessDecrease;
		public static int wetnessRainIncrease;
		public static int wetnessFluidIncrease;

		public static double heatingCoat1Modifier;
		public static double heatingCoat2Modifier;
		public static double heatingCoat3Modifier;

		public static double coolingCoat1Modifier;
		public static double coolingCoat2Modifier;
		public static double coolingCoat3Modifier;

		public static double thermalCoat1Modifier;
		public static double thermalCoat2Modifier;
		public static double thermalCoat3Modifier;
		
		public static int earlySpringModifier;
		public static int midSpringModifier;
		public static int lateSpringModifier;

		public static int earlySummerModifier;
		public static int midSummerModifier;
		public static int lateSummerModifier;

		public static int earlyAutumnModifier;
		public static int midAutumnModifier;
		public static int lateAutumnModifier;

		public static int earlyWinterModifier;
		public static int midWinterModifier;
		public static int lateWinterModifier;

		public static int earlyWetSeasonModifier;
		public static int midWetSeasonModifier;
		public static int lateWetSeasonModifier;

		public static int earlyDrySeasonModifier;
		public static int midDrySeasonModifier;
		public static int lateDrySeasonModifier;

		// Environment
		public static List<String> sunFernBiomeNames;
		public static List<String> sunFernBiomeCategories;
		public static List<String> iceFernBiomeNames;
		public static List<String> iceFernBiomeCategories;
		public static List<String> waterPlantBiomeNames;
		public static List<String> waterPlantBiomeCategories;

		// Thirst
		public static boolean thirstEnabled;
		public static boolean dangerousDehydration;
		public static double dehydrationDamageScaling;
		public static double thirstEffectModifier;
		public static double baseThirstExhaustion;
		public static double sprintingThirstExhaustion;
		public static double onJumpThirstExhaustion;
		public static double onBlockBreakThirstExhaustion;
		public static double onAttackThirstExhaustion;
		public static int canteenCapacity;
		public static int largeCanteenCapacity;
		public static boolean allowOverridePurifiedWater;
		public static boolean drinkFromRain;
		public static int hydrationRain;
		public static double saturationRain;
		public static double dirtyRain;
		public static boolean drinkFromWater;
		public static int hydrationWater;
		public static double saturationWater;
		public static double dirtyWater;
		public static int hydrationPotion;
		public static double saturationPotion;
		public static double dirtyPotion;
		public static int hydrationPurified;
		public static double saturationPurified;
		public static double dirtyPurified;
		public static boolean glassBottleLootAfterDrink;

		// Heart fruit
		public static boolean heartFruitsEnabled;
		public static int heartsLostOnDeath;
		public static int maxAdditionalHearts;
		public static int additionalHeartsPerFruit;
		public static boolean heartFruitsGiveRegen;

		// Body members damage
		public static boolean localizedBodyDamageEnabled;
		public static double headCriticalShotMultiplier;
		public static double bodyDamageMultiplier;
		public static double bodyHealthRatioRecoveredFromSleep;
		public static double healthRatioRecoveredFromSleep;

		public static String bodyPartHealthMode;
		public static double headPartHealth;
		public static double armsPartHealth;
		public static double chestPartHealth;
		public static double legsPartHealth;
		public static double feetPartHealth;

		public static double healingHerbsHealingValue;
		public static int healingHerbsHealingTime;
		public static int healingHerbsUseTime;
		public static int healingHerbsHealingCharges;
		public static double plasterHealingValue;
		public static int plasterHealingTime;
		public static int plasterUseTime;
		public static int plasterHealingCharges;
		public static double bandageHealingValue;
		public static int bandageHealingTime;
		public static int bandageUseTime;
		public static int bandageHealingCharges;
		public static double tonicHealingValue;
		public static int tonicHealingTime;
		public static int tonicUseTime;
		public static double medikitHealingValue;
		public static int medikitHealingTime;
		public static int medikitUseTime;

		public static List<String> headPartEffects;
		public static List<Integer> headPartEffectAmplifiers;
		public static List<Float> headPartEffectThresholds;

		public static List<String> armsPartEffects;
		public static List<Integer> armsPartEffectAmplifiers;
		public static List<Float> armsPartEffectThresholds;
		public static List<String> bothArmsPartEffects;
		public static List<Integer> bothArmsPartEffectAmplifiers;
		public static List<Float> bothArmsPartEffectThresholds;

		public static List<String> chestPartEffects;
		public static List<Integer> chestPartEffectAmplifiers;
		public static List<Float> chestPartEffectThresholds;

		public static List<String> legsPartEffects;
		public static List<Integer> legsPartEffectAmplifiers;
		public static List<Float> legsPartEffectThresholds;
		public static List<String> bothLegsPartEffects;
		public static List<Integer> bothLegsPartEffectAmplifiers;
		public static List<Float> bothLegsPartEffectThresholds;

		public static List<String> feetPartEffects;
		public static List<Integer> feetPartEffectAmplifiers;
		public static List<Float> feetPartEffectThresholds;
		public static List<String> bothFeetPartEffects;
		public static List<Integer> bothFeetPartEffectAmplifiers;
		public static List<Float> bothFeetPartEffectThresholds;

		// Client Config
		public static TemperatureDisplayEnum temperatureDisplayMode;
		public static int temperatureDisplayOffsetX;
		public static int temperatureDisplayOffsetY;

		public static int seasonCardsOffsetX;
		public static int seasonCardsOffsetY;
		public static int seasonCardsSpawnDimensionDelayInTicks;
		public static int seasonCardsDisplayTimeInTicks;
		public static int seasonCardsFadeInInTicks;
		public static int seasonCardsFadeOutInTicks;
		
		public static int wetnessIndicatorOffsetX;
		public static int wetnessIndicatorOffsetY;

		public static int bodyDamageIndicatorOffsetX;
		public static int bodyDamageIndicatorOffsetY;
		public static boolean alwaysShowBodyDamageIndicator;

		public static boolean showHydrationTooltip;
		public static boolean mergeHydrationAndSaturationTooltip;
		public static boolean thirstSaturationDisplayed;

		public static void bakeCommon()
		{
			LegendarySurvivalOverhaul.LOGGER.debug("Load Common configuration from file");
			try
			{
				hideInfoFromDebug = COMMON.hideInfoFromDebug.get();
				tickRate = COMMON.tickRate.get();
				minTemperatureModification = COMMON.minTemperatureModification.get();
				maxTemperatureModification = COMMON.maxTemperatureModification.get();
				routinePacketSync = COMMON.routinePacketSync.get();
				baseFoodExhaustion = COMMON.baseFoodExhaustion.get();

				temperatureEnabled = COMMON.temperatureEnabled.get();
				showPotionEffectParticles = COMMON.showPotionEffectParticles.get();

				temperatureResistanceOnDeathEnabled = COMMON.temperatureResistanceOnDeathEnabled.get();
				temperatureResistanceOnDeathTime = COMMON.temperatureResistanceOnDeathTime.get();
				
				dangerousTemperature = COMMON.dangerousTemperature.get();
				temperatureSecondaryEffects = COMMON.temperatureSecondaryEffects.get();
				heatThirstEffectModifier = COMMON.heatThirstEffectModifier.get();
				coldHungerEffectModifier = COMMON.coldHungerEffectModifier.get();

				overworldDefaultTemperature = COMMON.overworldDefaultTemperature.get();
				netherDefaultTemperature = COMMON.netherDefaultTemperature.get();
				endDefaultTemperature = COMMON.endDefaultTemperature.get();

				rainTemperatureModifier = COMMON.rainTemperatureModifier.get();
				snowTemperatureModifier = COMMON.snowTemperatureModifier.get();

				altitudeModifier = COMMON.altitudeModifier.get();
				biomeEffectsEnabled = COMMON.biomeEffectsEnabled.get();
				biomeDrynessEffectEnabled = COMMON.biomeDrynessEffectEnabled.get();
				biomeTemperatureMultiplier = COMMON.biomeTemperatureMultiplier.get();
				timeModifier = COMMON.timeModifier.get();
				biomeTimeMultiplier = COMMON.biomeTimeMultiplier.get();
				shadeTimeModifier = COMMON.shadeTimeModifier.get();

				tempInfluenceMaximumDist = COMMON.tempInfluenceMaximumDist.get();
				tempInfluenceUpDistMultiplier = COMMON.tempInfluenceUpDistMultiplier.get();
				tempInfluenceOutsideDistMultiplier = COMMON.tempInfluenceOutsideDistMultiplier.get();
				
				onFireModifier = COMMON.onFireModifier.get();
				sprintModifier = COMMON.sprintModifier.get();
				enchantmentMultiplier = COMMON.enchantmentMultiplier.get();
				
				wetnessMode = WetnessMode.getDisplayFromString(COMMON.wetnessMode.get());
				wetMultiplier = COMMON.wetMultiplier.get();
				wetnessDecrease = COMMON.wetnessDecrease.get();
				wetnessRainIncrease = COMMON.wetnessRainIncrease.get();
				wetnessFluidIncrease = COMMON.wetnessFluidIncrease.get();
				
				playerHuddlingModifier = COMMON.playerHuddlingModifier.get();
				playerHuddlingRadius = COMMON.playerHuddlingRadius.get();

				heatingCoat1Modifier = COMMON.heatingCoat1Modifier.get();
				heatingCoat2Modifier = COMMON.heatingCoat2Modifier.get();
				heatingCoat3Modifier = COMMON.heatingCoat3Modifier.get();

				coolingCoat1Modifier = COMMON.coolingCoat1Modifier.get();
				coolingCoat2Modifier = COMMON.coolingCoat2Modifier.get();
				coolingCoat3Modifier = COMMON.coolingCoat3Modifier.get();

				thermalCoat1Modifier = COMMON.thermalCoat1Modifier.get();
				thermalCoat2Modifier = COMMON.thermalCoat2Modifier.get();
				thermalCoat3Modifier = COMMON.thermalCoat3Modifier.get();

				seasonTemperatureEffects = COMMON.seasonTemperatureEffects.get();
				tropicalSeasonsEnabled = COMMON.tropicalSeasonsEnabled.get();
				seasonCardsEnabled = COMMON.seasonCardsEnabled.get();
				
				earlySpringModifier = COMMON.earlySpringModifier.get();
				midSpringModifier = COMMON.midSpringModifier.get();
				lateSpringModifier = COMMON.lateSpringModifier.get();

				earlySummerModifier = COMMON.earlySummerModifier.get();
				midSummerModifier = COMMON.midSummerModifier.get();
				lateSummerModifier = COMMON.lateSummerModifier.get();

				earlyAutumnModifier = COMMON.earlyAutumnModifier.get();
				midAutumnModifier = COMMON.midAutumnModifier.get();
				lateAutumnModifier = COMMON.lateAutumnModifier.get();

				earlyWinterModifier = COMMON.earlyWinterModifier.get();
				midWinterModifier = COMMON.midWinterModifier.get();
				lateWinterModifier = COMMON.lateWinterModifier.get();

				earlyWetSeasonModifier = COMMON.earlyWetSeasonModifier.get();
				midWetSeasonModifier = COMMON.midWetSeasonModifier.get();
				lateWetSeasonModifier = COMMON.lateWetSeasonModifier.get();

				earlyDrySeasonModifier = COMMON.earlyDrySeasonModifier.get();
				midDrySeasonModifier = COMMON.midDrySeasonModifier.get();
				lateDrySeasonModifier = COMMON.lateDrySeasonModifier.get();

				sunFernBiomeNames = COMMON.sunFernBiomeNames.get();
				sunFernBiomeCategories = COMMON.sunFernBiomeCategories.get();
				iceFernBiomeNames = COMMON.iceFernBiomeNames.get();
				iceFernBiomeCategories = COMMON.iceFernBiomeCategories.get();
				waterPlantBiomeNames = COMMON.waterPlantBiomeNames.get();
				waterPlantBiomeCategories = COMMON.waterPlantBiomeCategories.get();

				thirstEnabled = COMMON.thirstEnabled.get();
				dangerousDehydration = COMMON.dangerousDehydration.get();
				dehydrationDamageScaling = COMMON.dehydrationDamageScaling.get();
				thirstEffectModifier = COMMON.thirstEffectModifier.get();

				baseThirstExhaustion = COMMON.baseThirstExhaustion.get();
				sprintingThirstExhaustion = COMMON.sprintingThirstExhaustion.get();
				onJumpThirstExhaustion = COMMON.onJumpThirstExhaustion.get();
				onBlockBreakThirstExhaustion = COMMON.onBlockBreakThirstExhaustion.get();
				onAttackThirstExhaustion = COMMON.onAttackThirstExhaustion.get();

				canteenCapacity = COMMON.canteenCapacity.get();
				largeCanteenCapacity = COMMON.largeCanteenCapacity.get();
				allowOverridePurifiedWater = COMMON.allowOverridePurifiedWater.get();

				drinkFromRain = COMMON.drinkFromRain.get();
				hydrationRain = COMMON.hydrationRain.get();
				saturationRain = COMMON.saturationRain.get();
				dirtyRain = COMMON.dirtyRain.get();
				drinkFromWater = COMMON.drinkFromWater.get();
				hydrationWater = COMMON.hydrationWater.get();
				saturationWater = COMMON.saturationWater.get();
				dirtyWater = COMMON.dirtyWater.get();
				hydrationPotion = COMMON.hydrationPotion.get();
				saturationPotion = COMMON.saturationPotion.get();
				dirtyPotion = COMMON.dirtyPotion.get();
				hydrationPurified = COMMON.hydrationPurified.get();
				saturationPurified = COMMON.saturationPurified.get();
				dirtyPurified = COMMON.dirtyPurified.get();
				glassBottleLootAfterDrink = COMMON.glassBottleLootAfterDrink.get();

				heartFruitsEnabled = COMMON.heartFruitsEnabled.get();
				heartsLostOnDeath = COMMON.heartsLostOnDeath.get();
				maxAdditionalHearts = COMMON.maxAdditionalHearts.get();
				additionalHeartsPerFruit = COMMON.additionalHeartsPerFruit.get();
				heartFruitsGiveRegen = COMMON.heartFruitsGiveRegen.get();

				localizedBodyDamageEnabled = COMMON.localizedBodyDamageEnabled.get();
				headCriticalShotMultiplier = COMMON.headCriticalShotMultiplier.get();
				bodyDamageMultiplier = COMMON.bodyDamageMultiplier.get();
				bodyHealthRatioRecoveredFromSleep = COMMON.bodyHealthRatioRecoveredFromSleep.get();
				healthRatioRecoveredFromSleep = COMMON.healthRatioRecoveredFromSleep.get();

				bodyPartHealthMode = COMMON.bodyPartHealthMode.get();
				headPartHealth = COMMON.headPartHealth.get();
				chestPartHealth = COMMON.chestPartHealth.get();
				armsPartHealth = COMMON.armsPartHealth.get();
				legsPartHealth = COMMON.legsPartHealth.get();
				feetPartHealth = COMMON.feetPartHealth.get();

				healingHerbsHealingValue = COMMON.healingHerbsHealingValue.get();
				healingHerbsHealingTime = COMMON.healingHerbsHealingTime.get();
				healingHerbsUseTime = COMMON.healingHerbsUseTime.get();
				healingHerbsHealingCharges = COMMON.healingHerbsHealingCharges.get();
				plasterHealingValue = COMMON.plasterHealingValue.get();
				plasterHealingTime = COMMON.plasterHealingTime.get();
				plasterUseTime = COMMON.plasterUseTime.get();
				plasterHealingCharges = COMMON.plasterHealingCharges.get();
				bandageHealingValue = COMMON.bandageHealingValue.get();
				bandageHealingTime = COMMON.bandageHealingTime.get();
				bandageUseTime = COMMON.bandageUseTime.get();
				bandageHealingCharges = COMMON.bandageHealingCharges.get();
				tonicHealingValue = COMMON.tonicHealingValue.get();
				tonicHealingTime = COMMON.tonicHealingTime.get();
				tonicUseTime = COMMON.tonicUseTime.get();
				medikitHealingValue = COMMON.medikitHealingValue.get();
				medikitHealingTime = COMMON.medikitHealingTime.get();
				medikitUseTime = COMMON.medikitUseTime.get();

				headPartEffects = COMMON.headPartEffects.get();
				headPartEffectAmplifiers = COMMON.headPartEffectAmplifiers.get();
				headPartEffectThresholds = COMMON.headPartEffectThresholds.get();
				armsPartEffects = COMMON.armsPartEffects.get();
				armsPartEffectAmplifiers = COMMON.armsPartEffectAmplifiers.get();
				armsPartEffectThresholds = COMMON.armsPartEffectThresholds.get();
				bothArmsPartEffects = COMMON.bothArmsPartEffects.get();
				bothArmsPartEffectAmplifiers = COMMON.bothArmsPartEffectAmplifiers.get();
				bothArmsPartEffectThresholds = COMMON.bothArmsPartEffectThresholds.get();
				chestPartEffects = COMMON.chestPartEffects.get();
				chestPartEffectAmplifiers = COMMON.chestPartEffectAmplifiers.get();
				chestPartEffectThresholds = COMMON.chestPartEffectThresholds.get();
				legsPartEffects = COMMON.legsPartEffects.get();
				legsPartEffectAmplifiers = COMMON.legsPartEffectAmplifiers.get();
				legsPartEffectThresholds = COMMON.legsPartEffectThresholds.get();
				bothLegsPartEffects = COMMON.bothLegsPartEffects.get();
				bothLegsPartEffectAmplifiers = COMMON.bothLegsPartEffectAmplifiers.get();
				bothLegsPartEffectThresholds = COMMON.bothLegsPartEffectThresholds.get();
				feetPartEffects = COMMON.feetPartEffects.get();
				feetPartEffectAmplifiers = COMMON.feetPartEffectAmplifiers.get();
				feetPartEffectThresholds = COMMON.feetPartEffectThresholds.get();
				bothFeetPartEffects = COMMON.bothFeetPartEffects.get();
				bothFeetPartEffectAmplifiers = COMMON.bothFeetPartEffectAmplifiers.get();
				bothFeetPartEffectThresholds = COMMON.bothFeetPartEffectThresholds.get();
			}
			catch (Exception e)
			{
				LegendarySurvivalOverhaul.LOGGER.warn("An exception was caused trying to load the common config for Legendary Survival Overhaul");
				e.printStackTrace();
			}
		}
		
		public static void bakeClient()
		{
			LegendarySurvivalOverhaul.LOGGER.debug("Load Client configuration from file");
			try
			{
				temperatureDisplayMode = TemperatureDisplayEnum.getDisplayFromString(CLIENT.temperatureDisplayMode.get());
				temperatureDisplayOffsetX = CLIENT.temperatureDisplayOffsetX.get();
				temperatureDisplayOffsetY = CLIENT.temperatureDisplayOffsetY.get();
				showVanillaAnimationOverlay = CLIENT.showVanillaAnimationOverlay.get();

				seasonCardsOffsetX = CLIENT.seasonCardsOffsetX.get();
				seasonCardsOffsetY = CLIENT.seasonCardsOffsetY.get();
				seasonCardsSpawnDimensionDelayInTicks = CLIENT.seasonCardsSpawnDimensionDelayInTicks.get();
				seasonCardsDisplayTimeInTicks = CLIENT.seasonCardsDisplayTimeInTicks.get();
				seasonCardsFadeInInTicks = CLIENT.seasonCardsFadeInInTicks.get();
				seasonCardsFadeOutInTicks = CLIENT.seasonCardsFadeOutInTicks.get();
				
				wetnessIndicatorOffsetX = CLIENT.wetnessIndicatorOffsetX.get();
				wetnessIndicatorOffsetY = CLIENT.wetnessIndicatorOffsetY.get();

				bodyDamageIndicatorOffsetX = CLIENT.bodyDamageIndicatorOffsetX.get();
				bodyDamageIndicatorOffsetY = CLIENT.bodyDamageIndicatorOffsetY.get();
				alwaysShowBodyDamageIndicator = CLIENT.alwaysShowBodyDamageIndicator.get();

				foodSaturationDisplayed = CLIENT.foodSaturationDisplayed.get();
				thirstSaturationDisplayed = CLIENT.thirstSaturationDisplayed.get();
				showHydrationTooltip = CLIENT.showHydrationTooltip.get();
				mergeHydrationAndSaturationTooltip = CLIENT.mergeHydrationAndSaturationTooltip.get();
			}
			catch (Exception e)
			{
				LegendarySurvivalOverhaul.LOGGER.warn("An exception was caused trying to load the client config for Legendary Survival Overhaul.");
				e.printStackTrace();
			}
		}
	}
}
