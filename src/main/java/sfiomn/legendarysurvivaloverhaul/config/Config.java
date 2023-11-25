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
		public final ForgeConfigSpec.ConfigValue<Integer> minTickRate;
		public final ForgeConfigSpec.ConfigValue<Integer> maxTickRate;
		public final ForgeConfigSpec.ConfigValue<Integer> routinePacketSync;
		public final ForgeConfigSpec.ConfigValue<Boolean> hideInfoFromDebug;
		public final ForgeConfigSpec.ConfigValue<Float> baseFoodExhaustion;
		
		// Temperature
		public final ForgeConfigSpec.ConfigValue<Boolean> temperatureEnabled;
		public final ForgeConfigSpec.ConfigValue<Boolean> showPotionEffectParticles;
		public final ForgeConfigSpec.ConfigValue<Boolean> dangerousTemperature;
		public final ForgeConfigSpec.ConfigValue<Boolean> temperatureSecondaryEffects;
		public final ForgeConfigSpec.ConfigValue<Float> heatThirstEffectModifier;
		public final ForgeConfigSpec.ConfigValue<Float> coldHungerEffectModifier;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> biomeEffectsEnabled;
		public final ForgeConfigSpec.ConfigValue<Boolean> biomeDrynessEffectEnabled;
		public final ForgeConfigSpec.ConfigValue<Double> biomeTemperatureMultiplier;
		
		public final ForgeConfigSpec.ConfigValue<Double> timeMultiplier;
		public final ForgeConfigSpec.ConfigValue<Double> biomeTimeMultiplier;
		public final ForgeConfigSpec.ConfigValue<Integer> shadeTimeMultiplier;

		public final ForgeConfigSpec.ConfigValue<Double> altitudeModifier;
		public final ForgeConfigSpec.ConfigValue<Double> sprintModifier;
		public final ForgeConfigSpec.ConfigValue<Double> onFireModifier;
		public final ForgeConfigSpec.ConfigValue<Double> enchantmentMultiplier;
		
		public final ForgeConfigSpec.ConfigValue<String> wetnessMode;
		public final ForgeConfigSpec.ConfigValue<Double> wetMultiplier;

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
		public final ForgeConfigSpec.ConfigValue<Boolean> dangerousThirst;
		public final ForgeConfigSpec.ConfigValue<Float> thirstDamageScaling;
		public final ForgeConfigSpec.ConfigValue<Float> thirstEffectModifier;
		public final ForgeConfigSpec.ConfigValue<Float> baseThirstExhaustion;
		public final ForgeConfigSpec.ConfigValue<Float> sprintingThirstExhaustion;
		public final ForgeConfigSpec.ConfigValue<Float> onJumpThirstExhaustion;
		public final ForgeConfigSpec.ConfigValue<Float> onBlockBreakThirstExhaustion;
		public final ForgeConfigSpec.ConfigValue<Float> onAttackThirstExhaustion;
		public final ForgeConfigSpec.ConfigValue<Integer> canteenCapacity;
		public final ForgeConfigSpec.ConfigValue<Integer> largeCanteenCapacity;
		public final ForgeConfigSpec.ConfigValue<Boolean> allowOverridePurifiedWater;
		public final ForgeConfigSpec.ConfigValue<Boolean> drinkFromRain;
		public final ForgeConfigSpec.ConfigValue<Integer> hydrationRain;
		public final ForgeConfigSpec.ConfigValue<Float> saturationRain;
		public final ForgeConfigSpec.ConfigValue<Float> dirtyRain;
		public final ForgeConfigSpec.ConfigValue<Boolean> drinkFromWater;
		public final ForgeConfigSpec.ConfigValue<Integer> hydrationWater;
		public final ForgeConfigSpec.ConfigValue<Float> saturationWater;
		public final ForgeConfigSpec.ConfigValue<Float> dirtyWater;
		public final ForgeConfigSpec.ConfigValue<Integer> hydrationPotion;
		public final ForgeConfigSpec.ConfigValue<Float> saturationPotion;
		public final ForgeConfigSpec.ConfigValue<Float> dirtyPotion;
		public final ForgeConfigSpec.ConfigValue<Integer> hydrationPurified;
		public final ForgeConfigSpec.ConfigValue<Float> saturationPurified;
		public final ForgeConfigSpec.ConfigValue<Float> dirtyPurified;
		public final ForgeConfigSpec.ConfigValue<Boolean> glassBottleLootAfterDrink;


		// Heart Fruits
		public final ForgeConfigSpec.ConfigValue<Boolean> heartFruitsEnabled;
		
		public final ForgeConfigSpec.ConfigValue<Integer> heartsLostOnDeath;
		public final ForgeConfigSpec.ConfigValue<Integer> maxAdditionalHearts;
		
		public final ForgeConfigSpec.ConfigValue<Integer> additionalHeartsPerFruit;
		public final ForgeConfigSpec.ConfigValue<Boolean> heartFruitsGiveRegen;

		
		
		Common(ForgeConfigSpec.Builder builder)
		{
			builder.comment(new String [] {
						" Options related to enabling/disabling specific features",
						" See the jsons folder to customize the temperature of specific blocks, liquids, armors, etc.",
						" To reload your JSONs, type /reload into chat with cheats enabled; The same way you reload datapacks, crafttweaker scripts, etc."
					}).push("core");
			temperatureEnabled = builder
					.comment(" Whether or not the temperature system is enabled.")
					.define("Temperature Enabled", true);
			thirstEnabled = builder
					.comment(" Whether or not the thirst system is enabled.")
					.define("Thirst Enabled", true);
			heartFruitsEnabled = builder
					.comment(" Whether or not heart fruits are functional and generate in-world.")
					.define("Heart Fruits Enabled", true);
			hideInfoFromDebug = builder
					.comment(" Id enabled, information like position will be hidden from the debug screen (F3).")
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
					.define("Base Food Exhaustion", 0.03f);
			builder.pop();
			
			builder.comment(" Options related to the temperature system").push("temperature");
			dangerousTemperature = builder
					.comment(" If enabled, players will take damage from the effects of temperature.")
					.define("Dangerous Temperature Effects", true);
			builder.push("secondary_effet");
			temperatureSecondaryEffects = builder
					.comment(" If enabled, players will also receive other effects from their current temperature state.",
							" If the player is too hot, hydration will deplete faster. If the player is too cold, hunger will deplete faster.")
					.define("Secondary Temperature Effects", true);
			heatThirstEffectModifier = builder
					.comment(" How much thirst exhaustion will be added every 50 ticks with no amplification effect.")
					.define("Heat Thirst Effect Modifier", 0.1f);
			coldHungerEffectModifier = builder
					.comment(" How much food exhaustion will be added every 50 ticks with no amplification effect.",
							" As reference, the hunger effect add 0.025 food exhaustion every 50 ticks.")
					.define("Cold Hunger Modifier", 0.05f);
			builder.pop();
			onFireModifier = builder
					.comment(" How much of an effect being on fire has on a player's temperature.")
					.define("Player On Fire Modifier", 12.5d);
			sprintModifier = builder
					.comment(" How much of an effect sprinting has on a player's temperature.")
					.define("Player Sprint Modifier", 1.5d);
			enchantmentMultiplier = builder
					.comment(" Increases/decreases the effect that cooling/heating enchantments have on a player's temperature.")
					.define("Enchantment Modifier", 1.0d);
			showPotionEffectParticles = builder
					.comment(" If enabled, players will see particles on them when temperature resistance effect active.\n" +
							 " If disabled, the potion color will turn black due to forge weird behavior.")
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
					.define("Wetness Modifier", -7.0d);
			builder.pop();
			
			builder.push("huddling");
			playerHuddlingModifier = builder
					.comment(" How much nearby players increase the ambient temperature by.", " Note that this value stacks!")
					.define("Player Huddling Modifier", 0.5d);
			playerHuddlingRadius = builder
					.comment(" The radius, in blocks, around which players will add to each other's temperature.")
					.defineInRange("Player Huddling Radius", 1, 0, 10);
			builder.pop();
			
			builder.push("environment");

			builder.push("flowers");
			sunFernBiomeNames = builder.comment(" In which biome names the Sun Fern will spawn").define("Sun Fern Biome Names Spawn List", new ArrayList<>());
			sunFernBiomeCategories = builder.comment(" In which biome categories the Sun Fern will spawn").define("Sun Fern Biome Categories Spawn List", Arrays.asList("DESERT", "SAVANNA"));
			iceFernBiomeNames = builder.comment(" In which biome names the Ice Fern will spawn").define("Ice Fern Biome Names Spawn List", new ArrayList<>());
			iceFernBiomeCategories = builder.comment(" In which biome categories the Ice Fern will spawn").define("Ice Fern Biome Categories Spawn List", Arrays.asList("TAIGA", "ICY"));
			waterPlantBiomeNames = builder.comment(" In which biome names the Water Plant will spawn").define("Water Plant Biome Names Spawn List", new ArrayList<>());
			waterPlantBiomeCategories = builder.comment(" In which biome categories the Water Plant will spawn").define("Water Plant Biome Categories Spawn List", Collections.singletonList("DESERT"));
			builder.pop();

			altitudeModifier = builder
					.comment(" How much the effects of the player's altitude on temperature are multiplied.")
					.define("Altitude Modifier", 3.0d);
			builder.push("biomes");
			biomeTemperatureMultiplier = builder
					.comment(" How much a biome's temperature effects are multiplied.")
					.defineInRange("Biome Temperature Multiplier", 16.0d, 0.0d, Double.POSITIVE_INFINITY);
			biomeEffectsEnabled = builder
					.comment(" Whether or not biomes will have an effect on a player's temperature.")
					.define("Biomes affect Temperature", true);
			biomeDrynessEffectEnabled = builder
					.comment(" Whether or not hot biome's dryness will make days really hot and nights really cold.")
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
			builder.push("multipliers");
			timeMultiplier = builder
					.comment(" How strongly the effects of time on temperature are multiplied.", " Maximum effect at noon and midnight")
					.defineInRange("Time Multiplier", 2.0d, 0.0d, Double.POSITIVE_INFINITY);
			biomeTimeMultiplier = builder
					.comment(" How strongly different biomes affect temperature, based on time.")
					.defineInRange("Biome Time Multiplier", 1.75d, 1.0d, Double.POSITIVE_INFINITY);
			builder.pop();
			shadeTimeMultiplier = builder
					.comment(" Staying in the shade or during cloudy weather will reduce player's temperature by this amount based on time of the day.", " Only effective in hot biomes and during day time!")
					.define("Shade Time Multiplier", 3);
			builder.pop();
			builder.pop();

			builder.comment(" Temperature coat adds temperature effects on armors by using the sewing table.").push("coat");
			builder.comment(" Add an adaptive heating effect on armors.").push("heating");

			heatingCoat1Modifier = builder.define("Heating Coat I", 1.0d);
			heatingCoat2Modifier = builder.define("Heating Coat II", 2.0d);
			heatingCoat3Modifier = builder.define("Heating Coat III", 3.0d);

			builder.pop();
			builder.comment(" Add an adaptive cooling effect on armors.").push("cooling");

			coolingCoat1Modifier = builder.define("Cooling Coat I", 1.0d);
			coolingCoat2Modifier = builder.define("Cooling Coat II", 2.0d);
			coolingCoat3Modifier = builder.define("Cooling Coat III", 3.0d);

			builder.pop();
			builder.comment(" Add an adaptive temperature effect on armors that can both heat and cool the player.").push("thermal");

			thermalCoat1Modifier = builder.define("Thermal Coat I", 1.0d);
			thermalCoat2Modifier = builder.define("Thermal Coat II", 2.0d);
			thermalCoat3Modifier = builder.define("Thermal Coat III", 3.0d);

			builder.pop();
			builder.pop();
			
			builder.push("advanced");
			tempInfluenceMaximumDist = builder
					.comment(" Maximum distance, in blocks, where thermal sources will have an effect on temperature.")
					.defineInRange("Temperature Influence Maximum Distance", 20, 1, 40);
			tempInfluenceUpDistMultiplier = builder
					.comment(" How strongly distance above the player is reduced where thermal sources will have an effect on temperature.")
					.comment(" Example max dist is 10, up mult is 0.75 -> max distance is 10 * 0.75 above the player.")
					.defineInRange("Temperature Influence Up Distance Multiplier", 0.85, 0.0, 1.0);
			tempInfluenceOutsideDistMultiplier = builder
					.comment(" How strongly distance outside a structure is reduced where thermal sources will have an effect on temperature.")
					.defineInRange("Temperature Influence Outside Distance Multiplier", 0.75, 0.0, 1.0);
			builder.push("tickrate");
			maxTickRate = builder
					.comment(" Maximum amount of time between temperature ticks.")
					.defineInRange("Maximum Temperature Tickrate", 200, 20, Integer.MAX_VALUE);
			minTickRate = builder
					.comment(" Minimum amount of time between temperature ticks.")
					.defineInRange("Minimum Temperature Tickrate", 20, 20, Integer.MAX_VALUE);
			builder.pop();
			builder.pop();
			
			builder.push("integration");
			
			builder.push("seasons");
			seasonTemperatureEffects = builder
					.comment(" If Serene Seasons is installed, then seasons", " will have an effect on the player's temperature.")
					.define("Seasons affect Temperature", true);
			
			builder.comment("Temperature modifiers per season in temperate biomes.").push("temperate");
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
			
			builder.comment("Temperature modifiers per season in tropical biomes.").push("tropical");
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

			builder.comment(" Options related to thirst").push("thirst");
			dangerousThirst = builder
					.comment(" If enabled, players will take damage from the effects of thirst.")
					.define("Dangerous Thirst Effect", true);
			builder.push("exhaustion");
			baseThirstExhaustion = builder
					.comment(" Thirst exhausted every 10 ticks.")
					.define("Base Thirst Exhaustion", 0.03f);
			sprintingThirstExhaustion = builder
					.comment(" Thirst exhausted when sprinting, replacing the base thirst exhausted.")
					.define("Sprinting Thirst Exhaustion", 0.15f);
			onJumpThirstExhaustion = builder
					.comment(" Thirst exhausted on every jump.")
					.define("On Jump Thirst Exhaustion", 0.3f);
			onBlockBreakThirstExhaustion = builder
					.comment(" Thirst exhausted on every block break.")
					.define("On Block Break Thirst Exhaustion", 0.1f);
			onAttackThirstExhaustion = builder
					.comment(" Thirst exhausted on every attack.")
					.define("On Attack Thirst Exhaustion", 0.5f);
			builder.pop();
			thirstDamageScaling = builder
					.comment(" Scaling of the damages dealt when the thirst falls at 0. Each tick damage will be increased by this value.")
					.define("Thirst Damage Scaling", 0.3f);
			thirstEffectModifier = builder
					.comment(" How many thirst exhaustion will be added every 50 ticks when the player suffers from not amplified Thirst Effect.",
							" The player will suffer not amplified Thirst Effect from water dirtiness by example.")
					.define("Thirst Effect Modifier", 0.25f);
			builder.push("canteen");
			canteenCapacity = builder
					.comment(" Capacity of the canteen used to store water.")
					.define("Canteen Capacity", 5);
			largeCanteenCapacity = builder
					.comment(" Capacity of the large canteen used to store water.")
					.define("Large Canteen Capacity", 10);
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
					.define("Hydration", 1);
			saturationRain = builder
					.comment(" Amount of saturation recovered when drinking from the rain.")
					.define("Saturation", 0.0f);
			dirtyRain = builder
					.comment(" Chance of getting a thirst effect while drinking from the rain.")
					.define("Dirty", 0.0f);
			builder.pop();
			builder.push("water");
			drinkFromWater = builder
					.comment(" Whether players can drink from water (or flowing water) block or not.")
					.define("Drink From Water", true);
			hydrationWater = builder
					.comment(" Amount of hydration recovered while drinking water.")
					.define("Hydration", 3);
			saturationWater = builder
					.comment(" Amount of saturation recovered while drinking water.")
					.define("Saturation", 0.0f);
			dirtyWater = builder
					.comment(" Chance of getting a thirst effect while drinking water.")
					.define("Dirty", 0.75f);
			builder.pop();
			builder.comment(" Amount recovered by potions with effects").push("potion");
			hydrationPotion = builder
					.comment(" Amount of hydration recovered while drinking a potion.")
					.define("Hydration", 3);
			saturationPotion = builder
					.comment(" Amount of saturation recovered while drinking a potion.")
					.define("Saturation", 0.3f);
			dirtyPotion = builder
					.comment(" Chance of getting a thirst effect while drinking a potion.")
					.define("Dirty", 0.0f);
			builder.pop();
			builder.push("purified_water");
			hydrationPurified = builder
					.comment(" Amount of hydration recovered while drinking purified water.")
					.define("Hydration", 6);
			saturationPurified = builder
					.comment(" Amount of saturation recovered while drinking purified water.")
					.define("Saturation", 1.5f);
			dirtyPurified = builder
					.comment(" Chance of getting a thirst effect while drinking purified water.")
					.define("Dirty", 0.0f);
			builder.pop();
			builder.push("juices");
			glassBottleLootAfterDrink = builder
					.comment(" Whether or not the player retrieves a glass bottle after drinking a juice.")
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
					.comment(" Whether or not Heart Fruits give a strong regeneration effect.")
					.define("Heart Fruits Give Regen", true);
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
					.comment(" Whether or not the vanilla animation of the Food bar and Hydration bar is rendered. The bar shakes more the lower they are.",
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
			
			builder.comment(" The X and Y offset of the wetness indicator. Set both to 0 for no offset.").push("offset");
			wetnessIndicatorOffsetX = builder
					.define("Wetness Indicator X Offset", 0);
			wetnessIndicatorOffsetY = builder
					.define("Wetness Indicator Y Offset", 0);
			builder.pop();
			builder.pop();
			builder.pop();
			builder.push("thirst");
			showHydrationTooltip = builder
					.comment(" If enabled, show the hydration values in the item tooltip.")
					.define("Show Hydration Tooltip", true);
			mergeHydrationAndSaturationTooltip = builder
					.comment(" If enabled, show the hydration and the saturation values on the same line in the tooltip.")
					.define("Merge Hydration And Saturation Tooltip", false);
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
		public static int minTickRate;
		public static int maxTickRate;
		public static int routinePacketSync;
		public static boolean hideInfoFromDebug;
		public static float baseFoodExhaustion;


		// Temperature
		public static boolean temperatureEnabled;
		public static boolean showPotionEffectParticles;

		public static boolean dangerousTemperature;
		public static boolean temperatureSecondaryEffects;
		public static float heatThirstEffectModifier;
		public static float coldHungerEffectModifier;
		public static boolean foodSaturationDisplayed;
		public static boolean showVanillaAnimationOverlay;
		
		public static boolean biomeEffectsEnabled;
		public static boolean biomeDrynessEffectEnabled;
		public static double biomeTemperatureMultiplier;
		
		public static double rainTemperatureModifier;
		public static double snowTemperatureModifier;
		
		public static double altitudeModifier;

		public static List<String> sunFernBiomeNames;
		public static List<String> sunFernBiomeCategories;
		public static List<String> iceFernBiomeNames;
		public static List<String> iceFernBiomeCategories;
		public static List<String> waterPlantBiomeNames;
		public static List<String> waterPlantBiomeCategories;
		
		public static boolean seasonTemperatureEffects;
		
		public static double timeMultiplier;
		public static double biomeTimeMultiplier;
		public static int shadeTimeMultiplier;
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

		// Thirst
		public static boolean thirstEnabled;
		public static boolean dangerousThirst;
		public static float thirstDamageScaling;
		public static float thirstEffectModifier;
		public static boolean showHydrationTooltip;
		public static boolean mergeHydrationAndSaturationTooltip;
		public static float baseThirstExhaustion;
		public static float sprintingThirstExhaustion;
		public static float onJumpThirstExhaustion;
		public static float onBlockBreakThirstExhaustion;
		public static float onAttackThirstExhaustion;
		public static int canteenCapacity;
		public static int largeCanteenCapacity;
		public static boolean allowOverridePurifiedWater;
		public static boolean drinkFromRain;
		public static int hydrationRain;
		public static float saturationRain;
		public static float dirtyRain;
		public static boolean drinkFromWater;
		public static int hydrationWater;
		public static float saturationWater;
		public static float dirtyWater;
		public static int hydrationPotion;
		public static float saturationPotion;
		public static float dirtyPotion;
		public static int hydrationPurified;
		public static float saturationPurified;
		public static float dirtyPurified;
		public static boolean glassBottleLootAfterDrink;


		// Heart fruit
		public static boolean heartFruitsEnabled;
		public static int heartsLostOnDeath;
		public static int maxAdditionalHearts;
		public static int additionalHeartsPerFruit;
		public static boolean heartFruitsGiveRegen;

		
		// Client Config
		public static TemperatureDisplayEnum temperatureDisplayMode;
		public static int temperatureDisplayOffsetX;
		public static int temperatureDisplayOffsetY;
		
		public static int wetnessIndicatorOffsetX;
		public static int wetnessIndicatorOffsetY;

		public static boolean thirstSaturationDisplayed;
		public static void bakeCommon()
		{
			LegendarySurvivalOverhaul.LOGGER.debug("Load Common configuration from file");
			try
			{
				hideInfoFromDebug = COMMON.hideInfoFromDebug.get();
				minTickRate = COMMON.minTickRate.get();
				maxTickRate = COMMON.maxTickRate.get();
				routinePacketSync = COMMON.routinePacketSync.get();
				baseFoodExhaustion = COMMON.baseFoodExhaustion.get();

				temperatureEnabled = COMMON.temperatureEnabled.get();
				showPotionEffectParticles = COMMON.showPotionEffectParticles.get();
				
				dangerousTemperature = COMMON.dangerousTemperature.get();
				temperatureSecondaryEffects = COMMON.temperatureSecondaryEffects.get();
				heatThirstEffectModifier = COMMON.heatThirstEffectModifier.get();
				coldHungerEffectModifier = COMMON.coldHungerEffectModifier.get();
				
				altitudeModifier = COMMON.altitudeModifier.get();

				sunFernBiomeNames = COMMON.sunFernBiomeNames.get();
				sunFernBiomeCategories = COMMON.sunFernBiomeCategories.get();
				iceFernBiomeNames = COMMON.iceFernBiomeNames.get();
				iceFernBiomeCategories = COMMON.iceFernBiomeCategories.get();
				waterPlantBiomeNames = COMMON.waterPlantBiomeNames.get();
				waterPlantBiomeCategories = COMMON.waterPlantBiomeCategories.get();

				rainTemperatureModifier = COMMON.rainTemperatureModifier.get();
				snowTemperatureModifier = COMMON.snowTemperatureModifier.get();
				
				biomeEffectsEnabled = COMMON.biomeEffectsEnabled.get();
				biomeDrynessEffectEnabled = COMMON.biomeDrynessEffectEnabled.get();
				biomeTemperatureMultiplier = COMMON.biomeTemperatureMultiplier.get();
				timeMultiplier = COMMON.timeMultiplier.get();
				biomeTimeMultiplier = COMMON.biomeTimeMultiplier.get();
				shadeTimeMultiplier = COMMON.shadeTimeMultiplier.get();

				tempInfluenceMaximumDist = COMMON.tempInfluenceMaximumDist.get();
				tempInfluenceUpDistMultiplier = COMMON.tempInfluenceUpDistMultiplier.get();
				tempInfluenceOutsideDistMultiplier = COMMON.tempInfluenceOutsideDistMultiplier.get();
				
				onFireModifier = COMMON.onFireModifier.get();
				sprintModifier = COMMON.sprintModifier.get();
				enchantmentMultiplier = COMMON.enchantmentMultiplier.get();
				
				wetnessMode = WetnessMode.getDisplayFromString(COMMON.wetnessMode.get());
				wetMultiplier = COMMON.wetMultiplier.get();
				
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

				thirstEnabled = COMMON.thirstEnabled.get();
				dangerousThirst = COMMON.dangerousThirst.get();
				thirstDamageScaling = COMMON.thirstDamageScaling.get();
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

			}
			catch (Exception e)
			{
				LegendarySurvivalOverhaul.LOGGER.warn("An exception was caused trying to load the common config for Survival Overhaul");
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
				
				wetnessIndicatorOffsetX = CLIENT.wetnessIndicatorOffsetX.get();
				wetnessIndicatorOffsetY = CLIENT.wetnessIndicatorOffsetY.get();

				foodSaturationDisplayed = CLIENT.foodSaturationDisplayed.get();
				thirstSaturationDisplayed = CLIENT.thirstSaturationDisplayed.get();
				showHydrationTooltip = CLIENT.showHydrationTooltip.get();
				mergeHydrationAndSaturationTooltip = CLIENT.mergeHydrationAndSaturationTooltip.get();
			}
			catch (Exception e)
			{
				LegendarySurvivalOverhaul.LOGGER.warn("An exception was caused trying to load the client config for Survival Overhaul.");
				e.printStackTrace();
			}
		}
	}
}
