package sfiomn.legendarysurvivaloverhaul.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.json_old.JsonConfigRegistration;
import sfiomn.legendarysurvivaloverhaul.util.EnumUtil;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
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
		for (Path configPath: new Path[]{LegendarySurvivalOverhaul.modConfigPath}) {
			try {
				Files.createDirectory(configPath);
			} catch (FileAlreadyExistsException ignored) {
			} catch (IOException e) {
				LegendarySurvivalOverhaul.LOGGER.error("Failed to create Legendary Survival Overhaul config directory " + configPath);
				LegendarySurvivalOverhaul.LOGGER.error(e.getStackTrace());
			}
		}

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC, LegendarySurvivalOverhaul.MOD_ID + "/" + LegendarySurvivalOverhaul.MOD_ID +"-client.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC, LegendarySurvivalOverhaul.MOD_ID + "/" + LegendarySurvivalOverhaul.MOD_ID +"-common.toml");

		JsonConfigRegistration.init(LegendarySurvivalOverhaul.modConfigJsons.toFile());
	}

	public static class Common
	{
		// Core/Advanced
		public final ForgeConfigSpec.IntValue routinePacketSync;
		public final ForgeConfigSpec.BooleanValue hideInfoFromDebug;
		public final ForgeConfigSpec.BooleanValue naturalRegenerationEnabled;
		public final ForgeConfigSpec.BooleanValue vanillaFreezeEnabled;
		public final ForgeConfigSpec.EnumValue<EnumUtil.CompassInfo> compassInfoMode;
		public final ForgeConfigSpec.BooleanValue showCoordinateOnMap;
		public final ForgeConfigSpec.DoubleValue initialHealth;

		// Food
		public final ForgeConfigSpec.DoubleValue baseFoodExhaustion;
		public final ForgeConfigSpec.DoubleValue sprintingFoodExhaustion;
		public final ForgeConfigSpec.DoubleValue onAttackFoodExhaustion;

		// Temperature
		public final ForgeConfigSpec.BooleanValue temperatureEnabled;
		public final ForgeConfigSpec.EnumValue<EnumUtil.DifficultyMode> difficultyMode;
		public final ForgeConfigSpec.IntValue tempTickTime;
		public final ForgeConfigSpec.DoubleValue minTemperatureModification;
		public final ForgeConfigSpec.DoubleValue maxTemperatureModification;
		public final ForgeConfigSpec.BooleanValue dangerousHeatTemperature;
		public final ForgeConfigSpec.BooleanValue dangerousColdTemperature;
		public final ForgeConfigSpec.DoubleValue goldFernChance;
		public final ForgeConfigSpec.BooleanValue temperatureImmunityOnDeathEnabled;
		public final ForgeConfigSpec.IntValue temperatureImmunityOnDeathTime;
		public final ForgeConfigSpec.BooleanValue temperatureImmunityOnFirstSpawnEnabled;
		public final ForgeConfigSpec.IntValue temperatureImmunityOnFirstSpawnTime;

		public final ForgeConfigSpec.BooleanValue heatTemperatureSecondaryEffects;
		public final ForgeConfigSpec.BooleanValue coldTemperatureSecondaryEffects;
		public final ForgeConfigSpec.DoubleValue heatThirstEffectModifier;
		public final ForgeConfigSpec.DoubleValue coldHungerEffectModifier;

		public final ForgeConfigSpec.BooleanValue biomeEffectsEnabled;
		public final ForgeConfigSpec.DoubleValue biomeDrynessMultiplier;
		public final ForgeConfigSpec.DoubleValue biomeTemperatureMultiplier;

		public final ForgeConfigSpec.DoubleValue timeModifier;
		public final ForgeConfigSpec.DoubleValue biomeTimeMultiplier;
		public final ForgeConfigSpec.DoubleValue shadeTimeModifier;
		public final ForgeConfigSpec.DoubleValue shadeTimeModifierThreshold;

		public final ForgeConfigSpec.DoubleValue altitudeModifier;
		public final ForgeConfigSpec.DoubleValue sprintModifier;
		public final ForgeConfigSpec.DoubleValue onFireModifier;

		public final ForgeConfigSpec.BooleanValue wetnessEnabled;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> wetnessImmunityMounts;
		public final ForgeConfigSpec.DoubleValue wetMultiplier;
		public final ForgeConfigSpec.IntValue wetnessTickTimer;
		public final ForgeConfigSpec.IntValue wetnessDecrease;
		public final ForgeConfigSpec.IntValue wetnessRainIncrease;
		public final ForgeConfigSpec.IntValue wetnessFluidIncrease;

		public final ForgeConfigSpec.IntValue tempInfluenceMaximumDist;
		public final ForgeConfigSpec.DoubleValue tempInfluenceUpDistMultiplier;
		public final ForgeConfigSpec.DoubleValue tempInfluenceInWaterDistMultiplier;
		public final ForgeConfigSpec.DoubleValue tempInfluenceOutsideDistMultiplier;

		public final ForgeConfigSpec.DoubleValue undergroundBiomeTemperatureMultiplier;
		public final ForgeConfigSpec.IntValue undergroundEffectStartDistanceToWS;
		public final ForgeConfigSpec.IntValue undergroundEffectEndDistanceToWS;

		public final ForgeConfigSpec.DoubleValue rainTemperatureModifier;
		public final ForgeConfigSpec.DoubleValue snowTemperatureModifier;

		public final ForgeConfigSpec.DoubleValue maxFreezeTemperatureModifier;
		public final ForgeConfigSpec.IntValue maxFreezeEffectTick;

		public final ForgeConfigSpec.DoubleValue playerHuddlingModifier;
		public final ForgeConfigSpec.IntValue playerHuddlingRadius;

		public final ForgeConfigSpec.DoubleValue heatingCoat1Modifier;
		public final ForgeConfigSpec.DoubleValue heatingCoat2Modifier;
		public final ForgeConfigSpec.DoubleValue heatingCoat3Modifier;

		public final ForgeConfigSpec.DoubleValue coolingCoat1Modifier;
		public final ForgeConfigSpec.DoubleValue coolingCoat2Modifier;
		public final ForgeConfigSpec.DoubleValue coolingCoat3Modifier;

		public final ForgeConfigSpec.DoubleValue thermalCoat1Modifier;
		public final ForgeConfigSpec.DoubleValue thermalCoat2Modifier;
		public final ForgeConfigSpec.DoubleValue thermalCoat3Modifier;

		public final ForgeConfigSpec.DoubleValue tfcItemHeatMultiplier;
		public final ForgeConfigSpec.DoubleValue tfcTemperatureMultiplier;
		
		// > Temperature Origins Integration
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> originsWithWetnessImmunity;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> originsWithHighAltitudeImmunity;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> originsWithOnFireImmunity;

		public final ForgeConfigSpec.BooleanValue sereneSeasonsEnabled;
		public final ForgeConfigSpec.BooleanValue ssTropicalSeasonsEnabled;
		public final ForgeConfigSpec.BooleanValue ssSeasonCardsEnabled;
		public final ForgeConfigSpec.BooleanValue ssDefaultSeasonEnabled;

		public final ForgeConfigSpec.DoubleValue ssEarlySpringModifier;
		public final ForgeConfigSpec.DoubleValue ssMidSpringModifier;
		public final ForgeConfigSpec.DoubleValue ssLateSpringModifier;

		public final ForgeConfigSpec.DoubleValue ssEarlySummerModifier;
		public final ForgeConfigSpec.DoubleValue ssMidSummerModifier;
		public final ForgeConfigSpec.DoubleValue ssLateSummerModifier;

		public final ForgeConfigSpec.DoubleValue ssEarlyAutumnModifier;
		public final ForgeConfigSpec.DoubleValue ssMidAutumnModifier;
		public final ForgeConfigSpec.DoubleValue ssLateAutumnModifier;

		public final ForgeConfigSpec.DoubleValue ssEarlyWinterModifier;
		public final ForgeConfigSpec.DoubleValue ssMidWinterModifier;
		public final ForgeConfigSpec.DoubleValue ssLateWinterModifier;

		public final ForgeConfigSpec.DoubleValue ssEarlyWetSeasonModifier;
		public final ForgeConfigSpec.DoubleValue ssMidWetSeasonModifier;
		public final ForgeConfigSpec.DoubleValue ssLateWetSeasonModifier;

		public final ForgeConfigSpec.DoubleValue ssEarlyDrySeasonModifier;
		public final ForgeConfigSpec.DoubleValue ssMidDrySeasonModifier;
		public final ForgeConfigSpec.DoubleValue ssLateDrySeasonModifier;

		public final ForgeConfigSpec.BooleanValue eclipticSeasonsEnabled;
		public final ForgeConfigSpec.ConfigValue<List<? extends Double>> esSpringModifier;
		public final ForgeConfigSpec.ConfigValue<List<? extends Double>> esSummerModifier;
		public final ForgeConfigSpec.ConfigValue<List<? extends Double>> esAutumnModifier;
		public final ForgeConfigSpec.ConfigValue<List<? extends Double>> esWinterModifier;

		// Thirst
		public final ForgeConfigSpec.BooleanValue thirstEnabled;
		public final ForgeConfigSpec.BooleanValue dangerousDehydration;
		public final ForgeConfigSpec.BooleanValue cumulativeThirstEffectDuration;
		public final ForgeConfigSpec.DoubleValue dehydrationDamageScaling;
		public final ForgeConfigSpec.DoubleValue thirstEffectModifier;
		public final ForgeConfigSpec.DoubleValue baseHydrationExhaustion;
		public final ForgeConfigSpec.DoubleValue sprintingHydrationExhaustion;
		public final ForgeConfigSpec.DoubleValue onJumpHydrationExhaustion;
		public final ForgeConfigSpec.DoubleValue onBlockBreakHydrationExhaustion;
		public final ForgeConfigSpec.DoubleValue onAttackHydrationExhaustion;
		public final ForgeConfigSpec.IntValue canteenCapacity;
		public final ForgeConfigSpec.BooleanValue selfWateringCanteenEnabled;
		public final ForgeConfigSpec.IntValue selfWateringCanteenWetnessIncrease;
		public final ForgeConfigSpec.IntValue largeCanteenCapacity;
		public final ForgeConfigSpec.BooleanValue allowOverridePurifiedWater;
		public final ForgeConfigSpec.IntValue hydrationLava;
		public final ForgeConfigSpec.DoubleValue saturationLava;
		public final ForgeConfigSpec.BooleanValue glassBottleLootAfterDrink;

		// > Thirst Integration
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> originsWithThirstEffectImmunity;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> originsWithExtraThirstExhaustion;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> originsWithLavaDrinking;
		public final ForgeConfigSpec.IntValue hydrationLavaBlazeborn;
		public final ForgeConfigSpec.DoubleValue saturationLavaBlazeborn;
		public final ForgeConfigSpec.DoubleValue originsExtraThirstExhaustionValue;
		public final ForgeConfigSpec.BooleanValue thirstEnabledIfVampire;

		// Health Overhaul
		public final ForgeConfigSpec.BooleanValue healthOverhaulEnabled;
		public final ForgeConfigSpec.DoubleValue maxAdditionalHealth;
		public final ForgeConfigSpec.DoubleValue maxShieldHealth;
		public final ForgeConfigSpec.BooleanValue absorptionEffectOverride;
		public final ForgeConfigSpec.IntValue heartsLostOnDeath;
		public final ForgeConfigSpec.IntValue permanentHearts;
		public final ForgeConfigSpec.IntValue resilientHeartsWithBrokenHearts;
		public final ForgeConfigSpec.DoubleValue brokenHeartsPerInjuredLimb;
		public final ForgeConfigSpec.EnumValue<EnumUtil.brokenHeartsPerInjuredLimbMode> brokenHeartsPerInjuredLimbMode;

		// Localized Body Damage
		public final ForgeConfigSpec.BooleanValue localizedBodyDamageEnabled;
		public final ForgeConfigSpec.DoubleValue headCriticalShotMultiplier;
		public final ForgeConfigSpec.DoubleValue bodyDamageMultiplier;
		public final ForgeConfigSpec.DoubleValue bodyHealthRatioRecoveredFromSleep;
		public final ForgeConfigSpec.DoubleValue healthRatioRecoveredFromSleep;
		public final ForgeConfigSpec.DoubleValue bodyHealingFoodExhaustion;
		public final ForgeConfigSpec.IntValue minFoodOnBodyHealing;
		public final ForgeConfigSpec.IntValue painkillerAddictionDuration;

		public final ForgeConfigSpec.BooleanValue passiveLimbRegenerationOnFullHealth;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> passiveLimbRegenerationEffects;
		public final ForgeConfigSpec.BooleanValue passiveLimbRegenerationAmplificationEnabled;
		public final ForgeConfigSpec.DoubleValue passiveLimbHealthRegenerated;
		public final ForgeConfigSpec.IntValue passiveLimbRegenerationTickTimer;

		public final ForgeConfigSpec.DoubleValue firstAidSuppliesLimbHealthRegenerated;
		public final ForgeConfigSpec.EnumValue<EnumUtil.limbRegenerationMode> firstAidSuppliesLimbRegenerationMode;
		public final ForgeConfigSpec.BooleanValue firstAidSuppliesHealingOverflow;
		public final ForgeConfigSpec.IntValue firstAidSuppliesTickTimer;
		public final ForgeConfigSpec.BooleanValue firstAidSuppliesExhaustsFood;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> firstAidSuppliesBoostedOnEffects;
		public final ForgeConfigSpec.DoubleValue firstAidSuppliesBoostedTickTimerMultiplier;

		public final ForgeConfigSpec.DoubleValue recoveryEffectHealingValue;
		public final ForgeConfigSpec.IntValue healingHerbsUseTime;
		public final ForgeConfigSpec.IntValue plasterUseTime;
		public final ForgeConfigSpec.IntValue bandageUseTime;
		public final ForgeConfigSpec.IntValue tonicUseTime;
		public final ForgeConfigSpec.IntValue medkitUseTime;
		public final ForgeConfigSpec.IntValue morphineUseTime;
		public final ForgeConfigSpec.IntValue morphinePainkillerTickDuration;

		public final ForgeConfigSpec.EnumValue<EnumUtil.bodyPartHealthMode> bodyPartHealthMode;
		public final ForgeConfigSpec.DoubleValue headPartHealth;
		public final ForgeConfigSpec.DoubleValue armsPartHealth;
		public final ForgeConfigSpec.DoubleValue chestPartHealth;
		public final ForgeConfigSpec.DoubleValue legsPartHealth;
		public final ForgeConfigSpec.DoubleValue feetPartHealth;

		public final ForgeConfigSpec.ConfigValue<List<? extends String>> headPartEffects;
		public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> headPartEffectAmplifiers;
		public final ForgeConfigSpec.ConfigValue<List<? extends Double>> headPartEffectThresholds;

		public final ForgeConfigSpec.ConfigValue<List<? extends String>> armsPartEffects;
		public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> armsPartEffectAmplifiers;
		public final ForgeConfigSpec.ConfigValue<List<? extends Double>> armsPartEffectThresholds;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> bothArmsPartEffects;
		public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> bothArmsPartEffectAmplifiers;
		public final ForgeConfigSpec.ConfigValue<List<? extends Double>> bothArmsPartEffectThresholds;

		public final ForgeConfigSpec.ConfigValue<List<? extends String>> chestPartEffects;
		public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> chestPartEffectAmplifiers;
		public final ForgeConfigSpec.ConfigValue<List<? extends Double>> chestPartEffectThresholds;

		public final ForgeConfigSpec.ConfigValue<List<? extends String>> legsPartEffects;
		public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> legsPartEffectAmplifiers;
		public final ForgeConfigSpec.ConfigValue<List<? extends Double>> legsPartEffectThresholds;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> bothLegsPartEffects;
		public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> bothLegsPartEffectAmplifiers;
		public final ForgeConfigSpec.ConfigValue<List<? extends Double>> bothLegsPartEffectThresholds;

		public final ForgeConfigSpec.ConfigValue<List<? extends String>> feetPartEffects;
		public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> feetPartEffectAmplifiers;
		public final ForgeConfigSpec.ConfigValue<List<? extends Double>> feetPartEffectThresholds;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> bothFeetPartEffects;
		public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> bothFeetPartEffectAmplifiers;
		public final ForgeConfigSpec.ConfigValue<List<? extends Double>> bothFeetPartEffectThresholds;

		public final ForgeConfigSpec.BooleanValue morphineSyringeApplyPainkillerAddiction;

		Common(ForgeConfigSpec.Builder builder)
		{
			builder.comment(new String [] {
					" Options related to enabling/disabling specific features",
					" See the data packs to customize the temperature of specific blocks, liquids, armors, etc."
			}).push("core");
			temperatureEnabled = builder
					.comment(" Whether the temperature system is enabled.")
					.define("Temperature Enabled", true);
			thirstEnabled = builder
					.comment(" Whether the thirst system is enabled.")
					.define("Thirst Enabled", true);
			healthOverhaulEnabled = builder
					.comment(" Whether the overhaul health system is enabled.")
					.define("Health Overhaul Enabled", true);
			localizedBodyDamageEnabled = builder
					.comment(" Whether body members receive localized damages.")
					.define("Localized Body Damage Enabled", true);
			difficultyMode = builder
					.comment(" How the mod represents a challenge for the player.",
							" Accepted values are as follows:",
							"   PEACEFUL - Temperature doesn't harm the player and the hydration doesn't decrease.",
							"   EASY - Temperature and Thirst won't hurt the player health below 10 hearts.",
							"   NORMAL - Temperature and Thirst hurts the player down to 1 heart.",
							"   HARD - Temperature and Thirst can kill the player.",
							" Any other value will default to HARD.")
					.defineEnum("Temperature Difficulty Mode", EnumUtil.DifficultyMode.HARD);

			builder.push("advanced");
			routinePacketSync = builder
					.comment(" How often player temperature, thirst, body damage and health is regularly synced between the client and server, in ticks.",
							" Lower values will increase accuracy at the cost of performance.")
					.defineInRange("Routine Packet Sync", 30, 1, Integer.MAX_VALUE);

			builder.pop();

			builder.push("misc");
			compassInfoMode = builder
					.comment(" What information the compass returns when player is using it or in an item frame.")
					.defineEnum("Compass Info Mode", EnumUtil.CompassInfo.FULL);
			showCoordinateOnMap = builder
					.comment(" If enabled, use on a filled map will show destination coordinates.")
					.define("Show Coordinate On Filled Map", true);

			hideInfoFromDebug = builder
					.comment(" If enabled, information like position and direction will be hidden from the debug screen (F3).")
					.define("Hide Info From Debug", true);
			naturalRegenerationEnabled = builder
					.comment(" If enabled, the player can regenerate health naturally if their hunger is full enough (doesn't affect external healing, such as golden apples, the Regeneration effect, etc.)")
					.define("Natural Regeneration Enabled", true);
			vanillaFreezeEnabled = builder
					.comment(" If enabled, the player suffers vanilla freeze when inside powder snow.")
					.define("Vanilla Freeze Enabled", false);
			initialHealth = builder
					.comment(" How much health player will have initially.")
					.defineInRange("Initial Player Health", 20.0, 1.0, 10000.0);
			builder.pop();
			builder.pop();

			builder.comment(" Options related to the player food data").push("food");
			baseFoodExhaustion = builder
					.comment(" Food exhausted every 10 ticks. Increase the base minecraft food exhaustion.")
					.defineInRange("Base Food Exhaustion", 0.05d, 0, 1000.0D);
			sprintingFoodExhaustion = builder
					.comment(" Food exhausted every 10 ticks while sprinting in addition to the sprinting minecraft food exhaustion.")
					.defineInRange("Sprinting Food Exhaustion", 0.1d, 0, 1000.0D);
			onAttackFoodExhaustion = builder
					.comment(" Food exhausted on every attack in addition to the minecraft attack food exhaustion.")
					.defineInRange("On Attack Food Exhaustion", 0.1d, 0, 1000.0D);
			builder.pop();

			builder.comment(" Options related to the temperature system").push("temperature");
			dangerousHeatTemperature = builder
					.comment(" If enabled, players will take damage from the effects of high temperature.")
					.define("Dangerous Heat Temperature Effects", true);
			dangerousColdTemperature = builder
					.comment(" If enabled, players will take damage from the effects of low temperature.")
					.define("Dangerous Cold Temperature Effects", true);
			goldFernChance = builder
					.comment(" Chance of the ferns to become a gold fern when grow mature.")
					.defineInRange("Gold Fern Chance", 0.01, 0, 1);

			builder.push("temperature-immunity");
			temperatureImmunityOnDeathEnabled = builder
					.comment(" If enabled, players will be immune to temperature effects after death.")
					.define("Temperature Immunity On Death Enabled", true);
			temperatureImmunityOnDeathTime = builder
					.comment(" Temperature immunity period in ticks while the player is immune to temperature effects after death.")
					.defineInRange("Temperature Immunity On Death Time", 1800, 0, 100000);
			temperatureImmunityOnFirstSpawnEnabled = builder
					.comment(" If enabled, players will be immune to temperature effects on first spawn in a world.")
					.define("Temperature Immunity On First Spawn Enabled", true);
			temperatureImmunityOnFirstSpawnTime = builder
					.comment(" Temperature immunity period in ticks while the player is immune to temperature effects on first spawn.")
					.defineInRange("Temperature Immunity On First Spawn Time", 1800, 0, 100000);
			builder.pop();

			builder.push("secondary_effects");
			heatTemperatureSecondaryEffects = builder
					.comment(" If enabled, players will also receive other effects from their current temperature state.",
							" If the player is too hot, hydration will deplete faster.")
					.define("Heat Temperature Secondary Effects", true);
			coldTemperatureSecondaryEffects = builder
					.comment(" If enabled, players will also receive other effects from their current temperature state.",
							" If the player is too cold, hunger will deplete faster.")
					.define("Cold Temperature Secondary Effects", true);
			heatThirstEffectModifier = builder
					.comment(" How much thirst exhaustion will be added every 50 ticks with no amplification effect, when the player suffers from heat.")
					.defineInRange("Heat Thirst Effect Modifier", 0.2d, 0, 1000.0d);
			coldHungerEffectModifier = builder
					.comment(" How much food exhaustion will be added every 50 ticks with no amplification effect, when the player suffers from frostbite.",
							" As reference, the hunger effect add 0.025 food exhaustion every 50 ticks.")
					.defineInRange("Cold Hunger Modifier", 0.1d, 0, 1000.0d);
			builder.pop();

			onFireModifier = builder
					.comment(" How much of an effect being on fire has on a player's temperature.")
					.defineInRange("Player On Fire Modifier", 12.5, -1000, 1000);
			sprintModifier = builder
					.comment(" How much of an effect sprinting has on a player's temperature.")
					.defineInRange("Player Sprint Modifier", 1.5, -1000, 1000);
			altitudeModifier = builder
					.comment(" How much of an effect altitude has on player's temperature.",
							" Each 64 blocks further from sea level will impact player's temperature by this value.",
							" The sea level can be defined via datapack under the dimension's temperature.",
							" As an example, a value of -6 will reduce the player's temperature by 6 for each 64 blocks (the calculus is done linearly).")
					.defineInRange("Altitude Modifier", -6.0, -1000, 1000);

			builder.push("wetness");
			wetnessEnabled = builder
					.comment(" Enable the wetness mechanic.")
					.define("Wetness Enabled", true);

			wetnessImmunityMounts = builder
					.comment(" List of mounts that provide a wetness immunity.")
					.defineListAllowEmpty("Wetness Immunity Mounts", List.of("alexscaves:submarine", "immersive_machinery:bamboo_bee", "immersive_machinery:tunnel_digger", "immersive_machinery:redstone_sheep", "immersive_machinery:copperfin"), Config::validateEntityType);

			wetMultiplier = builder
					.comment(" How much being wet influences the player's temperature.",
							" It means that for a value of -10, the body temperature of the player is reduced by 10.")
					.defineInRange("Wetness Modifier", -10.0, -1000, 1000);

			wetnessTickTimer = builder
					.comment(" How frequently the wetness is modified.",
							" By default, every 10 ticks, the wetness will either increase or decrease, based on the conditions.")
					.defineInRange("Wetness Tick Timer", 10, 1, 100000);

			wetnessDecrease = builder
					.comment(" How much the wetness decrease when out of water.")
					.defineInRange("Wetness Decrease", -3, -1000, 0);
			wetnessRainIncrease = builder
					.comment(" How much the wetness increase when under rain.")
					.defineInRange("Wetness Under Rain Increase", 7, 0, 1000);
			wetnessFluidIncrease = builder
					.comment(" How much the wetness increase when the player is in a fluid, scale by the amount of fluid in the block.",
							" The defined value is for a full block of fluid, and goes up to 2 times this value when fully immerge.")
					.defineInRange("Wetness In Fluid Increase", 10, 0, 1000);
			builder.pop();

			builder.push("huddling");
			playerHuddlingModifier = builder
					.comment(" How much nearby players increase the ambient temperature by.", " Note that this value stacks!")
					.defineInRange("Player Huddling Modifier", 0.5, -1000, 1000);
			playerHuddlingRadius = builder
					.comment(" The radius, in blocks, around which players will add to each other's temperature.")
					.defineInRange("Player Huddling Radius", 1, 0, 10);
			builder.pop();

			builder.push("biomes");
			biomeTemperatureMultiplier = builder
					.comment(" How much a biome's temperature effects are multiplied.")
					.defineInRange("Biome Temperature Multiplier", 18.0d, 0.0d, 1000);
			biomeEffectsEnabled = builder
					.comment(" Whether biomes will have an effect on a player's temperature.")
					.define("Biomes affect Temperature", true);
			biomeDrynessMultiplier = builder
					.comment(" How much hot biome's dryness will make nights really cold.",
							" Affects only dry (minecraft down fall <0.2) and hot biome.",
							" 1 means no dryness effect; 0.5 means the biome temp will be divided by 2 at the middle of the night.")
					.defineInRange("Biome's Dryness Multiplier", 0.2d, 0, 1);
			builder.pop();

			builder.comment(" The underground effect starts apply at Start Distance to the world surface.",
					" The underground effect will linearly apply a multiplier on the biome temperature, and averages the time and season temperature effects.").push("underground");
			undergroundBiomeTemperatureMultiplier = builder
					.comment(" How much a biomes temperature effects are multiplied when player is underground")
					.defineInRange("Underground Biome Temperature Multiplier", 0.8d, 0.0d, 1000);
			undergroundEffectStartDistanceToWS = builder
					.comment(" Distance to the World Surface where underground effect will start to be applied.",
							" Smaller distance, no underground effect are applied.")
					.defineInRange("Start Distance To World Surface For Underground Effect", 10, 0, 400);
			undergroundEffectEndDistanceToWS = builder
					.comment(" Distance to the World Surface where underground effect will be maximal.",
							" Bigger distance, the underground effect is maximal. Between the Start and End Distance, the increase of underground effect is linear.")
					.defineInRange("End Distance To World Surface For Underground Effect", 16, 0, 400);
			builder.pop();

			builder.push("weather");
			rainTemperatureModifier = builder
					.comment(" How much of an effect rain has on temperature.",
							" It means that for a value of -2, the body temperature of the player is reduced by 2.")
					.defineInRange("Rain Temperature Modifier", -2.0, -1000, 1000);
			snowTemperatureModifier = builder
					.comment(" How much of an effect snow has on temperature.",
							" It means that for a value of -6, the body temperature of the player is reduced by 6.")
					.defineInRange("Snow Temperature Modifier", -6.0, -1000, 1000);
			builder.pop();

			builder.comment(" Freeze effect increases while inside snow powder.").push("freeze");
			maxFreezeTemperatureModifier = builder
					.comment(" How much of an effect freeze has on temperature when reaching maximum tick time. Starts at 0 and increases linearly.")
					.defineInRange("Max Freeze Temperature Modifier", -10.0, -1000, 1000);
			maxFreezeEffectTick = builder
					.comment(" How long in tick before freeze modifier reaches its maximum effect.")
					.defineInRange("Max Freeze Effect Tick", 400, 0, 100000);
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
							" It means that for a value of -6, the body temperature of the player is reduced by 6.",
							" Only effective when reaching the threshold and during day time!")
					.defineInRange("Shade Time Modifier", -6.0, -1000, 1000);
			shadeTimeModifierThreshold = builder
					.comment(" Defines when the biome temperature added by the season temperature (if seasons mod loaded) will trigger a shade effect.")
					.defineInRange("Shade Time Modifier Threshold", 9.0, 0, 10000);
			builder.pop();

			builder.comment(" Temperature coat adds temperature effects on armors by using the sewing table.",
							" Adaptive means the coating will maintain the player's temperature temperate.")
					.push("coat");

			builder.comment(" Add a heating resistance on armors.").push("heating");
			heatingCoat1Modifier = builder.defineInRange("Heating Coat I", 2.0d, 0, 1000.0d);
			heatingCoat2Modifier = builder.defineInRange("Heating Coat II", 3.0d, 0, 1000.0d);
			heatingCoat3Modifier = builder.defineInRange("Heating Coat III", 4.0d, 0, 1000.0d);
			builder.pop();

			builder.comment(" Add a cooling resistance on armors.").push("cooling");
			coolingCoat1Modifier = builder.defineInRange("Cooling Coat I", 2.0d, 0, 1000.0d);
			coolingCoat2Modifier = builder.defineInRange("Cooling Coat II", 3.0d, 0, 1000.0d);
			coolingCoat3Modifier = builder.defineInRange("Cooling Coat III", 4.0d, 0, 1000.0d);
			builder.pop();

			builder.comment(" Add a temperature resistance on armors that can both heat and cool the player.")
					.push("thermal");
			thermalCoat1Modifier = builder.defineInRange("Thermal Coat I", 2.0d, 0, 1000.0d);
			thermalCoat2Modifier = builder.defineInRange("Thermal Coat II", 3.0d, 0, 1000.0d);
			thermalCoat3Modifier = builder.defineInRange("Thermal Coat III", 4.0d, 0, 1000.0d);
			builder.pop();
			builder.pop();

			builder.push("advanced");
			tempInfluenceMaximumDist = builder
					.comment(" Maximum influence distance, in blocks, where thermal sources will have an effect on temperature.")
					.defineInRange("Temperature Influence Maximum Distance", 20, 1, 40);
			tempInfluenceUpDistMultiplier = builder
					.comment(" How strongly influence distance above the player is reduced for thermal sources to have an effect on temperature.")
					.comment(" Example max dist is 10, up mult is 0.75 -> max distance is 10 * 0.75 = 7.5 blocks above the player.",
							" Logic is that heat goes up, the strength of the heat source above the player is decreased faster with distance.")
					.defineInRange("Temperature Influence Up Distance Multiplier", 0.75, 0.0, 1.0);
			tempInfluenceInWaterDistMultiplier = builder
					.comment(" How strongly influence distance in water is reduced for thermal sources to have an effect on temperature.",
							"The under water maximum distance is defined as the maximum distance * this value")
					.defineInRange("Temperature Influence Outside Distance Multiplier", 0.25, 0.0, 1.0);

			tempInfluenceOutsideDistMultiplier = builder
					.comment(" How strongly influence distance outside a structure is reduced for thermal sources to have an effect on temperature.",
							" The outside maximum distance is defined as the maximum distance * this value")
					.defineInRange("Temperature Influence Outside Distance Multiplier", 0.5, 0.0, 1.0);
			builder
					.comment(" The player's temperature will be adjusted at each temperature tick time," ,
							" by an amount of temperature defined between the minimum and the maximum temperature modification adjusted linearly.")
					.push("temperature-modification");
			tempTickTime = builder
					.comment(" Amount of time in ticks between 2 player temperature modification. The bigger is this value, the more time it takes between temperature adjustments.")
					.defineInRange("Temperature Tick Time", 20, 5, Integer.MAX_VALUE);
			maxTemperatureModification = builder
					.comment(" Maximum amount of temperature the player's temperature can be modified at each temperature tick time.",
							" Correspond to the amount of temperature given when temperature difference is maximum, meaning 40.")
					.defineInRange("Maximum Temperature Modification", 1, 0.1, Integer.MAX_VALUE);
			minTemperatureModification = builder
					.comment(" Minimum amount of temperature the player's temperature can be modified at each temperature tick time.",
							" Correspond to the amount of temperature given when there is no temperature difference")
					.defineInRange("Minimum Temperature Modification", 0.2, 0.1, Integer.MAX_VALUE);
			builder.pop();
			builder.pop();

			builder.push("integration");

			builder.comment(" If TerraFirmaCraft is installed, then biome, time, season (if serene seasons installed) and altitude modifiers will be disabled, and TerraFirmaCraft calculation used instead.",
							" All other modifiers remain to calculate Player temperature.").push("terrafirmacraft");
			tfcItemHeatMultiplier = builder
					.comment(" How much the heat of the item provided by TerraFirmaCraft is multiplied. 0 deactivates the impact on temperature.")
					.defineInRange("TerraFirmaCraft Item Heat Multiplier", 0.01d, 0, 1000);
			tfcTemperatureMultiplier = builder
					.comment(" How much the temperature given from TerraFirmaCraft is multiplied. 0 deactivates the impact on temperature.")
					.defineInRange("TerraFirmaCraft Temperature Multiplier", 1.0d, 0, 1000);
			builder.pop();

			builder.push("serene-seasons");
			sereneSeasonsEnabled = builder
					.comment(" If Serene Seasons is installed, whether the seasons have an effect on the player's temperature.")
					.define("Serene Seasons Enabled", true);
			ssTropicalSeasonsEnabled = builder
					.comment(" If the tropical seasons are disabled, the normal summer-autumn-winter-spring seasons are applied.",
							" If disabled, dry and wet seasons are applied for hot biomes.")
					.define("Tropical Seasons Enabled", false);
			ssSeasonCardsEnabled = builder
					.comment(" If season cards are enabled, season cards will appear at every season changes.")
					.define("Season Cards Enabled", false);
			ssDefaultSeasonEnabled = builder
					.comment(" If default season is enabled, when serene season defines no season effect in a biome, the normal season temperature will be applied.",
							" If disabled, when serene season defines no season effects, no season temperature will be applied.")
					.define("Default Season Enabled", true);

			builder.comment(" Temperature modifiers per season in temperate biomes." +
							" The value is reached at the middle of the sub season, and smoothly transition from one to another.")
					.push("temperate");
			builder.push("spring");
			ssEarlySpringModifier = builder.defineInRange("Early Spring Modifier", -3.0, -1000, 1000);
			ssMidSpringModifier = builder.defineInRange("Mid Spring Modifier", 0.0, -1000, 1000);
			ssLateSpringModifier = builder.defineInRange("Late Spring Modifier", 3.0, -1000, 1000);
			builder.pop();

			builder.push("summer");
			ssEarlySummerModifier = builder.defineInRange("Early Summer Modifier", 6.0, -1000, 1000);
			ssMidSummerModifier = builder.defineInRange("Mid Summer Modifier", 10.0, -1000, 1000);
			ssLateSummerModifier = builder.defineInRange("Late Summer Modifier", 6.0, -1000, 1000);
			builder.pop();

			builder.push("autumn");
			ssEarlyAutumnModifier = builder.defineInRange("Early Autumn Modifier", 3.0, -1000, 1000);
			ssMidAutumnModifier = builder.defineInRange("Mid Autumn Modifier", 0.0, -1000, 1000);
			ssLateAutumnModifier = builder.defineInRange("Late Autumn Modifier", -3.0, -1000, 1000);
			builder.pop();

			builder.push("winter");
			ssEarlyWinterModifier = builder.defineInRange("Early Winter Modifier", -7.0, -1000, 1000);
			ssMidWinterModifier = builder.defineInRange("Mid Winter Modifier", -12.0, -1000, 1000);
			ssLateWinterModifier = builder.defineInRange("Late Winter Modifier", -7.0, -1000, 1000);
			builder.pop();
			builder.pop();

			builder.comment(" Temperature modifiers per season in tropical biomes.").push("tropical");
			builder.push("wet-season");
			ssEarlyWetSeasonModifier = builder.defineInRange("Early Wet Season Modifier", -1.0, -1000, 1000);
			ssMidWetSeasonModifier = builder.defineInRange("Mid Wet Season Modifier", -5.0, -1000, 1000);
			ssLateWetSeasonModifier = builder.defineInRange("Late Wet Season Modifier", -1.0, -1000, 1000);
			builder.pop();

			builder.push("dry-season");
			ssEarlyDrySeasonModifier = builder.defineInRange("Early Dry Season Modifier", 3.0, -1000, 1000);
			ssMidDrySeasonModifier = builder.defineInRange("Mid Dry Season Modifier", 7.0, -1000, 1000);
			ssLateDrySeasonModifier = builder.defineInRange("Late Dry Season Modifier", 3.0, -1000, 1000);
			builder.pop();

			builder.pop();
			builder.pop();

			builder.push("ecliptic-seasons");
			eclipticSeasonsEnabled = builder
					.comment(" If Ecliptic Seasons is installed, whether the seasons have an effect on the player's temperature.")
					.define("Ecliptic Seasons Enabled", true);

			builder.comment(" Temperature modifiers per season. Each season is subdivided in 6 sub seasons." +
							" The value is reached at the middle of the sub season, and smoothly transition from one to another.")
					.push("temperature");
			esSpringModifier = builder.defineList("Spring Modifier", List.of(-10.0, -7.0, -5.0, -3.0, -1.0, 0.0), Config::validateDouble);
			esSummerModifier = builder.defineList("Summer Modifier", List.of(1.0, 3.0, 5.0, 7.0, 9.0, 10.0), Config::validateDouble);
			esAutumnModifier = builder.defineList("Autumn Modifier", List.of(9.0, 7.0, 5.0, 3.0, 1.0, 0.0), Config::validateDouble);
			esWinterModifier = builder.defineList("Winter Modifier", List.of(-1.0, -3.0, -5.0, -7.0, -10.0, -12.0), Config::validateDouble);
			builder.pop();
			builder.pop();

			builder.comment(" Origins mod integration for temperature system.")
					.push("origins");
			originsWithWetnessImmunity = builder
					.comment(" Origins that are immune to wetness effects.",
							" Format: 'modid:origin_name' (e.g., 'origins:merling')")
					.defineListAllowEmpty("Origins With Wetness Immunity",
							List.of("origins:merling", "origins:blazeborn"),
							Config::validateResourceLocationString);
			originsWithHighAltitudeImmunity = builder
					.comment(" Origins that are immune to high altitude coldness.",
							" Format: 'modid:origin_name' (e.g., 'origins:avian')")
					.defineListAllowEmpty("Origins With High Altitude Immunity",
							List.of("origins:avian", "origins:elytrian"),
							Config::validateResourceLocationString);
			originsWithOnFireImmunity = builder
					.comment(" Origins that are immune to on fire temperature effects.",
							" Format: 'modid:origin_name' (e.g., 'origins:blazeborn')")
					.defineListAllowEmpty("Origins With On Fire Immunity",
							List.of("origins:blazeborn"),
							Config::validateResourceLocationString);
			builder.pop();

			builder.pop();
			builder.pop();

			builder.comment(" Options related to thirst feature").push("thirst");
			dangerousDehydration = builder
					.comment(" If enabled, players will take damage from the complete dehydration.")
					.define("Dangerous Dehydration", true);
			cumulativeThirstEffectDuration = builder
					.comment(" If enabled, each time the player receives a thirst effect, its duration will be added to the thirst effect duration if already on the player.")
					.define("Cumulative Thirst Effect Duration", true);
			builder.push("exhaustion");
			baseHydrationExhaustion = builder
					.comment(" Hydration exhausted every 10 ticks.")
					.defineInRange("Base Hydration Exhaustion", 0.03d, 0, 1000.0d);
			sprintingHydrationExhaustion = builder
					.comment(" Hydration exhausted when sprinting, replacing the base hydration exhausted.")
					.defineInRange("Sprinting Hydration Exhaustion", 0.1d, 0, 1000.0d);
			onJumpHydrationExhaustion = builder
					.comment(" Hydration exhausted on every jump.")
					.defineInRange("On Jump Hydration Exhaustion", 0.15d, 0, 1000.0d);
			onBlockBreakHydrationExhaustion = builder
					.comment(" Hydration exhausted on every block break.")
					.defineInRange("On Block Break Hydration Exhaustion", 0.07d, 0, 1000.0d);
			onAttackHydrationExhaustion = builder
					.comment(" Hydration exhausted on every attack.")
					.defineInRange("On Attack Hydration Exhaustion", 0.3d, 0, 1000.0d);
			builder.pop();
			dehydrationDamageScaling = builder
					.comment(" Scaling of the damages dealt when completely dehydrated. Each tick damage will be increased by this value.")
					.defineInRange("Dehydration Damage Scaling", 0.3d, 0, 1000.0d);
			thirstEffectModifier = builder
					.comment(" How much thirst exhaustion will be added every 50 ticks when the player suffers from non amplified Thirst Effect.",
							" The player will suffer Thirst Effect from dirty water for example.")
					.defineInRange("Thirst Effect Modifier", 0.25d, 0, 1000);
			builder.push("canteen");
			selfWateringCanteenEnabled = builder
					.comment(" If enabled, the player can water himself by using the canteen while crouching.",
							" This will increase the player wetness and remove fire.")
					.define("Self Watering Canteen Enabled", true);
			selfWateringCanteenWetnessIncrease = builder
					.comment(" If Self Watering Canteen and Wetness are enabled, defines how much wetness is added to the player.",
							" Set this value to 0 to have no wetness added. By default, the maximum wetness is 400.")
					.defineInRange("Self Watering Canteen Wetness", 400, 0, 10000);
			canteenCapacity = builder
					.comment(" Capacity of the canteen used to store water.")
					.defineInRange("Canteen Capacity", 10, 0, 1000);
			largeCanteenCapacity = builder
					.comment(" Capacity of the large canteen used to store water.")
					.defineInRange("Large Canteen Capacity", 20, 0, 1000);
			allowOverridePurifiedWater = builder
					.comment(" Allow override of purified water stored in canteen with normal water.")
					.define("Allow Override Purified Water", true);
			builder.pop();
			builder.comment(" Allows drinking from lava. Can be used as bauble.").push("nether_chalice");
			hydrationLava = builder
					.comment(" Amount of hydration recovered when drinking from lava.")
					.defineInRange("Lava Hydration", 6, 0, 20);
			saturationLava = builder
					.comment(" Amount of saturation recovered when drinking from lava.")
					.defineInRange("Lava Saturation", 4.0, 0, 20);
			builder.pop();
			builder.push("juices");
			glassBottleLootAfterDrink = builder
					.comment(" Whether the player retrieves a glass bottle after drinking a juice.")
					.define("Glass Bottle Loot After Drinking A Juice", true);
			builder.pop();

			builder.push("integration");
			builder.comment(" Origins mod integration for thirst system.")
					.push("origins");
			originsWithThirstEffectImmunity = builder
					.comment(" Origins that are immune to thirst effects (e.g., from dirty water).",
							" Format: 'modid:origin_name' (e.g., 'origins:merling')")
					.defineListAllowEmpty("Origins With Thirst Effect Immunity",
							List.of("origins:merling"),
							Config::validateResourceLocationString);
			
			originsWithExtraThirstExhaustion = builder
					.comment(" Origins that have extra thirst exhaustion added every 20 ticks.",
							" The exhaustion amount is defined in 'Extra Thirst Exhaustion' config below.",
							" Format: 'modid:origin_name' (e.g., 'origins:shulk')")
					.defineListAllowEmpty("Origins With Extra Thirst Exhaustion",
							List.of("origins:shulk", "origins:phantom"),
							Config::validateResourceLocationString);
			
			originsWithLavaDrinking = builder
					.comment(" Origins that can drink lava for hydration.",
							" The hydration and saturation amounts are defined in 'Lava Hydration' and 'Lava Saturation' configs below.",
							" Format: 'modid:origin_name' (e.g., 'origins:blazeborn')")
					.defineListAllowEmpty("Origins With Lava Drinking",
							List.of("origins:blazeborn"),
							Config::validateResourceLocationString);
			
			hydrationLavaBlazeborn = builder
					.comment(" Amount of hydration recovered when origins in 'Origins With Lava Drinking' drink from lava.")
					.defineInRange("Lava Hydration", 3, 0, 20);
			saturationLavaBlazeborn = builder
					.comment(" Amount of saturation recovered when origins in 'Origins With Lava Drinking' drink from lava.")
					.defineInRange("Lava Saturation", 1.0, 0, 20);
			
			originsExtraThirstExhaustionValue = builder
					.comment(" Amount of thirst exhaustion added every 20 ticks for origins in 'Origins With Extra Thirst Exhaustion'.")
					.defineInRange("Extra Thirst Exhaustion", 0.1, 0, 1000);
			
			builder.pop();

			builder.push("vampirism");
			thirstEnabledIfVampire = builder
					.comment(" If Vampirism is installed and if thirst enabled while being a vampire, keep the thirst system in addition to the vampiric one.",
							" If disabled, the thirst system will be disabled for vampires.")
					.define("Thirst Enabled If Vampire", false);
			builder.pop();
			builder.pop();

			builder.pop();

			builder.comment(" Options related to health overhaul").push("health-overhaul");
			maxAdditionalHealth = builder
					.comment(" How much of Additional Health a player can accumulate. 2 Heath means a full heart.")
					.defineInRange("Maximum Additional Health", 20.0, 0.0, 10000.0);

			builder.push("shield-health");
			maxShieldHealth = builder
					.comment(" How much of Shield Health a player can accumulate. 2 Shield Heath means a full shield.",
							" Shield Health are lost when the player suffers damages and can't regenerate. Works similarly as the Minecraft Absorption.")
					.defineInRange("Maximum Shield Health", 20.0, 1.0, 10000.0);
			absorptionEffectOverride = builder
					.comment(" Override the absorption effect by a shield health increase of 2.",
							" The absorption is typically given by the Golden Apple.")
					.define("Absorption Effect Override", true);
			builder.pop();

			builder.push("heart-loss");
			heartsLostOnDeath = builder
					.comment(" The number of Hearts lost on death.")
					.defineInRange("Hearts Lost On Death", 0, 0, 10000);

			permanentHearts = builder
					.comment(" The number of Hearts below which player can't lose hearts upon death.",
							" The hearts below this limit are de facto Permanent Hearts.")
					.defineInRange("Permanent Hearts", 10, 1, 10000);
			builder.pop();
			builder.push("broken-hearts")
					.comment(" Broken Hearts are an interaction with the localized body damage feature. Enables both feature to have it.",
							" Broken Hearts are lost hearts when a player's limb is severely injured and it can be recovered by healing the injured limb.");
			resilientHeartsWithBrokenHearts = builder
					.comment(" The Resilient Hearts is the number of heart below which Broken Hearts can no longer be added.",
							" By default, the player has 2 resilient heart, meaning no matter the amount of broken hearts, the player won't go below 2 hearts.")
					.defineInRange("Minimum Amount Of Player's Heart With Broken Hearts (Broken Heart Resilience)", 2, 1, 10000);
			brokenHeartsPerInjuredLimb = builder
					.comment(" Amount of Broken Hearts added per limbs fully injured.")
					.defineInRange("Added Broken Hearts Per Injured Limb", 0.1, 0, 10000);
			brokenHeartsPerInjuredLimbMode = builder
					.comment(" How broken hearts inflicted per injured limbs are calculated. The total amount will be round down to have an integer amount of broken hearts.",
							" For example, if the amount per injured limb is 0.1 with mode Player Dynamic and the player has 3 limbs injured, the total amount is 3 * (0.1 * 20), 20 being the default player max health, so 6 broken hearts will be inflicted.",
							" Accepted values are as follows:",
							"   SIMPLE - The broken heart amount is a fixed value defined in Broken Hearts Per Injured Limb.",
							"   PLAYER_DYNAMIC - The broken heart amount is a percentage value of the player max health using the percentage value defined in Broken Hearts Per Injured Limb.",
							"   LIMB_DYNAMIC - The broken heart amount is a percentage value of the injured limb max health using the percentage value defined in Broken Hearts Per Injured Limb.",
							" Any other value will default to SIMPLE.")
					.defineEnum("Broken Hearts Per Injured Limb Mode", EnumUtil.brokenHeartsPerInjuredLimbMode.PLAYER_DYNAMIC);
			builder.pop();
			builder.pop();

			builder.comment(" Options related to localized body damage",
					" The damageSourceBodyParts.json allows you to define for specific damage source, the damage spread across specified body parts.",
					" The damage distribution can either be ONE_OF or ALL. ALL means the damage are equally divided across all body parts.").push("body-damage");
			headCriticalShotMultiplier = builder
					.comment(" Multiply the damage taken by the player when shot in the head without helmet.")
					.defineInRange("Headshot Multiplier", 2.0d, 1.0d, 1000.0d);
			bodyDamageMultiplier = builder
					.comment(" How much of the hurt player's damage is assigned to the body parts.")
					.defineInRange("Body Damage Multiplier", 1.0d, 0.0d, 1000.0d);
			bodyHealthRatioRecoveredFromSleep = builder
					.comment(" How much health ratio are recovered in all body parts from bed sleeping.")
					.defineInRange("Body Part Health Ratio Recovered", 1.0d, 0.0d, 1.0d);
			healthRatioRecoveredFromSleep = builder
					.comment(" How much health ratio are recovered from bed sleeping.")
					.defineInRange("Health Ratio Recovered", 1.0d, 0.0d, 1.0d);

			bodyHealingFoodExhaustion = builder
					.comment(" How much food is exhausted when a limb regenerates based on the amount of health regenerated.",
							" For each 1 health regenerated, the food is exhausted by this value.")
					.defineInRange("Body Healing Food Exhaustion", 0.5d, 0, 1000.0D);

			minFoodOnBodyHealing = builder
					.comment(" The hunger bar won't drop below this value while body is healing.",
							" Each hunger icon has a value of 2 in the hunger bar.")
					.defineInRange("Minimum Food On Body Healing", 0, 0, 1000);

			painkillerAddictionDuration = builder
					.comment(" How long in ticks is the Addiction Effect lasting. 0 deactivates the feature.",
							" The Addiction Effect prevents you from re-using the morphine item.")
					.defineInRange("Painkiller Addiction Duration", 3600, 0, 100000);

			builder.push("healing-limbs");
			builder.push("passive-regeneration");
			passiveLimbRegenerationOnFullHealth = builder
					.comment(" The limbs will be healed when the player is at max health.")
					.define(" Passive Limb Regeneration On Full Health", true);
			passiveLimbRegenerationEffects = builder
					.comment(" The limbs will be healed when the player is under one of the mentioned effect, the most damaged limb first.")
					.defineListAllowEmpty("Passive Limb Regeneration On Effects", List.of("minecraft:regeneration", "farmersdelight:comfort"), Config::validateEffectName);
			passiveLimbRegenerationAmplificationEnabled = builder
					.comment(" Whether the amplification of the effect increase the limb regeneration speed.",
							" The speed increase follows the same logic as the Regeneration Effect Amplification :",
							"    Amplifier 1 -> tick timer /2, Amplifier 2 -> tick timer /4, ...")
					.define("Passive Limb Regeneration Amplification Enabled", true);
			passiveLimbHealthRegenerated = builder
					.comment(" Amount of limb health regenerated, the most damaged limb first.")
					.defineInRange("Passive Limb Health Regenerated", 0.5, 0, 1000);
			passiveLimbRegenerationTickTimer = builder
					.comment(" How fast in ticks the limbs regenerate passively. 20 ticks = 1s")
					.defineInRange("Passive Limb Regeneration Tick Timer", 50, 0, 10000);
			builder.pop();
			builder.comment(" The First Aid Supplies overrides the passive limb regeneration as its effects is meant to be stronger.").push("first-aid-supplies");
			firstAidSuppliesLimbHealthRegenerated = builder
					.comment(" The First Aid Supplies regenerate limb health passively, either by holding it or using Curios mod, the most damaged limb first.")
					.defineInRange("First Aid Supplies Limb Health Regenerated", 0.25, 0, 1000);
			firstAidSuppliesLimbRegenerationMode = builder
					.comment(" How a player's limb health regenerated is defined. Accepted values are as follows:",
							"   SIMPLE - The limb health regenerated is a fixed value defined in First Aid Supplies Limb Health Regenerated.",
							"   PLAYER_DYNAMIC - The limb health regenerated is a percentage value of the player max health using the percentage value defined in First Aid Supplies Limb Health Regenerated.",
							"   LIMB_DYNAMIC - The limb health regenerated is a percentage value of the limb max health using the percentage value defined in First Aid Supplies Limb Health Regenerated.",
							" Any other value will default to SIMPLE.")
					.defineEnum("First Aid Supplies Limb Regeneration Mode", EnumUtil.limbRegenerationMode.LIMB_DYNAMIC);
			firstAidSuppliesHealingOverflow = builder
					.comment(" Whether the exceeded limb health regenerated will heal the next most damaged limb.",
							" Only available for Regeneration Mode SIMPLE or PLAYER_DYNAMIC.")
					.define("First Aid Supplies Healing Overflow", false);
			firstAidSuppliesTickTimer = builder
					.comment(" How fast in ticks the First Aid Supplies will heal limbs. 20 ticks = 1s")
					.defineInRange("First Aid Supplies Tick Timer", 300, 0, 10000);
			firstAidSuppliesExhaustsFood = builder
					.comment(" Whether the First Aid Supplies exhaust food when healing limbs, such as the other healing items.")
					.define("First Aid Supplies Exhausts Food", true);
			firstAidSuppliesBoostedOnEffects = builder
					.comment(" The First Aid Supplies will heal limbs faster when the player is under one of the mentioned effect.")
					.defineListAllowEmpty("First Aid Supplies Boosted On Effects", List.of("minecraft:regeneration", "farmersdelight:comfort"), Config::validateEffectName);
			firstAidSuppliesBoostedTickTimerMultiplier = builder
					.comment(" How much the First Aid Supplies tick timer is multiplied when boosted. ",
							" A value of 1 would deactivate the speed boost. 0.5 makes the heal twice faster.")
					.defineInRange("First Aid Supplies Tick Timer Multiplier", 0.75, 0.1, 1);
			builder.pop();
			builder.push("healing-items");
			recoveryEffectHealingValue = builder
					.comment(" Amount of player health regenerated by the Recovery Effect every effect tick.",
							" For comparison, the Regeneration Effect healing amount is 1.")
					.defineInRange("Recovery Effect Healing Amount", 0.1, 0, 1000);

			healingHerbsUseTime = builder
					.comment(" Item use time in ticks.")
					.defineInRange("Healing Herbs Use Time", 20, 0, 1000);

			plasterUseTime = builder
					.comment(" Item use time in ticks.")
					.defineInRange("Plaster Use Time", 20, 0, 1000);

			bandageUseTime = builder
					.comment(" Item use time in ticks.")
					.defineInRange("Bandage Use Time", 30, 0, 1000);

			tonicUseTime = builder
					.comment(" Item use time in ticks.")
					.defineInRange("Tonic Use Time", 50, 0, 1000);

			medkitUseTime = builder
					.comment(" Item use time in ticks.")
					.defineInRange("Medkit Use Time", 50, 0, 1000);
			builder.pop();

			builder.push("morphine");
			morphineUseTime = builder
					.comment(" Item use time in ticks.")
					.defineInRange("Morphine Use Time", 30, 0, 1000);
			morphinePainkillerTickDuration = builder
					.comment(" Painkiller effect duration in ticks. This effect prevents the player to be affected by broken limbs effects.")
					.defineInRange("Morphine Painkiller Duration", 1800, 0, 10000);
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
					.defineEnum("Body Part Health Mode", EnumUtil.bodyPartHealthMode.DYNAMIC);

			headPartHealth = builder.defineInRange("Head Part Health", 0.4d, 0.0d, 1000.0d);
			armsPartHealth = builder.comment(" Both arms will have this health.")
					.defineInRange("Arms Part Health", 0.4d, 0.0d, 1000.0d);
			chestPartHealth = builder.defineInRange("Chest Part Health", 0.6d, 0.0d, 1000.0d);
			legsPartHealth = builder.comment(" Both legs will have this health.")
					.defineInRange("Legs Part Health", 0.6d, 0.0d, 1000.0d);
			feetPartHealth = builder.comment(" Both feet will have this health.")
					.defineInRange("Feet Part Health", 0.4d, 0.0d, 1000.0d);
			builder.pop();

			builder.push("body-parts-effects");
			builder.comment(" Each effect, threshold and amplifier lists must have the same number of items.",
							" The first effect will be triggered with the first amplifier value when the first threshold is reach.")
					.push("head");
			headPartEffects = builder
					.comment(" The list of effects that will be triggered when the head is damaged by the percentage of remaining head health defined in the thresholds.")
					.defineListAllowEmpty("Head Part Effects", List.of(LegendarySurvivalOverhaul.MOD_ID + ":headache"), Config::validateEffectName);
			headPartEffectAmplifiers = builder
					.comment(" The list of amplifiers the effect will have.",
							" 0 means the basic effect, 1 means the effect is amplified once.")
					.defineListAllowEmpty("Head Part Effect Amplifiers", List.of(0), Config::validatePositiveInt);
			headPartEffectThresholds = builder
					.comment(" The list of thresholds for which each effect will be triggered. A threshold is a percentage of remaining head health.",
							" 0 means the head is fully damaged.")
					.defineListAllowEmpty("Head Part Effect Thresholds", List.of(0.2), Config::validatePercentDouble);
			builder.pop();
			builder.push("arms");
			armsPartEffects = builder.defineListAllowEmpty("Arms Part Effects", List.of("minecraft:mining_fatigue"), Config::validateEffectName);
			armsPartEffectAmplifiers = builder.defineListAllowEmpty("Arms Part Effect Amplifiers", List.of(0), Config::validatePositiveInt);
			armsPartEffectThresholds = builder.defineListAllowEmpty("Arms Part Effect Thresholds", List.of(0.2), Config::validatePercentDouble);
			bothArmsPartEffects = builder
					.comment(" These effects will be triggered when both arms reach the thresholds.",
							" If a same effect is used with a higher amplifier, the higher prevails (normal Minecraft behaviour).")
					.defineListAllowEmpty("Both Arms Part Effects", List.of("minecraft:weakness"), Config::validateEffectName);
			bothArmsPartEffectAmplifiers = builder.defineListAllowEmpty("Both Arms Part Effect Amplifiers", List.of(0), Config::validatePositiveInt);
			bothArmsPartEffectThresholds = builder.defineListAllowEmpty("Both Arms Part Effect Thresholds", List.of(0.2), Config::validatePercentDouble);
			builder.pop();
			builder.push("chest");
			chestPartEffects = builder.defineListAllowEmpty("Chest Part Effects", List.of(LegendarySurvivalOverhaul.MOD_ID + ":vulnerability"), Config::validateEffectName);
			chestPartEffectAmplifiers = builder.defineListAllowEmpty("Chest Part Effect Amplifier", List.of(0), Config::validatePositiveInt);
			chestPartEffectThresholds = builder.defineListAllowEmpty("Chest Part Effect Thresholds", List.of(0.2), Config::validatePercentDouble);
			builder.pop();
			builder.push("legs");
			legsPartEffects = builder.defineListAllowEmpty("Legs Part Effects", List.of(LegendarySurvivalOverhaul.MOD_ID + ":hard_falling"), Config::validateEffectName);
			legsPartEffectAmplifiers = builder.defineListAllowEmpty("Legs Part Effect Amplifiers", List.of(0), Config::validatePositiveInt);
			legsPartEffectThresholds = builder.defineListAllowEmpty("Legs Part Effect Thresholds", List.of(0.2), Config::validatePercentDouble);
			bothLegsPartEffects = builder.defineListAllowEmpty("Both Legs Part Effects", List.of(LegendarySurvivalOverhaul.MOD_ID + ":hard_falling"), Config::validateEffectName);
			bothLegsPartEffectAmplifiers = builder.defineListAllowEmpty("Both Legs Part Effect Amplifiers", List.of(1), Config::validatePositiveInt);
			bothLegsPartEffectThresholds = builder.defineListAllowEmpty("Both Legs Part Effect Thresholds", List.of(0.2), Config::validatePercentDouble);
			builder.pop();
			builder.push("feet");
			feetPartEffects = builder.defineListAllowEmpty("Feet Part Effects", Collections.singletonList("minecraft:slowness"), Config::validateEffectName);
			feetPartEffectAmplifiers = builder.defineListAllowEmpty("Feet Part Effect Amplifiers", Collections.singletonList(0), Config::validatePositiveInt);
			feetPartEffectThresholds = builder.defineListAllowEmpty("Feet Part Effect Thresholds", Collections.singletonList(0.2), Config::validatePercentDouble);
			bothFeetPartEffects = builder.defineListAllowEmpty("Both Feet Part Effects", Collections.singletonList("minecraft:slowness"), Config::validateEffectName);
			bothFeetPartEffectAmplifiers = builder.defineListAllowEmpty("Both Feet Part Effect Amplifiers", Collections.singletonList(1), Config::validatePositiveInt);
			bothFeetPartEffectThresholds = builder.defineListAllowEmpty("Both Feet Part Effect Thresholds", Collections.singletonList(0.2), Config::validatePercentDouble);
			builder.pop();
			builder.pop();

			builder.push("integration");
			builder.push("meds_and_herbs");
			morphineSyringeApplyPainkillerAddiction = builder
					.comment(" Painkiller Addiction effect prevents the abusive usage of Painkiller effect that prevents all negative effects from limbs.",
							" Syringe Morphine share the same configuration as Morphine item")
					.define("Syringe Morphine Apply Painkiller Addiction", true);
			builder.pop();

			builder.pop();
			builder.pop();
		}
	}

	private static boolean validateDouble(final Object obj)
	{
		return obj instanceof Double;
	}

	private static boolean validatePositiveInt(final Object obj)
	{
		return obj instanceof final Integer intValue && intValue >= 0;
	}

	private static boolean validatePercentDouble(final Object obj)
	{
		return obj instanceof final Double doubleValue && doubleValue >= 0 && doubleValue <= 1;
	}

	private static boolean validateEffectName(final Object obj)
	{
		return obj instanceof final String effectName && ForgeRegistries.MOB_EFFECTS.containsKey(new ResourceLocation(effectName));
	}

	private static boolean validateEntityType(final Object obj)
	{
		return obj instanceof final String entityName && ForgeRegistries.ENTITY_TYPES.containsKey(new ResourceLocation(entityName));
	}

	private static boolean validateResourceLocationString(final Object obj)
	{
		if (!(obj instanceof String str)) {
			return false;
		}
		return ResourceLocation.tryParse(str) != null;
	}

	public static class Client
	{
		public final ForgeConfigSpec.BooleanValue foodSaturationDisplayed;
		public final ForgeConfigSpec.BooleanValue showVanillaBarAnimationOverlay;

		public final ForgeConfigSpec.EnumValue<EnumUtil.temperatureDisplayMode> temperatureDisplayMode;
		public final ForgeConfigSpec.IntValue temperatureDisplayOffsetX;
		public final ForgeConfigSpec.IntValue temperatureDisplayOffsetY;
		public final ForgeConfigSpec.IntValue bodyTemperatureDisplayOffsetX;
		public final ForgeConfigSpec.IntValue bodyTemperatureDisplayOffsetY;
		public final ForgeConfigSpec.BooleanValue heatTemperatureOverlay;
		public final ForgeConfigSpec.BooleanValue coldTemperatureOverlay;
		public final ForgeConfigSpec.BooleanValue breathingSoundEnabled;
		public final ForgeConfigSpec.DoubleValue coldBreathEffectThreshold;
		public final ForgeConfigSpec.BooleanValue renderTemperatureInFahrenheit;

		public final ForgeConfigSpec.IntValue wetnessIndicatorOffsetX;
		public final ForgeConfigSpec.IntValue wetnessIndicatorOffsetY;

		public final ForgeConfigSpec.IntValue bodyDamageIndicatorOffsetX;
		public final ForgeConfigSpec.IntValue bodyDamageIndicatorOffsetY;
		public final ForgeConfigSpec.DoubleValue bodyDamageIndicatorRenderHealthLimit;

		public final ForgeConfigSpec.IntValue seasonCardsDisplayOffsetX;
		public final ForgeConfigSpec.IntValue seasonCardsDisplayOffsetY;
		public final ForgeConfigSpec.IntValue seasonCardsSpawnDimensionDelayInTicks;
		public final ForgeConfigSpec.IntValue seasonCardsDisplayTimeInTicks;
		public final ForgeConfigSpec.IntValue seasonCardsFadeInInTicks;
		public final ForgeConfigSpec.IntValue seasonCardsFadeOutInTicks;

		public final ForgeConfigSpec.BooleanValue showHydrationTooltip;
		public final ForgeConfigSpec.BooleanValue mergeHydrationAndSaturationTooltip;
		public final ForgeConfigSpec.BooleanValue hydrationSaturationDisplayed;
		public final ForgeConfigSpec.BooleanValue showHydrationExhaustion;
		public final ForgeConfigSpec.BooleanValue lowHydrationEffect;
		public final ForgeConfigSpec.BooleanValue showHydrationBar;
		public final ForgeConfigSpec.BooleanValue showDrinkPreview;
		public final ForgeConfigSpec.IntValue hydrationBarOffsetX;
		public final ForgeConfigSpec.IntValue hydrationBarOffsetY;

		public final ForgeConfigSpec.BooleanValue appendBrokenShieldHeartsToHealthBar;

		Client(ForgeConfigSpec.Builder builder)
		{

			builder.comment(" Options related to the heads up display.",
					" These options will automatically update upon being saved.")
					.push("hud");
			builder.push("general");

			foodSaturationDisplayed = builder
					.comment(" If enabled, the food saturation will be rendered on the Food Bar while the player suffers Cold Hunger Effect (secondary temperature effect).")
					.define("Show Food Saturation Bar", true);

			showVanillaBarAnimationOverlay = builder
					.comment(" Whether the vanilla animation of the Food bar and Hydration bar is rendered. The bar shakes more the lower they are.",
							" This mod render a new food bar as a secondary effect of a cold temperature.",
							" Disable this animation if the temperature secondary effect is enabled to allow a compatibility with other mods rendering the food bar (for example Appleskin).")
					.define("Show Vanilla Bar Animation Overlay", true);
			builder.pop();

			builder.push("temperature");
			temperatureDisplayMode = builder
					.comment(" How temperature is displayed. Accepted values are as follows:",
							"    SYMBOL - Display the player's current temperature as a symbol above the hotbar.",
							"    NONE - Disable the temperature indicator.")
					.defineEnum("Temperature Display Mode", EnumUtil.temperatureDisplayMode.SYMBOL);
			temperatureDisplayOffsetX = builder
					.comment(" The X and Y offset of the temperature indicator. Set both to 0 for no offset.")
					.defineInRange("Temperature Display X Offset", 0, -10000, 10000);
			temperatureDisplayOffsetY = builder
					.defineInRange("Temperature Display Y Offset", 0, -10000, 10000);
			bodyTemperatureDisplayOffsetX = builder
					.comment(" The X and Y offset of the body temperature, shown when thermometer is equipped as a bauble (needs the curios mod).",
							" Set both to 0 for no offset.")
					.defineInRange("Body Temperature Display X Offset", 0, -10000, 10000);
			bodyTemperatureDisplayOffsetY = builder
					.defineInRange("Body Temperature Display Y Offset", 0, -10000, 10000);
			heatTemperatureOverlay = builder
					.comment(" If enabled, player will see a foggy effect when the heat is high.")
					.define("Heat Temperature Overlay", true);
			coldTemperatureOverlay = builder
					.comment(" If enabled, player will see a frost effect when the cold is low.")
					.define("Cold Temperature Overlay", true);
			breathingSoundEnabled = builder
					.comment(" If enabled, breathing sound can be heard while player faces harsh temperatures.")
					.define("Breathing Sound Enabled", true);
			coldBreathEffectThreshold = builder
					.comment(" Temperature threshold below which a cold breath effect is rendered by the player. -1000 disable the feature.")
					.defineInRange("Cold Breath Temperature Threshold", 10.0, -1000, 1000);
			renderTemperatureInFahrenheit = builder
					.comment(" If enabled, render the temperature values in Fahrenheit.")
					.define("Temperature In Fahrenheit", false);
			builder.push("wetness");
			wetnessIndicatorOffsetX = builder
					.comment(" The X and Y offset of the wetness indicator. Set both to 0 for no offset.")
					.defineInRange("Wetness Indicator X Offset", 0, -10000, 10000);
			wetnessIndicatorOffsetY = builder
					.defineInRange("Wetness Indicator Y Offset", 0, -10000, 10000);
			builder.pop();
			builder.pop();

			builder.pop();

			builder.push("body-damage");
			bodyDamageIndicatorOffsetX = builder
					.comment(" The X and Y offset of the body damage indicator. Set both to 0 for no offset.", " By default, render next to the inventory bar.")
					.defineInRange("Body Damage Indicator X Offset", 0, -10000, 10000);
			bodyDamageIndicatorOffsetY = builder.defineInRange("Body Damage Indicator Y Offset", 0, -10000, 10000);
			bodyDamageIndicatorRenderHealthLimit = builder
					.comment(" Limb health threshold below which the body damage indicator is rendered.", " If set to 1.1, the body damage indicator is always rendered.", " If set to 1.0, the body damage indicator is rendered as soon as a limb is wounded.")
					.defineInRange("Body Damage Indicator Limb Health Threshold", 1.0, 0.0, 1.1);
			builder.pop();

			builder.push("season-cards");
			seasonCardsDisplayOffsetX = builder
					.comment(" The X and Y offset of the season cards. Set both to 0 for no offset.", " By default, render first top quarter vertically and centered horizontally.")
					.defineInRange("Season Cards Display X Offset", 0, -10000, 10000);
			seasonCardsDisplayOffsetY = builder
					.defineInRange("Season Cards Display Y Offset", 0, -10000, 10000);
			seasonCardsSpawnDimensionDelayInTicks = builder
					.comment(" The delay before rendering the season card at first player spawn or player dimension change.")
					.defineInRange("Season Cards Delay In Ticks", 80, 0, Integer.MAX_VALUE);
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

			builder.push("tooltip");
			showHydrationTooltip = builder
					.comment(" If enabled, show the hydration values in the item tooltip.")
					.define("Show Hydration Tooltip", true);
			mergeHydrationAndSaturationTooltip = builder
					.comment(" If enabled, show the hydration and the saturation values on the same line in the tooltip.")
					.define("Merge Hydration And Saturation Tooltip", true);
			builder.pop();

			hydrationSaturationDisplayed = builder
					.comment(" Whether the Hydration Saturation is displayed or not.")
					.define("Render the hydration saturation", true);
			lowHydrationEffect = builder
					.comment(" If enabled, player's vision will become blurry when running low on hydration.")
					.define("Low Hydration Effect", true);
			builder.push("hydration-bar");
			showHydrationBar = builder
					.comment(" If enabled, the hydration bar will be displayed.")
					.define("Show Hydration Bar", true);
			showHydrationExhaustion = builder
					.comment(" If enabled, the Hydration Exhaustion will be displayed (grey bar behind the hydration bar).")
					.define("Render the hydration exhaustion", true);
			showDrinkPreview = builder
					.comment(" If enabled, a preview on hydration bar will be displayed if player holds a drink item.")
					.define("Show Drink Preview", true);
			hydrationBarOffsetX = builder
					.comment(" How much the hydration bar is moved to the right.")
					.defineInRange("Hydration Bar Offset X", 0, -10000, 10000);
			hydrationBarOffsetY = builder
					.comment(" How much the hydration bar is moved to the bottom.")
					.defineInRange("Hydration Bar Offset Y", 0, -10000, 10000);
			builder.pop();
			builder.pop();

			builder.push("health-overhaul");
			appendBrokenShieldHeartsToHealthBar = builder
					.comment(" If enabled, will try to append the broken and shield hearts at the end of the Health Bar.",
							" A compat is made with overflowing-bars mod.")
					.define("Append Broken/Shield Hearts To Health Bar", true);
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
		public static boolean naturalRegenerationEnabled;
		public static boolean vanillaFreezeEnabled;
		public static EnumUtil.CompassInfo compassInfoMode;
		public static boolean showCoordinateOnMap;
		public static double initialHealth;
		public static EnumUtil.DifficultyMode difficultyMode;

		// Food
		public static double baseFoodExhaustion;
		public static double sprintingFoodExhaustion;
		public static double onAttackFoodExhaustion;

		// Temperature
		public static boolean temperatureEnabled;
		public static int tempTickTime;
		public static double minTemperatureModification;
		public static double maxTemperatureModification;

		public static boolean dangerousHeatTemperature;
		public static boolean dangerousColdTemperature;
		public static double goldFernChance;

		public static boolean temperatureImmunityOnDeathEnabled;
		public static int temperatureImmunityOnDeathTime;
		public static boolean temperatureImmunityOnFirstSpawnEnabled;
		public static int temperatureImmunityOnFirstSpawnTime;

		public static boolean heatTemperatureSecondaryEffects;
		public static boolean coldTemperatureSecondaryEffects;
		public static double heatThirstEffectModifier;
		public static double coldHungerEffectModifier;

		public static boolean biomeEffectsEnabled;
		public static double biomeDrynessMultiplier;
		public static double biomeTemperatureMultiplier;

		public static double undergroundBiomeTemperatureMultiplier;
		public static int undergroundEffectStartDistanceToWS;
		public static int undergroundEffectEndDistanceToWS;

		public static double rainTemperatureModifier;
		public static double snowTemperatureModifier;

		public static double maxFreezeTemperatureModifier;
		public static int maxFreezeEffectTick;

		public static double altitudeModifier;

		public static double timeModifier;
		public static double biomeTimeMultiplier;
		public static double shadeTimeModifier;
		public static double shadeTimeModifierThreshold;
		public static int tempInfluenceMaximumDist;
		public static double tempInfluenceUpDistMultiplier;
		public static double tempInfluenceOutsideDistMultiplier;
		public static double tempInfluenceInWaterDistMultiplier;
		public static double sprintModifier;
		public static double onFireModifier;

		public static double playerHuddlingModifier;
		public static int playerHuddlingRadius;

		public static boolean wetnessEnabled;
		public static List<? extends String> wetnessImmunityMounts;
		public static double wetMultiplier;
		public static int wetnessTickTimer;
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

		public static double tfcItemHeatMultiplier;
		public static double tfcTemperatureMultiplier;
		
		public static List<? extends String> originsWithWetnessImmunity;
		public static List<? extends String> originsWithHighAltitudeImmunity;
		public static List<? extends String> originsWithOnFireImmunity;

		public static boolean sereneSeasonsEnabled;
		public static boolean ssTropicalSeasonsEnabled;
		public static boolean ssSeasonCardsEnabled;
		public static boolean ssDefaultSeasonEnabled;

		public static double ssEarlySpringModifier;
		public static double ssMidSpringModifier;
		public static double ssLateSpringModifier;

		public static double ssEarlySummerModifier;
		public static double ssMidSummerModifier;
		public static double ssLateSummerModifier;

		public static double ssEarlyAutumnModifier;
		public static double ssMidAutumnModifier;
		public static double ssLateAutumnModifier;

		public static double ssEarlyWinterModifier;
		public static double ssMidWinterModifier;
		public static double ssLateWinterModifier;

		public static double ssEarlyWetSeasonModifier;
		public static double ssMidWetSeasonModifier;
		public static double ssLateWetSeasonModifier;

		public static double earlyDrySeasonModifier;
		public static double midDrySeasonModifier;
		public static double lateDrySeasonModifier;

		public static boolean eclipticSeasonsEnabled;
		public static List<? extends Double> esSpringModifier;
		public static List<? extends Double> esSummerModifier;
		public static List<? extends Double> esAutumnModifier;
		public static List<? extends Double> esWinterModifier;

		// Thirst
		public static boolean thirstEnabled;
		public static boolean dangerousDehydration;
		public static boolean cumulativeThirstEffectDuration;
		public static double dehydrationDamageScaling;
		public static double thirstEffectModifier;
		public static double baseHydrationExhaustion;
		public static double sprintingHydrationExhaustion;
		public static double onJumpHydrationExhaustion;
		public static double onBlockBreakHydrationExhaustion;
		public static double onAttackHydrationExhaustion;
		public static boolean selfWateringCanteenEnabled;
		public static int selfWateringCanteenWetnessIncrease;
		public static int canteenCapacity;
		public static int largeCanteenCapacity;
		public static boolean allowOverridePurifiedWater;
		public static int hydrationLava;
		public static double saturationLava;
		public static boolean glassBottleLootAfterDrink;
		
		public static List<? extends String> originsWithThirstEffectImmunity;
		public static List<? extends String> originsWithExtraThirstExhaustion;
		public static List<? extends String> originsWithLavaDrinking;
		public static int hydrationLavaBlazeborn;
		public static double saturationLavaBlazeborn;
		public static double originsExtraThirstExhaustionValue;
		
		public static boolean thirstEnabledIfVampire;

		// Health Overhaul
		public static boolean healthOverhaulEnabled;
		public static double maxAdditionalHealth;
		public static double maxShieldHealth;
		public static boolean absorptionEffectOverride;
		public static int heartsLostOnDeath;
		public static int permanentHearts;
		public static int resilientHeartsWithBrokenHearts;
		public static double brokenHeartsPerInjuredLimb;
		public static EnumUtil.brokenHeartsPerInjuredLimbMode brokenHeartsPerInjuredLimbMode;

		// Body members damage
		public static boolean localizedBodyDamageEnabled;
		public static double headCriticalShotMultiplier;
		public static double bodyDamageMultiplier;
		public static double bodyHealthRatioRecoveredFromSleep;
		public static double healthRatioRecoveredFromSleep;
		public static double bodyHealingFoodExhaustion;
		public static int minFoodOnBodyHealing;
		public static int painkillerAddictionDuration;

		public static boolean passiveLimbRegenerationOnFullHealth;
		public static List<? extends String> passiveLimbRegenerationEffects;
		public static boolean passiveLimbRegenerationAmplificationEnabled;
		public static double passiveLimbHealthRegenerated;
		public static int passiveLimbRegenerationTickTimer;

		public static double firstAidSuppliesLimbHealthRegenerated;
		public static EnumUtil.limbRegenerationMode limbRegenerationMode;
		public static boolean firstAidSuppliesHealingOverflow;
		public static int firstAidSuppliesTickTimer;
		public static boolean firstAidSuppliesExhaustsFood;
		public static List<? extends String> firstAidSuppliesBoostedOnEffects;
		public static double firstAidSuppliesBoostedTickTimerMultiplier;

		public static EnumUtil.bodyPartHealthMode bodyPartHealthMode;
		public static double headPartHealth;
		public static double armsPartHealth;
		public static double chestPartHealth;
		public static double legsPartHealth;
		public static double feetPartHealth;

		public static double recoveryEffectHealingValue;
		public static int healingHerbsUseTime;
		public static int plasterUseTime;
		public static int bandageUseTime;
		public static int tonicUseTime;
		public static int medkitUseTime;
		public static int morphineUseTime;
		public static int morphinePainkillerTickDuration;

		public static List<? extends String> headPartEffects;
		public static List<? extends Integer> headPartEffectAmplifiers;
		public static List<? extends Double> headPartEffectThresholds;

		public static List<? extends String> armsPartEffects;
		public static List<? extends Integer> armsPartEffectAmplifiers;
		public static List<? extends Double> armsPartEffectThresholds;
		public static List<? extends String> bothArmsPartEffects;
		public static List<? extends Integer> bothArmsPartEffectAmplifiers;
		public static List<? extends Double> bothArmsPartEffectThresholds;

		public static List<? extends String> chestPartEffects;
		public static List<? extends Integer> chestPartEffectAmplifiers;
		public static List<? extends Double> chestPartEffectThresholds;

		public static List<? extends String> legsPartEffects;
		public static List<? extends Integer> legsPartEffectAmplifiers;
		public static List<? extends Double> legsPartEffectThresholds;
		public static List<? extends String> bothLegsPartEffects;
		public static List<? extends Integer> bothLegsPartEffectAmplifiers;
		public static List<? extends Double> bothLegsPartEffectThresholds;

		public static List<? extends String> feetPartEffects;
		public static List<? extends Integer> feetPartEffectAmplifiers;
		public static List<? extends Double> feetPartEffectThresholds;
		public static List<? extends String> bothFeetPartEffects;
		public static List<? extends Integer> bothFeetPartEffectAmplifiers;
		public static List<? extends Double> bothFeetPartEffectThresholds;

		public static boolean morphineSyringeApplyPainkillerAddiction;

		// Client Config
		public static EnumUtil.temperatureDisplayMode temperatureDisplayMode;
		public static int temperatureDisplayOffsetX;
		public static int temperatureDisplayOffsetY;
		public static int bodyTemperatureDisplayOffsetX;
		public static int bodyTemperatureDisplayOffsetY;
		public static boolean heatTemperatureOverlay;
		public static boolean coldTemperatureOverlay;
		public static boolean breathingSoundEnabled;
		public static double coldBreathEffectThreshold;
		public static boolean renderTemperatureInFahrenheit;

		public static boolean foodSaturationDisplayed;
		public static boolean showVanillaBarAnimationOverlay;

		public static int seasonCardsDisplayOffsetX;
		public static int seasonCardsDisplayOffsetY;
		public static int seasonCardsSpawnDimensionDelayInTicks;
		public static int seasonCardsDisplayTimeInTicks;
		public static int seasonCardsFadeInInTicks;
		public static int seasonCardsFadeOutInTicks;

		public static int wetnessIndicatorOffsetX;
		public static int wetnessIndicatorOffsetY;

		public static int bodyDamageIndicatorOffsetX;
		public static int bodyDamageIndicatorOffsetY;
		public static double bodyDamageIndicatorRenderHealthLimit;

		public static boolean showHydrationTooltip;
		public static boolean mergeHydrationAndSaturationTooltip;
		public static boolean hydrationSaturationDisplayed;
		public static boolean lowHydrationEffect;
		public static boolean showHydrationBar;
		public static boolean showHydrationExhaustion;
		public static boolean showDrinkPreview;
		public static int hydrationBarOffsetX;
		public static int hydrationBarOffsetY;

		public static boolean appendBrokenShieldHeartsToHealthBar;

		public static void bakeCommon()
		{
			LegendarySurvivalOverhaul.LOGGER.debug("Load Common configuration from file");
			try
			{
				hideInfoFromDebug = COMMON.hideInfoFromDebug.get();
				naturalRegenerationEnabled = COMMON.naturalRegenerationEnabled.get();
				routinePacketSync = COMMON.routinePacketSync.get();
				vanillaFreezeEnabled = COMMON.vanillaFreezeEnabled.get();
				compassInfoMode = COMMON.compassInfoMode.get();
				showCoordinateOnMap = COMMON.showCoordinateOnMap.get();
				initialHealth = COMMON.initialHealth.get();
				difficultyMode = COMMON.difficultyMode.get();

				baseFoodExhaustion = COMMON.baseFoodExhaustion.get();
				sprintingFoodExhaustion = COMMON.sprintingFoodExhaustion.get();
				onAttackFoodExhaustion = COMMON.onAttackFoodExhaustion.get();

				temperatureEnabled = COMMON.temperatureEnabled.get();
				tempTickTime = COMMON.tempTickTime.get();
				minTemperatureModification = COMMON.minTemperatureModification.get();
				maxTemperatureModification = COMMON.maxTemperatureModification.get();

				dangerousHeatTemperature = COMMON.dangerousHeatTemperature.get();
				dangerousColdTemperature = COMMON.dangerousColdTemperature.get();
				goldFernChance = COMMON.goldFernChance.get();

				temperatureImmunityOnDeathEnabled = COMMON.temperatureImmunityOnDeathEnabled.get();
				temperatureImmunityOnDeathTime = COMMON.temperatureImmunityOnDeathTime.get();
				temperatureImmunityOnFirstSpawnEnabled = COMMON.temperatureImmunityOnFirstSpawnEnabled.get();
				temperatureImmunityOnFirstSpawnTime = COMMON.temperatureImmunityOnFirstSpawnTime.get();

				heatTemperatureSecondaryEffects = COMMON.heatTemperatureSecondaryEffects.get();
				coldTemperatureSecondaryEffects = COMMON.coldTemperatureSecondaryEffects.get();
				heatThirstEffectModifier = COMMON.heatThirstEffectModifier.get();
				coldHungerEffectModifier = COMMON.coldHungerEffectModifier.get();

				rainTemperatureModifier = COMMON.rainTemperatureModifier.get();
				snowTemperatureModifier = COMMON.snowTemperatureModifier.get();

				maxFreezeTemperatureModifier = COMMON.maxFreezeTemperatureModifier.get();
				maxFreezeEffectTick = COMMON.maxFreezeEffectTick.get();

				undergroundBiomeTemperatureMultiplier = COMMON.undergroundBiomeTemperatureMultiplier.get();
				undergroundEffectStartDistanceToWS = COMMON.undergroundEffectStartDistanceToWS.get();
				undergroundEffectEndDistanceToWS = COMMON.undergroundEffectEndDistanceToWS.get();

				altitudeModifier = COMMON.altitudeModifier.get();
				biomeEffectsEnabled = COMMON.biomeEffectsEnabled.get();
				biomeDrynessMultiplier = COMMON.biomeDrynessMultiplier.get();
				biomeTemperatureMultiplier = COMMON.biomeTemperatureMultiplier.get();
				timeModifier = COMMON.timeModifier.get();
				biomeTimeMultiplier = COMMON.biomeTimeMultiplier.get();
				shadeTimeModifier = COMMON.shadeTimeModifier.get();
				shadeTimeModifierThreshold = COMMON.shadeTimeModifierThreshold.get();

				tempInfluenceMaximumDist = COMMON.tempInfluenceMaximumDist.get();
				tempInfluenceUpDistMultiplier = COMMON.tempInfluenceUpDistMultiplier.get();
				tempInfluenceInWaterDistMultiplier = COMMON.tempInfluenceInWaterDistMultiplier.get();
				tempInfluenceOutsideDistMultiplier = COMMON.tempInfluenceOutsideDistMultiplier.get();

				onFireModifier = COMMON.onFireModifier.get();
				sprintModifier = COMMON.sprintModifier.get();

				wetnessEnabled = COMMON.wetnessEnabled.get();
				wetnessImmunityMounts = COMMON.wetnessImmunityMounts.get();
				wetMultiplier = COMMON.wetMultiplier.get();
				wetnessTickTimer = COMMON.wetnessTickTimer.get();
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

				tfcItemHeatMultiplier = COMMON.tfcItemHeatMultiplier.get();
				tfcTemperatureMultiplier = COMMON.tfcTemperatureMultiplier.get();
				
				originsWithWetnessImmunity = COMMON.originsWithWetnessImmunity.get();
				originsWithHighAltitudeImmunity = COMMON.originsWithHighAltitudeImmunity.get();
				originsWithOnFireImmunity = COMMON.originsWithOnFireImmunity.get();

				sereneSeasonsEnabled = COMMON.sereneSeasonsEnabled.get();
				ssTropicalSeasonsEnabled = COMMON.ssTropicalSeasonsEnabled.get();
				ssSeasonCardsEnabled = COMMON.ssSeasonCardsEnabled.get();
				ssDefaultSeasonEnabled = COMMON.ssDefaultSeasonEnabled.get();

				ssEarlySpringModifier = COMMON.ssEarlySpringModifier.get();
				ssMidSpringModifier = COMMON.ssMidSpringModifier.get();
				ssLateSpringModifier = COMMON.ssLateSpringModifier.get();

				ssEarlySummerModifier = COMMON.ssEarlySummerModifier.get();
				ssMidSummerModifier = COMMON.ssMidSummerModifier.get();
				ssLateSummerModifier = COMMON.ssLateSummerModifier.get();

				ssEarlyAutumnModifier = COMMON.ssEarlyAutumnModifier.get();
				ssMidAutumnModifier = COMMON.ssMidAutumnModifier.get();
				ssLateAutumnModifier = COMMON.ssLateAutumnModifier.get();

				ssEarlyWinterModifier = COMMON.ssEarlyWinterModifier.get();
				ssMidWinterModifier = COMMON.ssMidWinterModifier.get();
				ssLateWinterModifier = COMMON.ssLateWinterModifier.get();

				ssEarlyWetSeasonModifier = COMMON.ssEarlyWetSeasonModifier.get();
				ssMidWetSeasonModifier = COMMON.ssMidWetSeasonModifier.get();
				ssLateWetSeasonModifier = COMMON.ssLateWetSeasonModifier.get();

				earlyDrySeasonModifier = COMMON.ssEarlyDrySeasonModifier.get();
				midDrySeasonModifier = COMMON.ssMidDrySeasonModifier.get();
				lateDrySeasonModifier = COMMON.ssLateDrySeasonModifier.get();

				eclipticSeasonsEnabled = COMMON.eclipticSeasonsEnabled.get();
				esSpringModifier = COMMON.esSpringModifier.get();
				esSummerModifier = COMMON.esSummerModifier.get();
				esAutumnModifier = COMMON.esAutumnModifier.get();
				esWinterModifier = COMMON.esWinterModifier.get();

				thirstEnabled = COMMON.thirstEnabled.get();
				dangerousDehydration = COMMON.dangerousDehydration.get();
				cumulativeThirstEffectDuration = COMMON.cumulativeThirstEffectDuration.get();
				dehydrationDamageScaling = COMMON.dehydrationDamageScaling.get();
				thirstEffectModifier = COMMON.thirstEffectModifier.get();

				baseHydrationExhaustion = COMMON.baseHydrationExhaustion.get();
				sprintingHydrationExhaustion = COMMON.sprintingHydrationExhaustion.get();
				onJumpHydrationExhaustion = COMMON.onJumpHydrationExhaustion.get();
				onBlockBreakHydrationExhaustion = COMMON.onBlockBreakHydrationExhaustion.get();
				onAttackHydrationExhaustion = COMMON.onAttackHydrationExhaustion.get();

				selfWateringCanteenEnabled = COMMON.selfWateringCanteenEnabled.get();
				selfWateringCanteenWetnessIncrease = COMMON.selfWateringCanteenWetnessIncrease.get();
				canteenCapacity = COMMON.canteenCapacity.get();
				largeCanteenCapacity = COMMON.largeCanteenCapacity.get();
				allowOverridePurifiedWater = COMMON.allowOverridePurifiedWater.get();

				hydrationLava = COMMON.hydrationLava.get();
				saturationLava = COMMON.saturationLava.get();

				glassBottleLootAfterDrink = COMMON.glassBottleLootAfterDrink.get();

				originsWithThirstEffectImmunity = COMMON.originsWithThirstEffectImmunity.get();
				originsWithExtraThirstExhaustion = COMMON.originsWithExtraThirstExhaustion.get();
				originsWithLavaDrinking = COMMON.originsWithLavaDrinking.get();
				hydrationLavaBlazeborn = COMMON.hydrationLavaBlazeborn.get();
				saturationLavaBlazeborn = COMMON.saturationLavaBlazeborn.get();
				originsExtraThirstExhaustionValue = COMMON.originsExtraThirstExhaustionValue.get();
				
				thirstEnabledIfVampire = COMMON.thirstEnabledIfVampire.get();

				healthOverhaulEnabled = COMMON.healthOverhaulEnabled.get();
				maxAdditionalHealth = COMMON.maxAdditionalHealth.get();
				maxShieldHealth = COMMON.maxShieldHealth.get();
				absorptionEffectOverride = COMMON.absorptionEffectOverride.get();
				heartsLostOnDeath = COMMON.heartsLostOnDeath.get();
				permanentHearts = COMMON.permanentHearts.get();
				resilientHeartsWithBrokenHearts = COMMON.resilientHeartsWithBrokenHearts.get();
				brokenHeartsPerInjuredLimb = COMMON.brokenHeartsPerInjuredLimb.get();
				brokenHeartsPerInjuredLimbMode = COMMON.brokenHeartsPerInjuredLimbMode.get();

				localizedBodyDamageEnabled = COMMON.localizedBodyDamageEnabled.get();
				headCriticalShotMultiplier = COMMON.headCriticalShotMultiplier.get();
				bodyDamageMultiplier = COMMON.bodyDamageMultiplier.get();
				bodyHealthRatioRecoveredFromSleep = COMMON.bodyHealthRatioRecoveredFromSleep.get();
				healthRatioRecoveredFromSleep = COMMON.healthRatioRecoveredFromSleep.get();
				bodyHealingFoodExhaustion = COMMON.bodyHealingFoodExhaustion.get();
				minFoodOnBodyHealing = COMMON.minFoodOnBodyHealing.get();
				painkillerAddictionDuration = COMMON.painkillerAddictionDuration.get();

				passiveLimbRegenerationOnFullHealth = COMMON.passiveLimbRegenerationOnFullHealth.get();
				passiveLimbRegenerationEffects = COMMON.passiveLimbRegenerationEffects.get();
				passiveLimbRegenerationAmplificationEnabled = COMMON.passiveLimbRegenerationAmplificationEnabled.get();
				passiveLimbHealthRegenerated = COMMON.passiveLimbHealthRegenerated.get();
				passiveLimbRegenerationTickTimer = COMMON.passiveLimbRegenerationTickTimer.get();

				firstAidSuppliesLimbHealthRegenerated = COMMON.firstAidSuppliesLimbHealthRegenerated.get();
				limbRegenerationMode = COMMON.firstAidSuppliesLimbRegenerationMode.get();
				firstAidSuppliesHealingOverflow = COMMON.firstAidSuppliesHealingOverflow.get();
				firstAidSuppliesTickTimer = COMMON.firstAidSuppliesTickTimer.get();
				firstAidSuppliesExhaustsFood = COMMON.firstAidSuppliesExhaustsFood.get();
				firstAidSuppliesBoostedOnEffects = COMMON.firstAidSuppliesBoostedOnEffects.get();
				firstAidSuppliesBoostedTickTimerMultiplier = COMMON.firstAidSuppliesBoostedTickTimerMultiplier.get();

				recoveryEffectHealingValue = COMMON.recoveryEffectHealingValue.get();
				healingHerbsUseTime = COMMON.healingHerbsUseTime.get();
				plasterUseTime = COMMON.plasterUseTime.get();
				bandageUseTime = COMMON.bandageUseTime.get();
				tonicUseTime = COMMON.tonicUseTime.get();
				medkitUseTime = COMMON.medkitUseTime.get();
				morphineUseTime = COMMON.morphineUseTime.get();
				morphinePainkillerTickDuration = COMMON.morphinePainkillerTickDuration.get();

				bodyPartHealthMode = COMMON.bodyPartHealthMode.get();
				headPartHealth = COMMON.headPartHealth.get();
				chestPartHealth = COMMON.chestPartHealth.get();
				armsPartHealth = COMMON.armsPartHealth.get();
				legsPartHealth = COMMON.legsPartHealth.get();
				feetPartHealth = COMMON.feetPartHealth.get();

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

				morphineSyringeApplyPainkillerAddiction = COMMON.morphineSyringeApplyPainkillerAddiction.get();
			}
			catch (Exception e)
			{
				LegendarySurvivalOverhaul.LOGGER.warn("An exception was caused trying to load the common config for Legendary Survival Overhaul");
				LegendarySurvivalOverhaul.LOGGER.warn(e.getStackTrace());
			}
		}

		public static void bakeClient()
		{
			LegendarySurvivalOverhaul.LOGGER.debug("Load Client configuration from file");
			try
			{
				temperatureDisplayMode = CLIENT.temperatureDisplayMode.get();
				temperatureDisplayOffsetX = CLIENT.temperatureDisplayOffsetX.get();
				temperatureDisplayOffsetY = CLIENT.temperatureDisplayOffsetY.get();
				bodyTemperatureDisplayOffsetX = CLIENT.bodyTemperatureDisplayOffsetX.get();
				bodyTemperatureDisplayOffsetY = CLIENT.bodyTemperatureDisplayOffsetY.get();
				heatTemperatureOverlay = CLIENT.heatTemperatureOverlay.get();
				coldTemperatureOverlay = CLIENT.coldTemperatureOverlay.get();
				breathingSoundEnabled = CLIENT.breathingSoundEnabled.get();
				coldBreathEffectThreshold = CLIENT.coldBreathEffectThreshold.get();
				renderTemperatureInFahrenheit = CLIENT.renderTemperatureInFahrenheit.get();

				foodSaturationDisplayed = CLIENT.foodSaturationDisplayed.get();
				showVanillaBarAnimationOverlay = CLIENT.showVanillaBarAnimationOverlay.get();

				seasonCardsDisplayOffsetX = CLIENT.seasonCardsDisplayOffsetX.get();
				seasonCardsDisplayOffsetY = CLIENT.seasonCardsDisplayOffsetY.get();
				seasonCardsSpawnDimensionDelayInTicks = CLIENT.seasonCardsSpawnDimensionDelayInTicks.get();
				seasonCardsDisplayTimeInTicks = CLIENT.seasonCardsDisplayTimeInTicks.get();
				seasonCardsFadeInInTicks = CLIENT.seasonCardsFadeInInTicks.get();
				seasonCardsFadeOutInTicks = CLIENT.seasonCardsFadeOutInTicks.get();

				wetnessIndicatorOffsetX = CLIENT.wetnessIndicatorOffsetX.get();
				wetnessIndicatorOffsetY = CLIENT.wetnessIndicatorOffsetY.get();

				bodyDamageIndicatorOffsetX = CLIENT.bodyDamageIndicatorOffsetX.get();
				bodyDamageIndicatorOffsetY = CLIENT.bodyDamageIndicatorOffsetY.get();
				bodyDamageIndicatorRenderHealthLimit = CLIENT.bodyDamageIndicatorRenderHealthLimit.get();

				hydrationSaturationDisplayed = CLIENT.hydrationSaturationDisplayed.get();
				showHydrationTooltip = CLIENT.showHydrationTooltip.get();
				mergeHydrationAndSaturationTooltip = CLIENT.mergeHydrationAndSaturationTooltip.get();
				lowHydrationEffect = CLIENT.lowHydrationEffect.get();
				showHydrationBar = CLIENT.showHydrationBar.get();
				showHydrationExhaustion = CLIENT.showHydrationExhaustion.get();
				showDrinkPreview = CLIENT.showDrinkPreview.get();
				hydrationBarOffsetX = CLIENT.hydrationBarOffsetX.get();
				hydrationBarOffsetY = CLIENT.hydrationBarOffsetY.get();

				appendBrokenShieldHeartsToHealthBar = CLIENT.appendBrokenShieldHeartsToHealthBar.get();
			}
			catch (Exception e)
			{
				LegendarySurvivalOverhaul.LOGGER.warn("An exception was caused trying to load the client config for Legendary Survival Overhaul.");
				LegendarySurvivalOverhaul.LOGGER.warn(e.getStackTrace());
			}
		}
	}
}