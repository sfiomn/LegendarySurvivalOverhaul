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
import icey.survivaloverhaul.client.hud.stamina.StaminaDisplayEnum;
import icey.survivaloverhaul.client.hud.temperature.TemperatureDisplayEnum;
import icey.survivaloverhaul.config.json.TemperatureConfig;

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
		Path configPath = FMLPaths.CONFIGDIR.get();
		Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "survivaloverhaul");
		
		try
		{
			Files.createDirectory(modConfigPath);
		}
		catch (FileAlreadyExistsException e) {}
		catch (IOException e)
		{
			Main.LOGGER.error("Failed to create Survival Overhaul config directory");
			e.printStackTrace();
		}
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC, "survivaloverhaul/survivaloverhaul-client.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC, "survivaloverhaul/survivaloverhaul-common.toml");
		
		TemperatureConfig.init(modConfigPath.toFile());
	}
	
	public static class Common
	{
		public final ForgeConfigSpec.ConfigValue<Boolean> temperatureEnabled;
		public final ForgeConfigSpec.ConfigValue<Boolean> thirstEnabled;
		public final ForgeConfigSpec.ConfigValue<Boolean> staminaEnabled;
		
		// Temperature
		public final ForgeConfigSpec.ConfigValue<Boolean> dangerousTemperature;
		public final ForgeConfigSpec.ConfigValue<Boolean> temperatureSecondaryEffects;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> biomeEffectsEnabled;
		public final ForgeConfigSpec.ConfigValue<Double> biomeTemperatureMultiplier;

		public final ForgeConfigSpec.ConfigValue<Integer> minTickRate;
		public final ForgeConfigSpec.ConfigValue<Integer> maxTickRate;
		public final ForgeConfigSpec.ConfigValue<Integer> routinePacketSync;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> seasonTemperatureEffects;
		
		// Thirst
		public final ForgeConfigSpec.ConfigValue<Boolean> temperatureAffectsThirst;
		public final ForgeConfigSpec.ConfigValue<Integer> maxCanteenDrinks;
		public final ForgeConfigSpec.ConfigValue<Integer> maxNetheriteCanteenDrinks;
		
		Common(ForgeConfigSpec.Builder builder)
		{
			builder.comment("Options related to enabling/disabling specific features").push("core");
			thirstEnabled = builder
					.comment("Whether or not the thirst system is enabled.")
					.define("Thirst Enabled", true);
			temperatureEnabled = builder
					.comment("Whether or not the temperature system is enabled.")
					.define("Temperature Enabled", true);
			staminaEnabled = builder
					.comment("Whether or not the stamina system is enabled.")
					.define("Stamina Enabled", true);
			builder.pop();
			
			builder.comment("Options related to the temperature system").push("temperature");
			dangerousTemperature = builder
					.comment("If enabled, players will directly take damage from the effects of temperature.")
					.define("Dangerous Temperature Effects", true);
			temperatureSecondaryEffects = builder
					.comment("If enabled, players will also recieve other effects from their current temperature state.")
					.define("Secondary Temperature Effects", true);
			
			builder.push("biomes");
			biomeTemperatureMultiplier = builder
					.comment("How much a biome's temperature effects are multiplied.")
					.define("Biome Temperature Multiplier", 1.0d);
			biomeEffectsEnabled = builder
					.comment("Whether or not biomes will have an effect on a player's temperature.")
					.define("Biomes affect Temperature", true);
			builder.pop();
			
			builder.push("advanced");
			
			builder.push("tickrate");
			maxTickRate = builder
					.comment("Maximum amount of time between temperature ticks.")
					.define("Maximum Temperature Tickrate", 200);
			minTickRate = builder
					.comment("Minimum amount of time between temperature ticks.")
					.define("Minimum Temperature Tickrate", 50);
			builder.pop();
			
			builder.comment("How often player temperature and thirst are regularly synced between the client and server, in ticks");
			builder.comment("Lower values will increase accuracy at the cost of performance");
			routinePacketSync = builder
					.define("Routine Packet Sync", 30);
			builder.pop();
			
			builder.push("compat");
			seasonTemperatureEffects = builder
					.comment("If Serene Seasons or Better Weather is installed, then seasons will have an effect on the player's temperature.")
					.define("Seasons affect Temperature", true);
			builder.pop();
			builder.pop();

			builder.comment("Options related to the thirst system").push("thirst");
			temperatureAffectsThirst = builder
					.comment("Whether or not temperature affects thirst. Only relevant if temperature is also enabled.")
					.define("Temperature Affects Thirst", true);
			
			builder.comment("Maximum amount of drinks in the canteen.");
			builder.comment("Default is 3.");
			maxCanteenDrinks = builder
					.define("Maximum Canteen Drinks", 3);
			
			builder.comment("Maximum amount of drinks in the netherite canteen.");
			builder.comment("Default is 3.");
			maxNetheriteCanteenDrinks = builder
					.define("Maximum Netherite Canteen Drinks", 3);
			
			builder.pop();
		}
	}
	
	public static class Client
	{
		public final ForgeConfigSpec.ConfigValue<TemperatureDisplayEnum> temperatureDisplayMode;
		public final ForgeConfigSpec.ConfigValue<Integer> temperatureDisplayOffsetX;
		public final ForgeConfigSpec.ConfigValue<Integer> temperatureDisplayOffsetY;
		
		public final ForgeConfigSpec.ConfigValue<StaminaDisplayEnum> staminaDisplayMode;
		public final ForgeConfigSpec.ConfigValue<Integer> staminaDisplayOffsetX;
		public final ForgeConfigSpec.ConfigValue<Integer> staminaDisplayOffsetY;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> climbingKeyIsToggle;
		
		Client(ForgeConfigSpec.Builder builder)
		{
			
			builder.comment("Options related to the heads up display").push("hud");
			
			builder.push("temperature");
			temperatureDisplayMode = builder
					.comment("How temperature is displayed. Accepted values are \"SYMBOL,\" and \"NONE.\"")
					.define("Temperature Display Mode", TemperatureDisplayEnum.SYMBOL);
			temperatureDisplayOffsetX = builder
					.comment("The X and Y offset of the temperature indicator. Set to 0 for no offset.")
					.define("Temperature Display X Offset", 0);
			temperatureDisplayOffsetY = builder
					.define("Temperature Display Y Offset", 0);
			builder.pop();
			
			builder.push("stamina");
			staminaDisplayMode = builder
					.comment("How stamina is displayed. Accepted values are \"ABOVE_ARMOR,\" \"BAR,\" and \"NONE.\"")
					.define("Stamina Display Mode", StaminaDisplayEnum.ABOVE_ARMOR);
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
		}
	}
	
	public static class Server
	{
		
		
		Server(ForgeConfigSpec.Builder builder)
		{
			
		}
	}
	
	public static class BakedConfigValues
	{
		public static boolean temperatureEnabled;
		public static boolean thirstEnabled;
		public static boolean staminaEnabled;
		
		public static boolean dangerousTemperature;
		public static boolean temperatureSecondaryEffects;
		
		public static boolean biomeEffectsEnabled;
		public static double biomeTemperatureMultiplier;
		
		public static int minTickRate;
		public static int maxTickRate;
		public static int routinePacketSync;
		
		public static boolean seasonTemperatureEffects;
		
		public static boolean temperatureAffectsThirst;
		public static int maxCanteenDrinks;
		public static int maxNetheriteCanteenDrinks;
		
		public static TemperatureDisplayEnum temperatureDisplayMode;
		public static int temperatureDisplayOffsetX;
		public static int temperatureDisplayOffsetY;

		public static StaminaDisplayEnum staminaDisplayMode;
		public static int staminaDisplayOffsetX;
		public static int staminaDisplayOffsetY;

		public static boolean climbingKeyIsToggle;
		
		public static void bakeCommon()
		{
			try
			{
				temperatureEnabled = COMMON.temperatureEnabled.get();
				thirstEnabled = COMMON.thirstEnabled.get();
				staminaEnabled = COMMON.staminaEnabled.get();
				
				dangerousTemperature = COMMON.dangerousTemperature.get();
				temperatureSecondaryEffects = COMMON.temperatureSecondaryEffects.get();

				biomeEffectsEnabled = COMMON.biomeEffectsEnabled.get();
				biomeTemperatureMultiplier = COMMON.biomeTemperatureMultiplier.get();

				minTickRate = COMMON.minTickRate.get();
				maxTickRate = COMMON.maxTickRate.get();
				routinePacketSync = COMMON.routinePacketSync.get();

				seasonTemperatureEffects = COMMON.seasonTemperatureEffects.get();

				temperatureAffectsThirst = COMMON.temperatureAffectsThirst.get();
				maxCanteenDrinks = COMMON.maxCanteenDrinks.get();
				maxNetheriteCanteenDrinks = COMMON.maxNetheriteCanteenDrinks.get();
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
				temperatureDisplayMode = CLIENT.temperatureDisplayMode.get();
				temperatureDisplayOffsetX = CLIENT.temperatureDisplayOffsetX.get();
				temperatureDisplayOffsetY = CLIENT.temperatureDisplayOffsetY.get();
				
				staminaDisplayMode = CLIENT.staminaDisplayMode.get();
				staminaDisplayOffsetX = CLIENT.staminaDisplayOffsetX.get();
				staminaDisplayOffsetY = CLIENT.staminaDisplayOffsetY.get();
				
				climbingKeyIsToggle = CLIENT.climbingKeyIsToggle.get();
			}
			catch (Exception e)
			{
				Main.LOGGER.warn("An exception was caused trying to load the client config for Survival Overhaul.");
				e.printStackTrace();
			}
		}
	}
}
