package icey.survivaloverhaul.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.tuple.Pair;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.client.hud.StaminaDisplayEnum;
import icey.survivaloverhaul.client.hud.TemperatureDisplayEnum;
import icey.survivaloverhaul.config.json.JsonConfigRegistration;

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
			Files.createDirectory(Main.modConfigPath);
			Files.createDirectory(Main.modConfigJsons);
		}
		catch (FileAlreadyExistsException e) {}
		catch (IOException e)
		{
			Main.LOGGER.error("Failed to create Survival Overhaul config directories");
			e.printStackTrace();
		}
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC, "survivaloverhaul/survivaloverhaul-client.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC, "survivaloverhaul/survivaloverhaul-common.toml");
		
		JsonConfigRegistration.init(Main.modConfigJsons.toFile());
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
		public final ForgeConfigSpec.ConfigValue<Double> biomeTemperatureMultiplier;
		
		public final ForgeConfigSpec.ConfigValue<Double> timeMultiplier;
		public final ForgeConfigSpec.ConfigValue<Double> biomeTimeMultiplier;
		public final ForgeConfigSpec.ConfigValue<Integer> timeShadeModifier;

		public final ForgeConfigSpec.ConfigValue<Double> wetMultiplier;
		public final ForgeConfigSpec.ConfigValue<Double> altitudeModifier;
		public final ForgeConfigSpec.ConfigValue<Double> sprintModifier;
		public final ForgeConfigSpec.ConfigValue<Double> onFireModifier;
		
		public final ForgeConfigSpec.ConfigValue<Integer> tempInfluenceHorizontalDist;
		public final ForgeConfigSpec.ConfigValue<Integer> tempInfluenceVerticalDist;

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
					.define("Routine Packet Sync", 30);
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
					.comment(/*" If enabled, players will also recieve other effects from their current temperature state.*/ " Currently non-functional.")
					.define("Secondary Temperature Effects", true);

			onFireModifier = builder
					.comment(" How much of an effect being on fire has on a player's temperature.")
					.define("Player On Fire Modifier", 12.5d);
			sprintModifier = builder
					.comment(" How much of an effect sprinting has on a player's temperature.")
					.define("Player Sprint Modifier", 1.5d);
			wetMultiplier = builder
					.comment("How much being wet influences the player's temperature.")
					.define("Wetness Modifier", -7.0d);
			
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
		/*
		public final ForgeConfigSpec.ConfigValue<String> staminaDisplayMode;
		public final ForgeConfigSpec.ConfigValue<Integer> staminaDisplayOffsetX;
		public final ForgeConfigSpec.ConfigValue<Integer> staminaDisplayOffsetY;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> climbingKeyIsToggle;
		*/
		Client(ForgeConfigSpec.Builder builder)
		{
			
			builder.comment("Options related to the heads up display").push("hud");
			
			builder.push("temperature");
			temperatureDisplayMode = builder
					.comment("How temperature is displayed. Accepted values are \"SYMBOL,\" and \"NONE.\"")
					.define("Temperature Display Mode", "SYMBOL");
			temperatureDisplayOffsetX = builder
					.comment("The X and Y offset of the temperature indicator. Set to 0 for no offset.")
					.define("Temperature Display X Offset", 0);
			temperatureDisplayOffsetY = builder
					.define("Temperature Display Y Offset", 0);
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
		
		public static double sprintModifier;
		public static double onFireModifier;
		
		public static double playerHuddlingModifier;
		public static int playerHuddlingRadius;
		
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
				biomeTemperatureMultiplier = COMMON.biomeTemperatureMultiplier.get();
				timeMultiplier = COMMON.timeMultiplier.get();
				biomeTimeMultiplier = COMMON.biomeTimeMultiplier.get();
				timeShadeModifier = COMMON.timeShadeModifier.get();
				
				tempInfluenceHorizontalDist = COMMON.tempInfluenceHorizontalDist.get();
				tempInfluenceVerticalDist = COMMON.tempInfluenceVerticalDist.get();
				minTickRate = COMMON.minTickRate.get();
				maxTickRate = COMMON.maxTickRate.get();
				routinePacketSync = COMMON.routinePacketSync.get();
				
				onFireModifier = COMMON.onFireModifier.get();
				sprintModifier = COMMON.sprintModifier.get();
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
				
				heartFruitsEnabled = COMMON.heartFruitsEnabled.get();
				heartsLostOnDeath = COMMON.heartsLostOnDeath.get();
				maxAdditionalHearts = COMMON.maxAdditionalHearts.get();
				additionalHeartsPerFruit = COMMON.additionalHeartsPerFruit.get();
				heartFruitsGiveRegen = COMMON.heartFruitsGiveRegen.get();
				
				// staminaEnabled = COMMON.staminaEnabled.get();
			}
			catch (Exception e)
			{
				Main.LOGGER.warn("An exception was caused trying to load the common config for Survival Overhaul");
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
				/*
				staminaDisplayMode = StaminaDisplayEnum.getDisplayFromString(CLIENT.staminaDisplayMode.get());
				staminaDisplayOffsetX = CLIENT.staminaDisplayOffsetX.get();
				staminaDisplayOffsetY = CLIENT.staminaDisplayOffsetY.get();
				
				climbingKeyIsToggle = CLIENT.climbingKeyIsToggle.get();
				*/
			}
			catch (Exception e)
			{
				Main.LOGGER.warn("An exception was caused trying to load the client config for Survival Overhaul.");
				e.printStackTrace();
			}
		}
	}
}
