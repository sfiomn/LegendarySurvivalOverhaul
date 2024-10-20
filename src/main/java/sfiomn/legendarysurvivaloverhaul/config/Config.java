package sfiomn.legendarysurvivaloverhaul.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureDisplayEnum;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfigRegistration;

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
		for (Path configPath: new Path[]{LegendarySurvivalOverhaul.modConfigPath,
				LegendarySurvivalOverhaul.modConfigPath,
				LegendarySurvivalOverhaul.modIntegrationConfigJsons}) {
			try {
				Files.createDirectory(configPath);
			} catch (FileAlreadyExistsException ignored) {
			} catch (IOException e) {
				LegendarySurvivalOverhaul.LOGGER.error("Failed to create Legendary Survival Overhaul config directory " + configPath);
				e.printStackTrace();
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
		public final ForgeConfigSpec.DoubleValue baseFoodExhaustion;

		// Temperature
		public final ForgeConfigSpec.BooleanValue temperatureEnabled;
		public final ForgeConfigSpec.IntValue tempTickTime;
		public final ForgeConfigSpec.DoubleValue minTemperatureModification;
		public final ForgeConfigSpec.DoubleValue maxTemperatureModification;
		public final ForgeConfigSpec.BooleanValue dangerousHeatTemperature;
		public final ForgeConfigSpec.BooleanValue dangerousColdTemperature;
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

		public final ForgeConfigSpec.DoubleValue altitudeModifier;
		public final ForgeConfigSpec.DoubleValue sprintModifier;
		public final ForgeConfigSpec.DoubleValue onFireModifier;

		public final ForgeConfigSpec.BooleanValue wetnessEnabled;
		public final ForgeConfigSpec.DoubleValue wetMultiplier;
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

		public final ForgeConfigSpec.BooleanValue seasonTemperatureEffects;
		public final ForgeConfigSpec.BooleanValue tropicalSeasonsEnabled;
		public final ForgeConfigSpec.BooleanValue seasonCardsEnabled;
		public final ForgeConfigSpec.BooleanValue defaultSeasonEnabled;

		public final ForgeConfigSpec.DoubleValue earlySpringModifier;
		public final ForgeConfigSpec.DoubleValue midSpringModifier;
		public final ForgeConfigSpec.DoubleValue lateSpringModifier;

		public final ForgeConfigSpec.DoubleValue earlySummerModifier;
		public final ForgeConfigSpec.DoubleValue midSummerModifier;
		public final ForgeConfigSpec.DoubleValue lateSummerModifier;

		public final ForgeConfigSpec.DoubleValue earlyAutumnModifier;
		public final ForgeConfigSpec.DoubleValue midAutumnModifier;
		public final ForgeConfigSpec.DoubleValue lateAutumnModifier;

		public final ForgeConfigSpec.DoubleValue earlyWinterModifier;
		public final ForgeConfigSpec.DoubleValue midWinterModifier;
		public final ForgeConfigSpec.DoubleValue lateWinterModifier;

		public final ForgeConfigSpec.DoubleValue earlyWetSeasonModifier;
		public final ForgeConfigSpec.DoubleValue midWetSeasonModifier;
		public final ForgeConfigSpec.DoubleValue lateWetSeasonModifier;

		public final ForgeConfigSpec.DoubleValue earlyDrySeasonModifier;
		public final ForgeConfigSpec.DoubleValue midDrySeasonModifier;
		public final ForgeConfigSpec.DoubleValue lateDrySeasonModifier;

		// Thirst
		public final ForgeConfigSpec.BooleanValue thirstEnabled;
		public final ForgeConfigSpec.BooleanValue dangerousDehydration;
		public final ForgeConfigSpec.BooleanValue cumulativeThirstEffectDuration;
		public final ForgeConfigSpec.DoubleValue dehydrationDamageScaling;
		public final ForgeConfigSpec.DoubleValue thirstEffectModifier;
		public final ForgeConfigSpec.DoubleValue baseThirstExhaustion;
		public final ForgeConfigSpec.DoubleValue sprintingThirstExhaustion;
		public final ForgeConfigSpec.DoubleValue onJumpThirstExhaustion;
		public final ForgeConfigSpec.DoubleValue onBlockBreakThirstExhaustion;
		public final ForgeConfigSpec.DoubleValue onAttackThirstExhaustion;
		public final ForgeConfigSpec.IntValue canteenCapacity;
		public final ForgeConfigSpec.IntValue largeCanteenCapacity;
		public final ForgeConfigSpec.BooleanValue allowOverridePurifiedWater;
		public final ForgeConfigSpec.IntValue hydrationLava;
		public final ForgeConfigSpec.DoubleValue saturationLava;
		public final ForgeConfigSpec.BooleanValue glassBottleLootAfterDrink;
		public final ForgeConfigSpec.IntValue hydrationLavaBlazeborn;
		public final ForgeConfigSpec.DoubleValue saturationLavaBlazeborn;
		public final ForgeConfigSpec.DoubleValue extraThirstExhaustionShulk;
		public final ForgeConfigSpec.DoubleValue extraThirstExhaustionPhantom;
		public final ForgeConfigSpec.BooleanValue thirstEnabledIfVampire;

		// Heart Fruits
		public final ForgeConfigSpec.BooleanValue heartFruitsEnabled;

		public final ForgeConfigSpec.IntValue heartsLostOnDeath;
		public final ForgeConfigSpec.IntValue maxAdditionalHearts;

		public final ForgeConfigSpec.IntValue additionalHeartsPerFruit;
		public final ForgeConfigSpec.BooleanValue heartFruitsGiveRegen;

		// Localized Body Damage
		public final ForgeConfigSpec.BooleanValue localizedBodyDamageEnabled;
		public final ForgeConfigSpec.DoubleValue headCriticalShotMultiplier;
		public final ForgeConfigSpec.DoubleValue bodyDamageMultiplier;
		public final ForgeConfigSpec.DoubleValue bodyHealthRatioRecoveredFromSleep;
		public final ForgeConfigSpec.DoubleValue healthRatioRecoveredFromSleep;

		public final ForgeConfigSpec.ConfigValue<String> bodyPartHealthMode;
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

		public final ForgeConfigSpec.IntValue healingHerbsUseTime;
		public final ForgeConfigSpec.IntValue plasterUseTime;
		public final ForgeConfigSpec.IntValue bandageUseTime;
		public final ForgeConfigSpec.IntValue tonicUseTime;
		public final ForgeConfigSpec.IntValue medikitUseTime;

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
			naturalRegenerationEnabled = builder
					.comment(" If enabled, the player can regenerate health naturally if their hunger is full enough (doesn't affect external healing, such as golden apples, the Regeneration effect, etc.)")
					.define("Natural Regeneration Enabled", false);
			vanillaFreezeEnabled = builder
					.comment(" If enabled, the player suffers vanilla freeze when inside powder snow.")
					.define("Vanilla Freeze Enabled", false);

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
			dangerousHeatTemperature = builder
					.comment(" If enabled, players will take damage from the effects of high temperature.")
					.define("Dangerous Heat Temperature Effects", true);
			dangerousColdTemperature = builder
					.comment(" If enabled, players will take damage from the effects of low temperature.")
					.define("Dangerous Cold Temperature Effects", true);

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
					.comment(" How much thirst exhaustion will be added every 50 ticks with no amplification effect.")
					.defineInRange("Heat Thirst Effect Modifier", 0.1d, 0, 1000.0d);
			coldHungerEffectModifier = builder
					.comment(" How much food exhaustion will be added every 50 ticks with no amplification effect.",
							" As reference, the hunger effect add 0.025 food exhaustion every 50 ticks.")
					.defineInRange("Cold Hunger Modifier", 0.05d, 0, 1000.0d);
			builder.pop();

			onFireModifier = builder
					.comment(" How much of an effect being on fire has on a player's temperature.")
					.defineInRange("Player On Fire Modifier", 12.5, -1000, 1000);
			sprintModifier = builder
					.comment(" How much of an effect sprinting has on a player's temperature.")
					.defineInRange("Player Sprint Modifier", 1.5, -1000, 1000);
			altitudeModifier = builder
					.comment(" How much the effects of the player's altitude on temperature are multiplied starting at Y 64.",
							" Each 64 blocks further from Y 64 will reduce player's temperature by this value.")
					.defineInRange("Altitude Modifier", -5.0, -1000, 1000);

			builder.push("wetness");
			wetnessEnabled = builder
					.comment(" Enable the wetness mechanic.")
					.define("Wetness Enabled", true);

			wetMultiplier = builder
					.comment(" How much being wet influences the player's temperature.")
					.defineInRange("Wetness Modifier", -10.0, -1000, 1000);

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
					.defineInRange("Biome Temperature Multiplier", 16.0d, 0.0d, 1000);
			biomeEffectsEnabled = builder
					.comment(" Whether biomes will have an effect on a player's temperature.")
					.define("Biomes affect Temperature", true);
			biomeDrynessMultiplier = builder
					.comment(" How much hot biome's dryness will make nights really cold.",
							" Affects only dry (minecraft down fall <0.2) and hot biome.",
							" 0 means no dryness effect; 0.5 means the biome temp will be divided by 2 at the middle of the night.")
					.defineInRange("Biome's Dryness Multiplier", 0.8d, 0, 1);
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
					.comment(" How much of an effect rain has on temperature.")
					.defineInRange("Rain Temperature Modifier", -2.0, -1000, 1000);
			snowTemperatureModifier = builder
					.comment(" How much of an effect snow has on temperature.")
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
							" Only effective in hot biomes and during day time!")
					.defineInRange("Shade Time Modifier", -6.0, -1000, 1000);
			builder.pop();

			builder.comment(" Temperature coat adds temperature effects on armors by using the sewing table.",
							" Adaptive means the coating will maintain the player's temperature temperate.")
					.push("coat");

			builder.comment(" Add an adaptive heating effect on armors.").push("heating");
			heatingCoat1Modifier = builder.defineInRange("Heating Coat I", 2.0d, 0, 1000.0d);
			heatingCoat2Modifier = builder.defineInRange("Heating Coat II", 3.0d, 0, 1000.0d);
			heatingCoat3Modifier = builder.defineInRange("Heating Coat III", 4.0d, 0, 1000.0d);
			builder.pop();

			builder.comment(" Add an adaptive cooling effect on armors.").push("cooling");
			coolingCoat1Modifier = builder.defineInRange("Cooling Coat I", 2.0d, 0, 1000.0d);
			coolingCoat2Modifier = builder.defineInRange("Cooling Coat II", 3.0d, 0, 1000.0d);
			coolingCoat3Modifier = builder.defineInRange("Cooling Coat III", 4.0d, 0, 1000.0d);
			builder.pop();

			builder.comment(" Add an adaptive temperature effect on armors that can both heat and cool the player.")
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

			builder.push("seasons");
			seasonTemperatureEffects = builder
					.comment(" If Serene Seasons is installed, then seasons will have an effect on the player's temperature.")
					.define("Seasons affect Temperature", true);
			tropicalSeasonsEnabled = builder
					.comment(" If the tropical seasons are disabled, the normal summer-autumn-winter-spring seasons are applied.",
							" If disabled, dry and wet seasons are applied for hot biomes.")
					.define("Tropical Seasons Enabled", false);
			seasonCardsEnabled = builder
					.comment(" If season cards are enabled, season cards will appear at every season changes.")
					.define("Season Cards Enabled", true);
			defaultSeasonEnabled = builder
					.comment(" If default season is enabled, when serene season defines no season effect in a biome, the normal season temperature will be applied.",
							" If disabled, when serene season defines no season effects, no season temperature will be applied.")
					.define("Default Season Enabled", true);

			builder.comment(" Temperature modifiers per season in temperate biomes." +
							" The value is reached at the middle of the sub season, and smoothly transition from one to another.")
					.push("temperate");
			builder.push("spring");
			earlySpringModifier = builder.defineInRange("Early Spring Modifier", -3.0, -1000, 1000);
			midSpringModifier = builder.defineInRange("Mid Spring Modifier", 0.0, -1000, 1000);
			lateSpringModifier = builder.defineInRange("Late Spring Modifier", 3.0, -1000, 1000);
			builder.pop();

			builder.push("summer");
			earlySummerModifier = builder.defineInRange("Early Summer Modifier", 5.0, -1000, 1000);
			midSummerModifier = builder.defineInRange("Mid Summer Modifier", 8.0, -1000, 1000);
			lateSummerModifier = builder.defineInRange("Late Summer Modifier", 5.0, -1000, 1000);
			builder.pop();

			builder.push("autumn");
			earlyAutumnModifier = builder.defineInRange("Early Autumn Modifier", 3.0, -1000, 1000);
			midAutumnModifier = builder.defineInRange("Mid Autumn Modifier", 0.0, -1000, 1000);
			lateAutumnModifier = builder.defineInRange("Late Autumn Modifier", -3.0, -1000, 1000);
			builder.pop();

			builder.push("winter");
			earlyWinterModifier = builder.defineInRange("Early Winter Modifier", -7.0, -1000, 1000);
			midWinterModifier = builder.defineInRange("Mid Winter Modifier", -12.0, -1000, 1000);
			lateWinterModifier = builder.defineInRange("Late Winter Modifier", -7.0, -1000, 1000);
			builder.pop();
			builder.pop();

			builder.comment(" Temperature modifiers per season in tropical biomes.").push("tropical");
			builder.push("wet-season");
			earlyWetSeasonModifier = builder.defineInRange("Early Wet Season Modifier", -1.0, -1000, 1000);
			midWetSeasonModifier = builder.defineInRange("Mid Wet Season Modifier", -5.0, -1000, 1000);
			lateWetSeasonModifier = builder.defineInRange("Late Wet Season Modifier", -1.0, -1000, 1000);
			builder.pop();

			builder.push("dry-season");
			earlyDrySeasonModifier = builder.defineInRange("Early Dry Season Modifier", 3.0, -1000, 1000);
			midDrySeasonModifier = builder.defineInRange("Mid Dry Season Modifier", 7.0, -1000, 1000);
			lateDrySeasonModifier = builder.defineInRange("Late Dry Season Modifier", 3.0, -1000, 1000);
			builder.pop();
			builder.pop();

			builder.pop();
			builder.pop();
			builder.pop();

			builder.comment(" Options related to thirst").push("thirst");
			dangerousDehydration = builder
					.comment(" If enabled, players will take damage from the complete dehydration.")
					.define("Dangerous Dehydration", true);
			cumulativeThirstEffectDuration = builder
					.comment(" If enabled, each time the player receives a thirst effect, its duration will be added to the thirst effect duration if already on the player.")
					.define("Cumulative Thirst Effect Duration", true);
			builder.push("exhaustion");
			baseThirstExhaustion = builder
					.comment(" Thirst exhausted every 10 ticks.")
					.defineInRange("Base Thirst Exhaustion", 0.03d, 0, 1000.0d);
			sprintingThirstExhaustion = builder
					.comment(" Thirst exhausted when sprinting, replacing the base thirst exhausted.")
					.defineInRange("Sprinting Thirst Exhaustion", 0.1d, 0, 1000.0d);
			onJumpThirstExhaustion = builder
					.comment(" Thirst exhausted on every jump.")
					.defineInRange("On Jump Thirst Exhaustion", 0.15d, 0, 1000.0d);
			onBlockBreakThirstExhaustion = builder
					.comment(" Thirst exhausted on every block break.")
					.defineInRange("On Block Break Thirst Exhaustion", 0.07d, 0, 1000.0d);
			onAttackThirstExhaustion = builder
					.comment(" Thirst exhausted on every attack.")
					.defineInRange("On Attack Thirst Exhaustion", 0.3d, 0, 1000.0d);
			builder.pop();
			dehydrationDamageScaling = builder
					.comment(" Scaling of the damages dealt when completely dehydrated. Each tick damage will be increased by this value.")
					.defineInRange("Dehydration Damage Scaling", 0.3d, 0, 1000.0d);
			thirstEffectModifier = builder
					.comment(" How many thirst exhaustion will be added every 50 ticks when the player suffers from not amplified Thirst Effect.",
							" The player will suffer Thirst Effect from dirty water for example.")
					.defineInRange("Thirst Effect Modifier", 0.25d, 0, 1000);
			builder.push("canteen");
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
			builder.push("origins");

			builder.comment(" Temperature won't increase will on fire",
					" Immune to wetness",
					"Can drink lava")
					.push("blazeborn");
			hydrationLavaBlazeborn = builder
					.comment(" Amount of hydration recovered when drinking from lava.")
					.defineInRange("Lava Hydration For Blazeborn", 3, 0, 20);
			saturationLavaBlazeborn = builder
					.comment(" Amount of saturation recovered when drinking from lava.")
					.defineInRange("Lava Saturation For Blazeborn", 1.0, 0, 20);
			builder.pop();

			builder.comment(" Immune to wetness",
					"Immune to Thirst Effect").push("merling");
			builder.pop();

			builder.comment(" Immune to high altitude coldness").push("elytrian");
			builder.pop();

			builder.comment(" Immune to high altitude coldness").push("avian");
			builder.pop();

			builder.comment(" Thirst depletes slightly faster").push("shulk");
			extraThirstExhaustionShulk = builder
					.comment(" Amount of thirst exhaustion added every 20 ticks.")
					.defineInRange("Extra Thirst Exhaustion For Shulk", 0.1, 0, 1000);
			builder.pop();

			builder.comment(" Thirst depletes slightly faster").push("phantom");
			extraThirstExhaustionPhantom = builder
					.comment(" Amount of thirst exhaustion added every 20 ticks.")
					.defineInRange("Extra Thirst Exhaustion For Phantom", 0.1, 0, 1000);
			builder.pop();

			builder.pop();

			builder.push("vampirism");
			thirstEnabledIfVampire = builder
					.comment(" If Vampirism is installed and if thirst enabled while being a vampire, keep the thirst system in addition to the vampiric one.",
							" If disabled, the thirst system will be disabled for vampires.")
					.define("Thirst Enabled If Vampire", false);
			builder.pop();
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
					.defineInRange("Body Part Health Ratio Recovered", 1.0d, 0.0d, 1.0d);
			healthRatioRecoveredFromSleep = builder
					.comment(" How much health ratio are recovered from bed sleeping.")
					.defineInRange("Health Ratio Recovered", 1.0d, 0.0d, 1.0d);

			builder.push("healing-items");
			healingHerbsUseTime = builder
					.comment(" Item use time is ticks.")
					.defineInRange("Healing Herbs Use Time", 20, 0, 1000);
			plasterUseTime = builder
					.defineInRange("Plaster Use Time", 20, 0, 1000);
			bandageUseTime = builder
					.defineInRange("Bandage Use Time", 30, 0, 1000);
			tonicUseTime = builder
					.defineInRange("Tonic Use Time", 50, 0, 1000);
			medikitUseTime = builder
					.defineInRange("Medikit Use Time", 50, 0, 1000);
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

			headPartHealth = builder.defineInRange("Head Part Health", 0.4d, 0.0d, 1000.0d);
			armsPartHealth = builder.comment(" Both arms will have this health.")
					.defineInRange("Arms Part Health", 0.4d, 0.0d, 1000.0d);;
			chestPartHealth = builder.defineInRange("Chest Part Health", 0.6d, 0.0d, 1000.0d);;
			legsPartHealth = builder.comment(" Both legs will have this health.")
					.defineInRange("Legs Part Health", 0.6d, 0.0d, 1000.0d);;
			feetPartHealth = builder.comment(" Both feet will have this health.")
					.defineInRange("Feet Part Health", 0.4d, 0.0d, 1000.0d);;
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
			builder.pop();
		}
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

	public static class Client
	{
		public final ForgeConfigSpec.BooleanValue foodSaturationDisplayed;
		public final ForgeConfigSpec.BooleanValue showVanillaBarAnimationOverlay;

		public final ForgeConfigSpec.EnumValue<TemperatureDisplayEnum> temperatureDisplayMode;
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
		public final ForgeConfigSpec.BooleanValue alwaysShowBodyDamageIndicator;

		public final ForgeConfigSpec.IntValue seasonCardsDisplayOffsetX;
		public final ForgeConfigSpec.IntValue seasonCardsDisplayOffsetY;
		public final ForgeConfigSpec.IntValue seasonCardsSpawnDimensionDelayInTicks;
		public final ForgeConfigSpec.IntValue seasonCardsDisplayTimeInTicks;
		public final ForgeConfigSpec.IntValue seasonCardsFadeInInTicks;
		public final ForgeConfigSpec.IntValue seasonCardsFadeOutInTicks;

		public final ForgeConfigSpec.BooleanValue showHydrationTooltip;
		public final ForgeConfigSpec.BooleanValue mergeHydrationAndSaturationTooltip;
		public final ForgeConfigSpec.BooleanValue thirstSaturationDisplayed;
		public final ForgeConfigSpec.BooleanValue lowHydrationEffect;

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
					.defineEnum("Temperature Display Mode", TemperatureDisplayEnum.SYMBOL);
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
			alwaysShowBodyDamageIndicator = builder
					.comment(" If true, the body damage indicator will always be rendered", " By default, the body damage indicator disappears when no wounded body limbs.")
					.define("Body Damage indicator Always Rendered", false);
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
			showHydrationTooltip = builder
					.comment(" If enabled, show the hydration values in the item tooltip.")
					.define("Show Hydration Tooltip", true);
			mergeHydrationAndSaturationTooltip = builder
					.comment(" If enabled, show the hydration and the saturation values on the same line in the tooltip.")
					.define("Merge Hydration And Saturation Tooltip", true);
			thirstSaturationDisplayed = builder
					.comment(" Whether the Thirst Saturation is displayed or not.")
					.define("Render the thirst saturation", true);
			lowHydrationEffect = builder
					.comment(" If enabled, player's vision will become blurry when running low on hydration.")
					.define("Low Thirst Effect", true);
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
		public static double baseFoodExhaustion;

		// Temperature
		public static boolean temperatureEnabled;
		public static int tempTickTime;
		public static double minTemperatureModification;
		public static double maxTemperatureModification;
		public static boolean temperatureImmunityOnDeathEnabled;
		public static int temperatureImmunityOnDeathTime;
		public static boolean temperatureImmunityOnFirstSpawnEnabled;
		public static int temperatureImmunityOnFirstSpawnTime;

		public static boolean dangerousHeatTemperature;
		public static boolean dangerousColdTemperature;
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
		public static int tempInfluenceMaximumDist;
		public static double tempInfluenceUpDistMultiplier;
		public static double tempInfluenceOutsideDistMultiplier;
		public static double tempInfluenceInWaterDistMultiplier;
		public static double sprintModifier;
		public static double onFireModifier;

		public static double playerHuddlingModifier;
		public static int playerHuddlingRadius;

		public static boolean wetnessEnabled;
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

		public static double tfcItemHeatMultiplier;
		public static double tfcTemperatureMultiplier;

		public static boolean seasonTemperatureEffects;
		public static boolean tropicalSeasonsEnabled;
		public static boolean seasonCardsEnabled;
		public static boolean defaultSeasonEnabled;

		public static double earlySpringModifier;
		public static double midSpringModifier;
		public static double lateSpringModifier;

		public static double earlySummerModifier;
		public static double midSummerModifier;
		public static double lateSummerModifier;

		public static double earlyAutumnModifier;
		public static double midAutumnModifier;
		public static double lateAutumnModifier;

		public static double earlyWinterModifier;
		public static double midWinterModifier;
		public static double lateWinterModifier;

		public static double earlyWetSeasonModifier;
		public static double midWetSeasonModifier;
		public static double lateWetSeasonModifier;

		public static double earlyDrySeasonModifier;
		public static double midDrySeasonModifier;
		public static double lateDrySeasonModifier;

		// Thirst
		public static boolean thirstEnabled;
		public static boolean dangerousDehydration;
		public static boolean cumulativeThirstEffectDuration;
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
		public static int hydrationLava;
		public static double saturationLava;
		public static boolean glassBottleLootAfterDrink;
		public static int hydrationLavaBlazeborn;
		public static double saturationLavaBlazeborn;
		public static double extraThirstExhaustionShulk;
		public static double extraThirstExhaustionPhantom;
		public static boolean thirstEnabledIfVampire;

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

		public static int healingHerbsUseTime;
		public static int plasterUseTime;
		public static int bandageUseTime;
		public static int tonicUseTime;
		public static int medikitUseTime;

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

		// Client Config
		public static TemperatureDisplayEnum temperatureDisplayMode;
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
		public static boolean alwaysShowBodyDamageIndicator;

		public static boolean showHydrationTooltip;
		public static boolean mergeHydrationAndSaturationTooltip;
		public static boolean thirstSaturationDisplayed;
		public static boolean lowHydrationEffect;

		public static void bakeCommon()
		{
			LegendarySurvivalOverhaul.LOGGER.debug("Load Common configuration from file");
			try
			{
				hideInfoFromDebug = COMMON.hideInfoFromDebug.get();
				naturalRegenerationEnabled = COMMON.naturalRegenerationEnabled.get();
				routinePacketSync = COMMON.routinePacketSync.get();
				vanillaFreezeEnabled = COMMON.vanillaFreezeEnabled.get();
				baseFoodExhaustion = COMMON.baseFoodExhaustion.get();

				temperatureEnabled = COMMON.temperatureEnabled.get();
				tempTickTime = COMMON.tempTickTime.get();
				minTemperatureModification = COMMON.minTemperatureModification.get();
				maxTemperatureModification = COMMON.maxTemperatureModification.get();

				temperatureImmunityOnDeathEnabled = COMMON.temperatureImmunityOnDeathEnabled.get();
				temperatureImmunityOnDeathTime = COMMON.temperatureImmunityOnDeathTime.get();
				temperatureImmunityOnFirstSpawnEnabled = COMMON.temperatureImmunityOnFirstSpawnEnabled.get();
				temperatureImmunityOnFirstSpawnTime = COMMON.temperatureImmunityOnFirstSpawnTime.get();

				dangerousHeatTemperature = COMMON.dangerousHeatTemperature.get();
				dangerousColdTemperature = COMMON.dangerousColdTemperature.get();
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

				tempInfluenceMaximumDist = COMMON.tempInfluenceMaximumDist.get();
				tempInfluenceUpDistMultiplier = COMMON.tempInfluenceUpDistMultiplier.get();
				tempInfluenceInWaterDistMultiplier = COMMON.tempInfluenceInWaterDistMultiplier.get();
				tempInfluenceOutsideDistMultiplier = COMMON.tempInfluenceOutsideDistMultiplier.get();

				onFireModifier = COMMON.onFireModifier.get();
				sprintModifier = COMMON.sprintModifier.get();

				wetnessEnabled = COMMON.wetnessEnabled.get();
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

				tfcItemHeatMultiplier = COMMON.tfcItemHeatMultiplier.get();
				tfcTemperatureMultiplier = COMMON.tfcTemperatureMultiplier.get();

				seasonTemperatureEffects = COMMON.seasonTemperatureEffects.get();
				tropicalSeasonsEnabled = COMMON.tropicalSeasonsEnabled.get();
				seasonCardsEnabled = COMMON.seasonCardsEnabled.get();
				defaultSeasonEnabled = COMMON.defaultSeasonEnabled.get();

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
				dangerousDehydration = COMMON.dangerousDehydration.get();
				cumulativeThirstEffectDuration = COMMON.cumulativeThirstEffectDuration.get();
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

				hydrationLava = COMMON.hydrationLava.get();
				saturationLava = COMMON.saturationLava.get();

				glassBottleLootAfterDrink = COMMON.glassBottleLootAfterDrink.get();

				hydrationLavaBlazeborn = COMMON.hydrationLavaBlazeborn.get();
				saturationLavaBlazeborn = COMMON.saturationLavaBlazeborn.get();
				extraThirstExhaustionShulk = COMMON.extraThirstExhaustionShulk.get();
				extraThirstExhaustionPhantom = COMMON.extraThirstExhaustionPhantom.get();
				thirstEnabledIfVampire = COMMON.thirstEnabledIfVampire.get();

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

				healingHerbsUseTime = COMMON.healingHerbsUseTime.get();
				plasterUseTime = COMMON.plasterUseTime.get();
				bandageUseTime = COMMON.bandageUseTime.get();
				tonicUseTime = COMMON.tonicUseTime.get();
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
				alwaysShowBodyDamageIndicator = CLIENT.alwaysShowBodyDamageIndicator.get();

				thirstSaturationDisplayed = CLIENT.thirstSaturationDisplayed.get();
				showHydrationTooltip = CLIENT.showHydrationTooltip.get();
				mergeHydrationAndSaturationTooltip = CLIENT.mergeHydrationAndSaturationTooltip.get();
				lowHydrationEffect = CLIENT.lowHydrationEffect.get();
			}
			catch (Exception e)
			{
				LegendarySurvivalOverhaul.LOGGER.warn("An exception was caused trying to load the client config for Legendary Survival Overhaul.");
				e.printStackTrace();
			}
		}
	}
}