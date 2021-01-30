package icey.survivaloverhaul.common.compat.sereneseasons;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.config.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sereneseasons.api.season.ISeasonState;
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
			return Config.Baked.earlySpringModifier;
		case MID_SPRING:
			return Config.Baked.midSpringModifier;
		case LATE_SPRING:
			return Config.Baked.lateSpringModifier;
		case EARLY_SUMMER:
			return Config.Baked.earlySummerModifier;
		case MID_SUMMER:
			return Config.Baked.midSummerModifier;
		case LATE_SUMMER:
			return Config.Baked.lateSummerModifier;
		case EARLY_AUTUMN:
			return Config.Baked.earlyAutumnModifier;
		case MID_AUTUMN:
			return Config.Baked.midAutumnModifier;
		case LATE_AUTUMN:
			return Config.Baked.lateAutumnModifier;
		case EARLY_WINTER:
			return Config.Baked.earlyWinterModifier;
		case MID_WINTER:
			return Config.Baked.midWinterModifier;
		case LATE_WINTER:
			return Config.Baked.lateWinterModifier;
		default:
			return 0.0f;
		}
	}
}
