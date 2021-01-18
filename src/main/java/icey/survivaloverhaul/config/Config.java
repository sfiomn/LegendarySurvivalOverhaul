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
		Path configPath = FMLPaths.CONFIGDIR.get();
		Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "survivaloverhaul");
		Path modConfigJsons = Paths.get(modConfigPath.toString(), "json");
		
		try
		{
			Files.createDirectory(modConfigPath);
			Files.createDirectory(modConfigJsons);
		}
		catch (FileAlreadyExistsException e) {}
		catch (IOException e)
		{
			Main.LOGGER.error("Failed to create Survival Overhaul config directories");
			e.printStackTrace();
		}
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC, "survivaloverhaul/survivaloverhaul-client.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC, "survivaloverhaul/survivaloverhaul-common.toml");
		
		JsonConfigRegistration.init(modConfigJsons.toFile());
	}
	
	public static class Common
	{
		public final ForgeConfigSpec.ConfigValue<Boolean> temperatureEnabled;
		public final ForgeConfigSpec.ConfigValue<Boolean> thirstEnabled;
		public final ForgeConfigSpec.ConfigValue<Boolean> staminaEnabled;
		
		// Temperature
		public final ForgeConfigSpec.ConfigValue<Boolean> dangerousTemperature;
		public final ForgeConfigSpec.ConfigValue<Boolean> temperatureSecondaryEffects;
		
		public final ForgeConfigSpec.ConfigValue<Double> maxCoilInfluenceDistance;
		public final ForgeConfigSpec.ConfigValue<Double> coilFullPowerDistance;
		public final ForgeConfigSpec.ConfigValue<Integer> coilInfluence;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> snowArmorSetBonusEnabled;
		public final ForgeConfigSpec.ConfigValue<Boolean> desertArmorSetBonusEnabled;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> biomeEffectsEnabled;
		public final ForgeConfigSpec.ConfigValue<Double> biomeTemperatureMultiplier;
		
		public final ForgeConfigSpec.ConfigValue<Double> timeMultiplier;
		public final ForgeConfigSpec.ConfigValue<Double> biomeTimeMultiplier;
		public final ForgeConfigSpec.ConfigValue<Integer> timeShadeModifier;
		
		public final ForgeConfigSpec.ConfigValue<Double> altitudeModifier;
		
		public final ForgeConfigSpec.ConfigValue<Double> sprintModifier;
		
		public final ForgeConfigSpec.ConfigValue<Integer> minTickRate;
		public final ForgeConfigSpec.ConfigValue<Integer> maxTickRate;
		public final ForgeConfigSpec.ConfigValue<Integer> routinePacketSync;
		
		public final ForgeConfigSpec.ConfigValue<Integer> tempInfluenceHorizontalDist;
		public final ForgeConfigSpec.ConfigValue<Integer> tempInfluenceVerticalDist;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> seasonTemperatureEffects;
		
		// Thirst
		public final ForgeConfigSpec.ConfigValue<Boolean> temperatureAffectsThirst;
		public final ForgeConfigSpec.ConfigValue<Integer> maxCanteenDrinks;
		public final ForgeConfigSpec.ConfigValue<Integer> maxNetheriteCanteenDrinks;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> canDrinkFromRain;
		
		public final ForgeConfigSpec.ConfigValue<Double> thirstExhaustionLimit;
		public final ForgeConfigSpec.ConfigValue<Double> thirstEffectStrength;
		public final ForgeConfigSpec.ConfigValue<Double> thirstAttackExhaustion;
		public final ForgeConfigSpec.ConfigValue<Double> thirstBlockBreakExhaustion;
		public final ForgeConfigSpec.ConfigValue<Double> thirstSprintJumpExhaustion;
		public final ForgeConfigSpec.ConfigValue<Double> thirstJumpExhaustion;
		public final ForgeConfigSpec.ConfigValue<Double> thirstBaseMovementExhaustion;
		public final ForgeConfigSpec.ConfigValue<Double> thirstSwimExhaustion;
		public final ForgeConfigSpec.ConfigValue<Double> thirstFastSwimExhaustion;
		public final ForgeConfigSpec.ConfigValue<Double> thirstSprintExhaustion;
		public final ForgeConfigSpec.ConfigValue<Double> thirstWalkingExhaustion;
		
		Common(ForgeConfigSpec.Builder builder)
		{
			builder.comment(new String [] {
					" Options related to enabling/disabling specific features",
					" See the jsons folder to customize the temperature of specific blocks, liquids, armors, etc."
			}).push("core");
			thirstEnabled = builder
					.comment(" Whether or not the thirst system is enabled.")
					.define("Thirst Enabled", true);
			temperatureEnabled = builder
					.comment(" Whether or not the temperature system is enabled.")
					.define("Temperature Enabled", true);
			staminaEnabled = builder
					.comment(" Whether or not the stamina system is enabled.")
					.define("Stamina Enabled", true);
			
			builder.push("advanced");
			routinePacketSync = builder
					.comment(new String[] {" How often player temperature and thirst are regularly synced between the client and server, in ticks."," Lower values will increase accuracy at the cost of performance"})
					.define("Routine Packet Sync", 30);
			builder.pop();
			builder.pop();
			
			builder.comment(" Options related to the temperature system").push("temperature");
			dangerousTemperature = builder
					.comment(" If enabled, players will directly take damage from the effects of temperature.")
					.define("Dangerous Temperature Effects", true);
			temperatureSecondaryEffects = builder
					.comment(" If enabled, players will also recieve other effects from their current temperature state.")
					.define("Secondary Temperature Effects", true);
			
			builder.push("items");
			builder.push("coils").comment("Currently non-functional. Use the JSON configs to modify the temperature modifiers.");
			maxCoilInfluenceDistance = builder
					.comment(" Maximum distance where powered coils will have an effect on a player's temperature.")
					.define("Max Coil Influence Distance", 32.0d);
			coilFullPowerDistance = builder
					.comment(" Maximum distance where powered coils will have their maximum effect applied to a player.")
					.define("Coil Full Power Distance", 16.0d);
			coilInfluence = builder
					.comment(" Influence that a coil has on a player's temperature at the nearest distance.")
					.define("Coil Temperature Influence", 10);
			builder.pop();
			builder.push("armors");
			snowArmorSetBonusEnabled = builder
					.comment(" Whether or not a full set of Snow Armor will protect the player from Frostbite while on the surface of the overworld.")
					.define("Snow Armor Set Bonus Enabled", true);
			desertArmorSetBonusEnabled = builder
					.comment(" Whether or not a full set of Desert Armor will protect the player from Heat Stroke while on the surface of the overworld.")
					.define("Desert Armor Set Bonus Enabled", true);
			
			builder.pop();
			builder.pop();
			
			sprintModifier = builder
					.comment(" How much of an effect sprinting has on a player's temperature.")
					.define("Player Sprint Modifier", 1.5d);
			
			builder.push("environment");
			altitudeModifier = builder
					.comment(" How much the effects of the player's altitude on temperature are multiplied.")
					.define("Altitude Modifier", 3.0d);
			builder.push("biomes");
			biomeTemperatureMultiplier = builder
					.comment(" How much a biome's temperature effects are multiplied.")
					.defineInRange("Biome Temperature Multiplier", 10.0d, 0.0d, Double.POSITIVE_INFINITY);
			biomeEffectsEnabled = builder
					.comment(" Whether or not biomes will have an effect on a player's temperature.")
					.define("Biomes affect Temperature", true);
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
					.comment(" Maximum horizontal distance where heat sources will have an effect on temperature.")
					.defineInRange("Temperature Influence Horizontal Distance", 3, 1, 10);
			tempInfluenceVerticalDist = builder
					.comment(" Maximum vertical distance where heat sources will have an effect on temperature.")
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
			seasonTemperatureEffects = builder
					.comment(new String[] {" If Serene Seasons or Better Weather is installed,", " then seasons will have an effect on the player's temperature."})
					.define("Seasons affect Temperature", true);
			builder.pop();
			builder.pop();

			builder.comment(" Options related to the thirst system").push("thirst");
			temperatureAffectsThirst = builder
					.comment(new String [] {" Whether or not temperature affects thirst.", " Only relevant if temperature is also enabled."})
					.define("Temperature Affects Thirst", true);
			
			maxCanteenDrinks = builder
					.comment(new String[] {" Maximum amount of drinks in the canteen.", " Default is 3."})
					.define("Maximum Canteen Drinks", 3);
			
			maxNetheriteCanteenDrinks = builder
					.comment(new String[] {" Maximum amount of drinks in the netherite canteen.", " Default is 3."} )
					.define("Maximum Netherite Canteen Drinks", 3);
			
			builder.push("water-sources");
			canDrinkFromRain = builder
					.comment("Whether or not the player can drink while it is raining.")
					.define("Player can drink from rain", true);
			builder.pop();
			
			builder.push("exhaustion");
			thirstExhaustionLimit = builder
					.comment(" How exhausted the player has to be before they lose thirst.")
					.defineInRange("Thirst Exhaustion Limit", 4.0d, 1.0d, 8.0d);
			thirstEffectStrength = builder
					.comment(" Strength of the thirst effect")
					.defineInRange("Thirst Effect Strength", 0.025d, 0.0d, 1.0d);
			thirstAttackExhaustion = builder
					.comment(" How exhausting attacking is")
					.defineInRange("Thirst Attacking Exhaustion", 0.3d, 0.0d, 1.0d);
			thirstBlockBreakExhaustion = builder
					.comment(" How exhausting breaking blocks is.")
					.defineInRange("Thirst Block Breaking Exhaustion", 0.025d, 0.0d, 1.0d);
			thirstSprintJumpExhaustion = builder
					.comment(" How exhausting jumping while sprinting is.")
					.defineInRange("Thirst Sprint Jumping Exhaustion", 0.8d, 0.0d, 1.0d);
			thirstJumpExhaustion = builder
					.comment(" How exhausting jumping is.")
					.defineInRange("Thirst Jumping Exhaustion", 0.2d, 0.0d, 1.0d);
			thirstBaseMovementExhaustion = builder
					.comment(" How exhausting any kind of movement is.")
					.defineInRange("Thirst Movement Exhaustion", 0.01d, 0.0d, 1.0d);
			thirstSwimExhaustion = builder
					.comment(" How exhausting swimming is.")
					.defineInRange("Thirst Swimming Exhaustion", 0.015d, 0.0d, 1.0d);
			thirstFastSwimExhaustion = builder
					.comment(" How exhausting fast swimming is.")
					.defineInRange("Thirst Fast Swimming Exhaustion", 0.035d, 0.0d, 1.0d);
			thirstSprintExhaustion = builder
					.comment(" How exhausting sprinting is.")
					.defineInRange("Thirst Sprint Exhaustion", 0.1d, 0.0d, 1.0d);
			thirstWalkingExhaustion = builder
					.comment(" How exhausting walking is.")
					.defineInRange("Thirst Walking Exhaustion", 0.01d, 0.0d, 1.0d);
			builder.pop();
			
			builder.pop();
		}
	}
	
	public static class Client
	{
		public final ForgeConfigSpec.ConfigValue<String> temperatureDisplayMode;
		public final ForgeConfigSpec.ConfigValue<Integer> temperatureDisplayOffsetX;
		public final ForgeConfigSpec.ConfigValue<Integer> temperatureDisplayOffsetY;
		
		public final ForgeConfigSpec.ConfigValue<String> staminaDisplayMode;
		public final ForgeConfigSpec.ConfigValue<Integer> staminaDisplayOffsetX;
		public final ForgeConfigSpec.ConfigValue<Integer> staminaDisplayOffsetY;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> climbingKeyIsToggle;
		
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
		
		public static double altitudeModifier;
		
		public static double maxCoilInfluenceDistance;
		public static double coilFullPowerDistance;
		public static int coilInfluence;

		public static boolean snowArmorSetBonusEnabled;
		public static boolean desertArmorSetBonusEnabled;
		
		public static int minTickRate;
		public static int maxTickRate;
		public static int routinePacketSync;
		
		public static boolean seasonTemperatureEffects;
		
		public static boolean temperatureAffectsThirst;
		public static int maxCanteenDrinks;
		public static int maxNetheriteCanteenDrinks;
		
		public static double timeMultiplier;
		public static double biomeTimeMultiplier;
		public static int timeShadeModifier;
		
		public static int tempInfluenceHorizontalDist;
		public static int tempInfluenceVerticalDist;
		
		public static double sprintModifier;
		
		public static boolean canDrinkFromRain;
		
		public static double thirstExhaustionLimit;
		public static double thirstEffectStrength;
		public static double thirstAttackExhaustion;
		public static double thirstBlockBreakExhaustion;
		public static double thirstSprintJumpExhaustion;
		public static double thirstJumpExhaustion;
		public static double thirstBaseMovementExhaustion;
		public static double thirstSwimExhaustion;
		public static double thirstFastSwimExhaustion;
		public static double thirstSprintExhaustion;
		public static double thirstWalkingExhaustion;
		
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
				
				altitudeModifier = COMMON.altitudeModifier.get();
				
				maxCoilInfluenceDistance = COMMON.maxCoilInfluenceDistance.get();
				coilFullPowerDistance = COMMON.coilFullPowerDistance.get();
				coilInfluence = COMMON.coilInfluence.get();

				snowArmorSetBonusEnabled = COMMON.snowArmorSetBonusEnabled.get();
				desertArmorSetBonusEnabled = COMMON.desertArmorSetBonusEnabled.get();

				
				biomeEffectsEnabled = COMMON.biomeEffectsEnabled.get();
				biomeTemperatureMultiplier = COMMON.biomeTemperatureMultiplier.get();
				timeMultiplier = COMMON.timeMultiplier.get();
				biomeTimeMultiplier = COMMON.biomeTimeMultiplier.get();
				timeShadeModifier = COMMON.timeShadeModifier.get();
				
				sprintModifier = COMMON.sprintModifier.get();
				
				tempInfluenceHorizontalDist = COMMON.tempInfluenceHorizontalDist.get();
				tempInfluenceVerticalDist = COMMON.tempInfluenceVerticalDist.get();
				minTickRate = COMMON.minTickRate.get();
				maxTickRate = COMMON.maxTickRate.get();
				routinePacketSync = COMMON.routinePacketSync.get();
				
				seasonTemperatureEffects = COMMON.seasonTemperatureEffects.get();
				
				temperatureAffectsThirst = COMMON.temperatureAffectsThirst.get();
				maxCanteenDrinks = COMMON.maxCanteenDrinks.get();
				maxNetheriteCanteenDrinks = COMMON.maxNetheriteCanteenDrinks.get();
				
				canDrinkFromRain = COMMON.canDrinkFromRain.get();
				
				thirstExhaustionLimit = COMMON.thirstExhaustionLimit.get();
				thirstEffectStrength = COMMON.thirstEffectStrength.get();
				thirstAttackExhaustion = COMMON.thirstAttackExhaustion.get();
				thirstBlockBreakExhaustion = COMMON.thirstBlockBreakExhaustion.get();
				thirstSprintJumpExhaustion = COMMON.thirstSprintJumpExhaustion.get();
				thirstJumpExhaustion = COMMON.thirstJumpExhaustion.get();
				thirstBaseMovementExhaustion = COMMON.thirstBaseMovementExhaustion.get();
				thirstSwimExhaustion = COMMON.thirstSwimExhaustion.get();
				thirstFastSwimExhaustion = COMMON.thirstFastSwimExhaustion.get();
				thirstSprintExhaustion = COMMON.thirstSprintExhaustion.get();
				thirstWalkingExhaustion = COMMON.thirstWalkingExhaustion.get();
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

				staminaDisplayMode = StaminaDisplayEnum.getDisplayFromString(CLIENT.staminaDisplayMode.get());
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
