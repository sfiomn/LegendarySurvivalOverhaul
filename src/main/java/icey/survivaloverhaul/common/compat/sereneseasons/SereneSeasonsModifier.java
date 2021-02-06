package icey.survivaloverhaul.common.compat.sereneseasons;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.config.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;

public class SereneSeasonsModifier extends ModifierBase
{
	public SereneSeasonsModifier()
	{
		super();
	}
	

	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		if (!Main.sereneSeasonsLoaded)
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
			Main.LOGGER.error("An error has occured with Serene Seasons compatability, disabling modifier", e);
			Main.sereneSeasonsLoaded = false;
			
			return 0.0f;
		}
	}
	
	public float getUncaughtWorldInfluence(World world, BlockPos pos)
	{
		ISeasonState seasonState = SeasonHelper.getSeasonState(world);
		
		float value = 0.0f;
		switch(seasonState.getSubSeason())
		{
		case EARLY_SPRING:
			value = Config.Baked.earlySpringModifier;
		case MID_SPRING:
			value = Config.Baked.midSpringModifier;
		case LATE_SPRING:
			value = Config.Baked.lateSpringModifier;
		case EARLY_SUMMER:
			value = Config.Baked.earlySummerModifier;
		case MID_SUMMER:
			value = Config.Baked.midSummerModifier;
		case LATE_SUMMER:
			value = Config.Baked.lateSummerModifier;
		case EARLY_AUTUMN:
			value = Config.Baked.earlyAutumnModifier;
		case MID_AUTUMN:
			value = Config.Baked.midAutumnModifier;
		case LATE_AUTUMN:
			value = Config.Baked.lateAutumnModifier;
		case EARLY_WINTER:
			value = Config.Baked.earlyWinterModifier;
		case MID_WINTER:
			value =  Config.Baked.midWinterModifier;
		case LATE_WINTER:
			value = Config.Baked.lateWinterModifier;
		default:
			value = 0.0f;
		}
		
		return applyUndergroundEffect(value, world, pos);
	}
}
