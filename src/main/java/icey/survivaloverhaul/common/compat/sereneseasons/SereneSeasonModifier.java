package icey.survivaloverhaul.common.compat.sereneseasons;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.config.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

public class SereneSeasonModifier extends ModifierBase
{
	public SereneSeasonModifier()
	{
		super();
		this.setRegistryName(Main.MOD_ID,"compat/serene_seasons");
	}
	

	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		if (!Main.sereneSeasonsLoaded)
				return 0.0f;
		
		try
		{
			return applyUndergroundEffect(getUncaughtWorldInfluence(world, pos), world, pos);
		}
		catch (Exception e)
		{
			Main.LOGGER.error("An error has occured with Serene Seasons compatability, disabling modifier", e);
			Main.sereneSeasonsLoaded = false;
			
			return 0.0f;
		}
	}
	
	public float getUncaughtWorldInfluence(World world, BlockPos pos)
	{
		ISeasonState seasonState = SeasonHelper.getSeasonState(world);
		
		switch(seasonState.getSubSeason())
		{
		case EARLY_SPRING:
			return Config.BakedConfigValues.earlySpringModifier;
		case MID_SPRING:
			return Config.BakedConfigValues.midSpringModifier;
		case LATE_SPRING:
			return Config.BakedConfigValues.lateSpringModifier;
		case EARLY_SUMMER:
			return Config.BakedConfigValues.earlySummerModifier;
		case MID_SUMMER:
			return Config.BakedConfigValues.midSummerModifier;
		case LATE_SUMMER:
			return Config.BakedConfigValues.lateSummerModifier;
		case EARLY_AUTUMN:
			return Config.BakedConfigValues.earlyAutumnModifier;
		case MID_AUTUMN:
			return Config.BakedConfigValues.midAutumnModifier;
		case LATE_AUTUMN:
			return Config.BakedConfigValues.lateAutumnModifier;
		case EARLY_WINTER:
			return Config.BakedConfigValues.earlyWinterModifier;
		case MID_WINTER:
			return Config.BakedConfigValues.midWinterModifier;
		case LATE_WINTER:
			return Config.BakedConfigValues.lateWinterModifier;
		default:
			return 0.0f;
		}
	}
}
