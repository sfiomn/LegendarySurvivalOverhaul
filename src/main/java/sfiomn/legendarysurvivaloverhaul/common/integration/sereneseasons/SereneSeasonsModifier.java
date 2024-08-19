package sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.ServerConfig;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.integration.terrafirmacraft.TerraFirmaCraftUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;


public class SereneSeasonsModifier extends ModifierBase
{
	public SereneSeasonsModifier()
	{
		super();
	}

	@Override
	public float getWorldInfluence(Level level, BlockPos pos)
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
			return getUncaughtWorldInfluence(level, pos);
		}
		catch (Exception e)
		{
			// If an error somehow occurs, disable compatibility 
			LegendarySurvivalOverhaul.LOGGER.error("An error has occurred with Serene Seasons compatibility, disabling modifier", e);
			LegendarySurvivalOverhaul.sereneSeasonsLoaded = false;
			
			return 0.0f;
		}
	}
	
	public float getUncaughtWorldInfluence(Level level, BlockPos pos)
	{
		ISeasonState seasonState = SeasonHelper.getSeasonState(level);
		
		if (seasonState == null || !ServerConfig.isDimensionWhitelisted(level.dimension()))
			return 0.0f;

		Vec3i[] posOffsets;
		if (Config.Baked.tropicalSeasonsEnabled)
			posOffsets = new Vec3i[]{
					new Vec3i(0, 0, 0),
                    new Vec3i(10, 0, 0),
                    new Vec3i(-10, 0, 0),
                    new Vec3i(0, 0, 10),
                    new Vec3i(0, 0, -10)
			};
		else
			posOffsets = new Vec3i[]{new Vec3i(0, 0, 0)};
		
		float value = 0.0f;
		int validSpot = posOffsets.length;

		for (Vec3i offset : posOffsets)
		{
			SereneSeasonsUtil.SeasonType seasonType = SereneSeasonsUtil.getSeasonType(level.getBiome(pos.offset(offset)));

			if (seasonType == SereneSeasonsUtil.SeasonType.NO_SEASON) {
				validSpot -= 1;
				continue;
			}

			if (seasonType != SereneSeasonsUtil.SeasonType.TROPICAL_SEASON) {
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
		return value;
	}

	private float getSeasonModifier(double previousSeasonModifier, double currentSeasonModifier, double nextSeasonModifier, int time, int subSeasonDuration) {
		return time < subSeasonDuration / 2 ?
				calculateSinusoidalBetweenSeasons(previousSeasonModifier, currentSeasonModifier, time + (subSeasonDuration / 2), subSeasonDuration):
				calculateSinusoidalBetweenSeasons(currentSeasonModifier, nextSeasonModifier, time - (subSeasonDuration / 2), subSeasonDuration);
	}

	private float calculateSinusoidalBetweenSeasons(double previousSeasonModifier, double nextSeasonModifier, int time, int subSeasonDuration) {
		double tempDiff = nextSeasonModifier - previousSeasonModifier;
		double seasonModifier = (Math.sin(((time * Math.PI) / subSeasonDuration) - (Math.PI / 2)) + 1) * (tempDiff / 2) + previousSeasonModifier;
		return MathUtil.round((float) seasonModifier, 2);
	}
}
