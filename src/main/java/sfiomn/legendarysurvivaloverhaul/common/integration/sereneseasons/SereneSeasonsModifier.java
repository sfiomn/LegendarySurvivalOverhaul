package sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.util.ResourceLocation;
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

		Vector3i[] posOffsets = 
			{
					new Vector3i(0, 0, 0),
					new Vector3i(10, 0, 0),
					new Vector3i(-10, 0, 0),
					new Vector3i(0, 0, 10),
					new Vector3i(0, 0, -10)
			};
		
		float value = 0.0f;

		for (Vector3i offset : posOffsets)
		{
			Biome biome = world.getBiome(pos.offset(offset));
			int seasonType = SereneSeasonsUtil.getSeasonType(biome);

			if (seasonType == 2)
				continue;
			
			boolean useTropicalMods = seasonType == 1 && Config.Baked.tropicalSeasonsEnabled;

			if (!useTropicalMods) {
				switch(seasonState.getSubSeason())
				{
				case EARLY_SPRING:
					value += Config.Baked.earlySpringModifier;
					break;
				case MID_SPRING:
					value += Config.Baked.midSpringModifier;
					break;
				case LATE_SPRING:
					value += Config.Baked.lateSpringModifier;
					break;
				case EARLY_SUMMER:
					value += Config.Baked.earlySummerModifier;
					break;
				case MID_SUMMER:
					value += Config.Baked.midSummerModifier;
					break;
				case LATE_SUMMER:
					value += Config.Baked.lateSummerModifier;
					break;
				case EARLY_AUTUMN:
					value += Config.Baked.earlyAutumnModifier;
					break;
				case MID_AUTUMN:
					value += Config.Baked.midAutumnModifier;
					break;
				case LATE_AUTUMN:
					value += Config.Baked.lateAutumnModifier;
					break;
				case EARLY_WINTER:
					value += Config.Baked.earlyWinterModifier;
					break;
				case MID_WINTER:
					value +=  Config.Baked.midWinterModifier;
					break;
				case LATE_WINTER:
					value += Config.Baked.lateWinterModifier;
					break;
				}
			} else {
				switch (seasonState.getTropicalSeason())
				{
				case EARLY_DRY:
					value += Config.Baked.earlyDrySeasonModifier;
					break;
				case MID_DRY:
					value += Config.Baked.midDrySeasonModifier;
					break;
				case LATE_DRY:
					value += Config.Baked.lateDrySeasonModifier;
					break;
				case EARLY_WET:
					value += Config.Baked.earlyWetSeasonModifier;
					break;
				case MID_WET:
					value += Config.Baked.midWetSeasonModifier;
					break;
				case LATE_WET:
					value += Config.Baked.lateWetSeasonModifier;
					break;
				}
			}
        }
		
		value /= posOffsets.length;

		// LegendarySurvivalOverhaul.LOGGER.debug("Serene temp influence : " + value);
		// float tempInfl = applyUndergroundEffect(value, world, pos);
		// LegendarySurvivalOverhaul.LOGGER.debug("Serene temp influence after underground : " + tempInfl);
		return applyUndergroundEffect(value, world, pos);
	}
}
