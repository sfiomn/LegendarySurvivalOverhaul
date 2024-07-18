package sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.SeasonsConfig;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Modifier;
import java.util.Map;

public class SereneSeasonsModifier extends ModifierBase
{
	public static Map<String, SSBiomeIdentity> biomeIdentities = Maps.newHashMap();
	public static String jsonFileName = "biome_info.json";
	
	// This is prone to breaking if Serene Seasons updates how they handle their configuration
	// But unfortunately it's the only method I can think of since whether a biome is tropical or not
	// isn't handled by the api
	public static void prepareBiomeIdentities()
	{
		try
		{
			File jsonFile = new File(LegendarySurvivalOverhaul.ssConfigPath.toFile(), jsonFileName);
			
			if (jsonFile.exists())
			{
				Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create();
				biomeIdentities = gson.fromJson(new FileReader(jsonFile), new TypeToken<Map<String, SSBiomeIdentity>>(){}.getType());
			}
		}
		catch (Exception e)
		{
			LegendarySurvivalOverhaul.LOGGER.error("Unknown error while reading Serene Seasons config", e);
		}
		
		LegendarySurvivalOverhaul.LOGGER.debug("Got " + biomeIdentities.size() + " entries from Serene Seasons configs");
	}
	
	public SereneSeasonsModifier()
	{
		super();
	}

	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		if (!LegendarySurvivalOverhaul.sereneSeasonsLoaded)
			return 0.0f;
		
		if (!Config.Baked.seasonTemperatureEffects)
			return 0.0f;
		
