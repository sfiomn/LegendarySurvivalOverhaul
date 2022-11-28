package sfiomn.legendarysurvivaloverhaul.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.client.gui.TemperatureDisplayEnum;
import sfiomn.legendarysurvivaloverhaul.common.capability.wetness.WetnessMode;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfigRegistration;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

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
		catch (FileAlreadyExistsException e) {}
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
		
		// Temperature
		public final ForgeConfigSpec.ConfigValue<Boolean> temperatureEnabled;

		public final ForgeConfigSpec.ConfigValue<Boolean> dangerousTemperature;
		public final ForgeConfigSpec.ConfigValue<Boolean> temperatureSecondaryEffects;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> biomeEffectsEnabled;
		public final ForgeConfigSpec.ConfigValue<Boolean> biomeDrynessEffectEnabled;
		public final ForgeConfigSpec.ConfigValue<Double> biomeTemperatureMultiplier;
		
		public final ForgeConfigSpec.ConfigValue<Double> timeMultiplier;
		public final ForgeConfigSpec.ConfigValue<Double> biomeTimeMultiplier;
		public final ForgeConfigSpec.ConfigValue<Integer> timeShadeModifier;

		public final ForgeConfigSpec.ConfigValue<Double> altitudeModifier;
		public final ForgeConfigSpec.ConfigValue<Double> sprintModifier;
		public final ForgeConfigSpec.ConfigValue<Double> onFireModifier;
		public final ForgeConfigSpec.ConfigValue<Double> enchantmentMultiplier;
		
		public final ForgeConfigSpec.ConfigValue<String> wetnessMode;
		public final ForgeConfigSpec.ConfigValue<Double> wetMultiplier;
		
		public final ForgeConfigSpec.ConfigValue<Integer> tempInfluenceHorizontalDist;
		public final ForgeConfigSpec.ConfigValue<Integer> tempInfluenceVerticalDist;
		public final ForgeConfigSpec.ConfigValue<Integer> tempInfluenceMaximumDist;
		public final ForgeConfigSpec.ConfigValue<Double> tempInfluenceUpDistMultiplier;
		public final ForgeConfigSpec.ConfigValue<Double> tempInfluenceOutsideDistMultiplier;

		public final ForgeConfigSpec.ConfigValue<Double> rainTemperatureModifier;
		public final ForgeConfigSpec.ConfigValue<Double> snowTemperatureModifier;

		public final ForgeConfigSpec.ConfigValue<Double> playerHuddlingModifier;
		public final ForgeConfigSpec.ConfigValue<Integer> playerHuddlingRadius;
		
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
		
		// Heart Fruits
		public final ForgeConfigSpec.ConfigValue<Boolean> heartFruitsEnabled;
		
		public final ForgeConfigSpec.ConfigValue<Integer> heartsLostOnDeath;
		public final ForgeConfigSpec.ConfigValue<Integer> maxAdditionalHearts;
		
		public final ForgeConfigSpec.ConfigValue<Integer> additionalHeartsPerFruit;
		public final ForgeConfigSpec.ConfigValue<Boolean> heartFruitsGiveRegen;
		
		// Stamina
		// public final ForgeConfigSpec.ConfigValue<Boolean> staminaEnabled;
		
		
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
			heartFruitsEnabled = builder
					.comment(" Whether or not heart fruits are functional and generate in-world.")
					.define("Heart Fruits Enabled", true);
			/*
			staminaEnabled = builder
					.comment(" Whether or not the stamina system is enabled.")
					.define("Stamina Enabled", true);
			*/
			builder.push("advanced");
			routinePacketSync = builder
					.comment(new String[] 
							{
									" How often player temperature is regularly synced between the client and server, in ticks.",
									" Lower values will increase accuracy at the cost of performance"
							})
					.defineInRange("Routine Packet Sync", 30, 1, Integer.MAX_VALUE);
			/*
			forceDisableFlightKick = builder
					.comment(new String[] 
							{
									" If true, Survival Overhaul will forcefully enable the \"allow-flight\" parameter in server.properties.",
									" Useful if you keep getting kicked for flying when you're just trying to climb up a wall.",
									" Disable only if you know what you're doing. Requires a server restart to take effect."
							})
					.define("Force Allow Flight", true);
					*/
			builder.pop();
			builder.pop();
			
			builder.comment(" Options related to the temperature system").push("temperature");
			dangerousTemperature = builder
					.comment(/*" If enabled, players will directly take damage from the effects of temperature.*/ " Currently non-functional.")
					.define("Dangerous Temperature Effects", true);
			temperatureSecondaryEffects = builder
					.comment(/*" If enabled, players will also receive other effects from their current temperature state.*/ " Currently non-functional.")
					.define("Secondary Temperature Effects", true);

			onFireModifier = builder
					.comment(" How much of an effect being on fire has on a player's temperature.")
					.define("Player On Fire Modifier", 12.5d);
			sprintModifier = builder
					.comment(" How much of an effect sprinting has on a player's temperature.")
					.define("Player Sprint Modifier", 1.5d);
			enchantmentMultiplier = builder
					.comment(" Increases/decreases the effect that cooling/heating enchantments have on a player's temperature.")
					.define("Enchantment Modifier", 1.0d);
			
			builder.push("wetness");
			wetnessMode = builder
					.comment(new String[] {
							" How a player's \"wetness\" is determined. Accepted values are as follows:",
							"   DISABLE - Disable wetness and any effects on temperature it might have.",
							"   SIMPLE - Wetness is only based on whether you're in water/rain or not. Slightly better in terms of performance.",
							"   DYNAMIC - Wetness can change dynamically based on various conditions, and does not instantly go away when moving out of water.",
							" Any other value will default to DISABLE."
					})
					.define("Wetness Mode", "DYNAMIC");
			
			wetMultiplier = builder
					.comment(" How much being wet influences the player's temperature.")
					.define("Wetness Modifier", -7.0d);
			builder.pop();
			
			builder.push("huddling");
			playerHuddlingModifier = builder
					.comment(new String[] { " How much nearby players increase the ambient temperature by.", " Note that this value stacks!" })
					.define("Player Huddling Modifier", 0.5d);
			playerHuddlingRadius = builder
					.comment(" The radius, in blocks, around which players will add to each other's temperature.")
					.defineInRange("Player Huddling Radius", 1, 0, 10);
			builder.pop();
			
			builder.push("environment");
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
					.comment(" How strongly the effects of time on temperature are multiplied.")
					.defineInRange("Time Multiplier", 2.0d, 0.0d, Double.POSITIVE_INFINITY);
			biomeTimeMultiplier = builder
					.comment(" How strongly different biomes affect temperature, based on time.")
					.defineInRange("Biome Time Multiplier", 1.75d, 1.0d, Double.POSITIVE_INFINITY);
			builder.pop();
			timeShadeModifier = builder
					.comment(new String[] {" Staying in the shade will reduce a player's temperature by this amount.", " Only effective in hot biomes!"} )
					.define("Time Shade Modifier", -3);
			builder.pop();
			builder.pop();
			
			builder.push("advanced");
			tempInfluenceHorizontalDist = builder
					.comment(" Maximum horizontal distance, in blocks, where heat sources will have an effect on temperature.")
					.defineInRange("Temperature Influence Horizontal Distance", 3, 1, 10);
			tempInfluenceVerticalDist = builder
					.comment(" Maximum vertical distance, in blocks, where heat sources will have an effect on temperature.")
					.defineInRange("Temperature Influence Vertical Distance", 2, 1, 10);
			tempInfluenceMaximumDist = builder
					.comment(" Maximum distance, in blocks, where thermal sources will have an effect on temperature.")
					.defineInRange("Temperature Influence Maximum Distance", 15, 1, 30);
			tempInfluenceUpDistMultiplier = builder
					.comment(" How strongly distance above the player is reduced where thermal sources will have an effect on temperature.")
					.comment(" Example max dist is 10, up mult is 0.75 -> max distance is 10 * 0.75 above the player.")
					.defineInRange("Temperature Influence Up Distance Multiplier", 0.75, 0.0, 1.0);
			tempInfluenceOutsideDistMultiplier = builder
					.comment(" How strongly distance outside a structure is reduced where thermal sources will have an effect on temperature.")
					.defineInRange("Temperature Influence Outside Distance Multiplier", 0.5, 0.0, 1.0);
			builder.push("tickrate");
			maxTickRate = builder
					.comment(" Maximum amount of time between temperature ticks.")
					.defineInRange("Maximum Temperature Tickrate", 200, 20, Integer.MAX_VALUE);
			minTickRate = builder
					.comment(" Minimum amount of time between temperature ticks.")
					.defineInRange("Minimum Temperature Tickrate", 20, 20, Integer.MAX_VALUE);
			builder.pop();
			builder.pop();
			
			builder.push("compat");
			
			builder.push("seasons");
			seasonTemperatureEffects = builder
					.comment(new String[] {" If Serene Seasons is installed, then seasons", " will have an effect on the player's temperature."})
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
			
			builder.comment(" Options relating to heart fruits").push("heart-fruits");
			maxAdditionalHearts = builder
					.comment(" Maximum number of additional hearts that can be given by Heart Fruits.")
					.defineInRange("Maximum Additional Hearts", 10, 1, Integer.MAX_VALUE);
			heartsLostOnDeath = builder
					.comment(new String[] {
							" The number of additional hearts lost on death.",
							" Set to -1 to force loss of all additional hearts on death.",
							" Set to 0 to make additional hearts permanent."
					})
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
		public final ForgeConfigSpec.ConfigValue<String> temperatureDisplayMode;
		public final ForgeConfigSpec.ConfigValue<Integer> temperatureDisplayOffsetX;
		public final ForgeConfigSpec.ConfigValue<Integer> temperatureDisplayOffsetY;
		
		public final ForgeConfigSpec.ConfigValue<Integer> wetnessIndicatorOffsetX;
		public final ForgeConfigSpec.ConfigValue<Integer> wetnessIndicatorOffsetY;
		/*
		public final ForgeConfigSpec.ConfigValue<String> staminaDisplayMode;
		public final ForgeConfigSpec.ConfigValue<Integer> staminaDisplayOffsetX;
		public final ForgeConfigSpec.ConfigValue<Integer> staminaDisplayOffsetY;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> climbingKeyIsToggle;
		*/
		Client(ForgeConfigSpec.Builder builder)
		{
			
			builder.comment(new String[] {" Options related to the heads up display.",
					" These options will automatically update upon being saved."
					}).push("hud");
			
			builder.push("temperature");
			temperatureDisplayMode = builder
					.comment(new String[]
							{
									" How temperature is displayed. Accepted values are as follows:",
									"    SYMBOL - Display the player's current temperature as a symbol above the hotbar.",
									"    NONE - Disable the temperature indicator."
							})
					.define("Temperature Display Mode", "SYMBOL");
			temperatureDisplayOffsetX = builder
					.comment(" The X and Y offset of the temperature indicator. Set both to 0 for no offset.")
					.define("Temperature Display X Offset", 0);
			temperatureDisplayOffsetY = builder
					.define("Temperature Display Y Offset", 0);
			builder.push("wetness");
			
			builder.comment(" The X and Y offset of the wetness indicator. Set both to 0 for no offset.").push("offset");
			wetnessIndicatorOffsetX = builder
					.define("Wetness Indicator X Offset", 0);
			wetnessIndicatorOffsetY = builder
					.define("Wetness Indicator Y Offset", 0);
			builder.pop();
			builder.pop();
			builder.pop();
			/*
			builder.push("stamina");
			staminaDisplayMode = builder
					.comment("How stamina is displayed. Accepted values are \"ABOVE_ARMOR,\" \"BAR,\" and \"NONE.\"")
					.define("Stamina Display Mode", "ABOVE_ARMOR");
			staminaDisplayOffsetX = builder
					.comment("The X and Y offset of the stamina meter. Set to 0 for no offset.")
					.define("Stamina Display X Offset", 0);
			staminaDisplayOffsetY = builder
					.define("Stamina Display Y Offset", 0);
			builder.pop();
			builder.pop();
			builder.comment("Options relating to accessibility").push("accessibility");
			climbingKeyIsToggle = builder
					.comment("If true, then you can press your climbing key once to activate it, rather than having to hold it down.")
					.define("Climbing key is toggle", false);
			builder.pop();
			*/
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
		public static boolean temperatureEnabled;
		
		public static boolean dangerousTemperature;
		public static boolean temperatureSecondaryEffects;
		
		public static boolean biomeEffectsEnabled;
		public static boolean biomeDrynessEffectEnabled;
		public static double biomeTemperatureMultiplier;
		
		public static double rainTemperatureModifier;
		public static double snowTemperatureModifier;
		
		public static double altitudeModifier;
		
		public static int minTickRate;
		public static int maxTickRate;
		public static int routinePacketSync;
		
		public static boolean seasonTemperatureEffects;
		
		public static double timeMultiplier;
		public static double biomeTimeMultiplier;
		public static int timeShadeModifier;
		
		public static int tempInfluenceHorizontalDist;
		public static int tempInfluenceVerticalDist;
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
		
		public static boolean heartFruitsEnabled;
		public static int heartsLostOnDeath;
		public static int maxAdditionalHearts;
		public static int additionalHeartsPerFruit;
		public static boolean heartFruitsGiveRegen;
		
		public static boolean staminaEnabled;
		
		// Client Config
		public static TemperatureDisplayEnum temperatureDisplayMode;
		public static int temperatureDisplayOffsetX;
		public static int temperatureDisplayOffsetY;
		
		public static int wetnessIndicatorOffsetX;
		public static int wetnessIndicatorOffsetY;
		/*
		public static StaminaDisplayEnum staminaDisplayMode;
		public static int staminaDisplayOffsetX;
		public static int staminaDisplayOffsetY;

		public static boolean climbingKeyIsToggle;
		*/
		public static void bakeCommon()
		{
			try
			{
				temperatureEnabled = COMMON.temperatureEnabled.get();
				
				dangerousTemperature = COMMON.dangerousTemperature.get();
				temperatureSecondaryEffects = COMMON.temperatureSecondaryEffects.get();
				
				altitudeModifier = COMMON.altitudeModifier.get();
				
				rainTemperatureModifier = COMMON.rainTemperatureModifier.get();
				snowTemperatureModifier = COMMON.snowTemperatureModifier.get();
				
				biomeEffectsEnabled = COMMON.biomeEffectsEnabled.get();
				biomeDrynessEffectEnabled = COMMON.biomeDrynessEffectEnabled.get();
				biomeTemperatureMultiplier = COMMON.biomeTemperatureMultiplier.get();
				timeMultiplier = COMMON.timeMultiplier.get();
				biomeTimeMultiplier = COMMON.biomeTimeMultiplier.get();
				timeShadeModifier = COMMON.timeShadeModifier.get();
				
				tempInfluenceHorizontalDist = COMMON.tempInfluenceHorizontalDist.get();
				tempInfluenceVerticalDist = COMMON.tempInfluenceVerticalDist.get();
				tempInfluenceMaximumDist = COMMON.tempInfluenceMaximumDist.get();
				tempInfluenceUpDistMultiplier = COMMON.tempInfluenceUpDistMultiplier.get();
				tempInfluenceOutsideDistMultiplier = COMMON.tempInfluenceOutsideDistMultiplier.get();
				minTickRate = COMMON.minTickRate.get();
				maxTickRate = COMMON.maxTickRate.get();
				routinePacketSync = COMMON.routinePacketSync.get();
				
				onFireModifier = COMMON.onFireModifier.get();
				sprintModifier = COMMON.sprintModifier.get();
				enchantmentMultiplier = COMMON.enchantmentMultiplier.get();
				
				wetnessMode = WetnessMode.getDisplayFromString(COMMON.wetnessMode.get());
				wetMultiplier = COMMON.wetMultiplier.get();
				
				playerHuddlingModifier = COMMON.playerHuddlingModifier.get();
				playerHuddlingRadius = COMMON.playerHuddlingRadius.get();
				
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
				
				heartFruitsEnabled = COMMON.heartFruitsEnabled.get();
				heartsLostOnDeath = COMMON.heartsLostOnDeath.get();
				maxAdditionalHearts = COMMON.maxAdditionalHearts.get();
				additionalHeartsPerFruit = COMMON.additionalHeartsPerFruit.get();
				heartFruitsGiveRegen = COMMON.heartFruitsGiveRegen.get();
				
				// staminaEnabled = COMMON.staminaEnabled.get();
			}
			catch (Exception e)
			{
				LegendarySurvivalOverhaul.LOGGER.warn("An exception was caused trying to load the common config for Survival Overhaul");
				e.printStackTrace();
			}
		}
		
		public static void bakeClient()
		{
			try
			{
				temperatureDisplayMode = TemperatureDisplayEnum.getDisplayFromString(CLIENT.temperatureDisplayMode.get());
				temperatureDisplayOffsetX = CLIENT.temperatureDisplayOffsetX.get();
				temperatureDisplayOffsetY = CLIENT.temperatureDisplayOffsetY.get();
				
				wetnessIndicatorOffsetX = CLIENT.wetnessIndicatorOffsetX.get();
				wetnessIndicatorOffsetY = CLIENT.wetnessIndicatorOffsetY.get();
				/*
				staminaDisplayMode = StaminaDisplayEnum.getDisplayFromString(CLIENT.staminaDisplayMode.get());
				staminaDisplayOffsetX = CLIENT.staminaDisplayOffsetX.get();
				staminaDisplayOffsetY = CLIENT.staminaDisplayOffsetY.get();
				
				climbingKeyIsToggle = CLIENT.climbingKeyIsToggle.get();
				*/
			}
			catch (Exception e)
			{
				LegendarySurvivalOverhaul.LOGGER.warn("An exception was caused trying to load the client config for Survival Overhaul.");
				e.printStackTrace();
			}
		}
	}
}