		try
		{
			// In theory, this should only ever run if Serene Seasons is installed
			// However, just to be safe, we put this inside of a try/catch to make
			// sure something weird hasn't happened with the API
			return getUncaughtWorldInfluence(world, pos);
		}
		catch (Exception e)
		{
			// If an error somehow occurs, disable compatibility 
			LegendarySurvivalOverhaul.LOGGER.error("An error has occurred with Serene Seasons compatibility, disabling modifier", e);
			LegendarySurvivalOverhaul.sereneSeasonsLoaded = false;
			
			return 0.0f;
		}
	}
	
	public float getUncaughtWorldInfluence(World world, BlockPos pos)
	{
		ISeasonState seasonState = SeasonHelper.getSeasonState(world);
		
		if (seasonState == null || !SeasonsConfig.isDimensionWhitelisted(world.dimension()))
			return 0.0f;

		Vector3i[] posOffsets;
		if (Config.Baked.tropicalSeasonsEnabled)
			posOffsets = new Vector3i[]{
					new Vector3i(0, 0, 0),
                    new Vector3i(10, 0, 0),
                    new Vector3i(-10, 0, 0),
                    new Vector3i(0, 0, 10),
                    new Vector3i(0, 0, -10)
		};
		else
			posOffsets = new Vector3i[]{new Vector3i(0, 0, 0)};
		
		float value = 0.0f;
		int validSpot = posOffsets.length;

		for (Vector3i offset : posOffsets)
		{
			Biome biome = world.getBiome(pos.offset(offset));
			int seasonType = SereneSeasonsUtil.getSeasonType(biome);

			if (seasonType == 2) {
				validSpot -= 1;
				continue;
			}
			
			boolean useTropicalMods = seasonType == 1 && Config.Baked.tropicalSeasonsEnabled;

			if (!useTropicalMods) {
				int timeInSubSeason = seasonState.getSeasonCycleTicks() % seasonState.getSubSeasonDuration();
				switch(seasonState.getSubSeason()) {
					case EARLY_SPRING:
						value += getSeasonModifier(Config.Baked.lateWinterModifier, Config.Baked.earlySpringModifier, Config.Baked.midSpringModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
						break;
					case MID_SPRING:
						value += getSeasonModifier(Config.Baked.earlySpringModifier, Config.Baked.midSpringModifier, Config.Baked.lateSpringModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
						break;
					case LATE_SPRING:
						value += getSeasonModifier(Config.Baked.midSpringModifier, Config.Baked.lateSpringModifier, Config.Baked.earlySummerModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
						break;
					case EARLY_SUMMER:
						value += getSeasonModifier(Config.Baked.lateSpringModifier, Config.Baked.earlySummerModifier, Config.Baked.midSummerModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
						break;
					case MID_SUMMER:
						value += getSeasonModifier(Config.Baked.earlySummerModifier, Config.Baked.midSummerModifier, Config.Baked.lateSummerModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
						break;
					case LATE_SUMMER:
						value += getSeasonModifier(Config.Baked.midSummerModifier, Config.Baked.lateSummerModifier, Config.Baked.earlyAutumnModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
						break;
					case EARLY_AUTUMN:
						value += getSeasonModifier(Config.Baked.lateSummerModifier, Config.Baked.earlyAutumnModifier, Config.Baked.midAutumnModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
						break;
					case MID_AUTUMN:
						value += getSeasonModifier(Config.Baked.earlyAutumnModifier, Config.Baked.midAutumnModifier, Config.Baked.lateAutumnModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
						break;
					case LATE_AUTUMN:
						value += getSeasonModifier(Config.Baked.midAutumnModifier, Config.Baked.lateAutumnModifier, Config.Baked.earlyWinterModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
						break;
					case EARLY_WINTER:
						value += getSeasonModifier(Config.Baked.lateAutumnModifier, Config.Baked.earlyWinterModifier, Config.Baked.midWinterModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
						break;
					case MID_WINTER:
						value += getSeasonModifier(Config.Baked.earlyWinterModifier, Config.Baked.midWinterModifier, Config.Baked.lateWinterModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
						break;
					case LATE_WINTER:
						value += getSeasonModifier(Config.Baked.midWinterModifier, Config.Baked.lateWinterModifier, Config.Baked.earlySpringModifier, timeInSubSeason, seasonState.getSubSeasonDuration());
						break;
				}
			} else {
				int timeInSubSeason = (seasonState.getSeasonCycleTicks() + seasonState.getSubSeasonDuration()) % (seasonState.getSubSeasonDuration() * 2);
				switch (seasonState.getTropicalSeason()) {
					case EARLY_DRY:
						value += getSeasonModifier(Config.Baked.lateWetSeasonModifier, Config.Baked.earlyDrySeasonModifier, Config.Baked.midDrySeasonModifier, timeInSubSeason, seasonState.getSubSeasonDuration() * 2);
						break;
					case MID_DRY:
						value += getSeasonModifier(Config.Baked.earlyDrySeasonModifier, Config.Baked.midDrySeasonModifier, Config.Baked.lateDrySeasonModifier, timeInSubSeason, seasonState.getSubSeasonDuration() * 2);
						break;
					case LATE_DRY:
						value += getSeasonModifier(Config.Baked.midDrySeasonModifier, Config.Baked.lateDrySeasonModifier, Config.Baked.earlyWetSeasonModifier, timeInSubSeason, seasonState.getSubSeasonDuration() * 2);
						break;
					case EARLY_WET:
						value += getSeasonModifier(Config.Baked.lateDrySeasonModifier, Config.Baked.earlyWetSeasonModifier, Config.Baked.midWetSeasonModifier, timeInSubSeason, seasonState.getSubSeasonDuration() * 2);
						break;
					case MID_WET:
						value += getSeasonModifier(Config.Baked.earlyWetSeasonModifier, Config.Baked.midWetSeasonModifier, Config.Baked.lateWetSeasonModifier, timeInSubSeason, seasonState.getSubSeasonDuration() * 2);
						break;
					case LATE_WET:
						value += getSeasonModifier(Config.Baked.midWetSeasonModifier, Config.Baked.lateWetSeasonModifier, Config.Baked.earlyDrySeasonModifier, timeInSubSeason, seasonState.getSubSeasonDuration() * 2);
						break;
				}
			}
        }

		value = validSpot == 0 ? 0 : value / validSpot;

		// LegendarySurvivalOverhaul.LOGGER.debug("Serene temp influence : " + value);
		// float tempInfl = applyUndergroundEffect(value, world, pos);
		// LegendarySurvivalOverhaul.LOGGER.debug("Serene temp influence after underground : " + tempInfl);
		return applyUndergroundEffect(value, world, pos);
	}

	private float getSeasonModifier(float previousSeasonModifier, float currentSeasonModifier, float nextSeasonModifier, int time, int subSeasonDuration) {
		return time < subSeasonDuration / 2 ?
				calculateSinusoidalBetweenSeasons(previousSeasonModifier, currentSeasonModifier, time + (subSeasonDuration / 2), subSeasonDuration):
				calculateSinusoidalBetweenSeasons(currentSeasonModifier, nextSeasonModifier, time - (subSeasonDuration / 2), subSeasonDuration);
	}

	private float calculateSinusoidalBetweenSeasons(float previousSeasonModifier, float nextSeasonModifier, int time, int subSeasonDuration) {
		float tempDiff = nextSeasonModifier - previousSeasonModifier;
		float seasonModifier = (float) (Math.sin(((time * Math.PI) / subSeasonDuration) - (Math.PI / 2)) + 1) * (tempDiff / 2) + previousSeasonModifier;
		return MathUtil.round(seasonModifier, 2);
	}
}
