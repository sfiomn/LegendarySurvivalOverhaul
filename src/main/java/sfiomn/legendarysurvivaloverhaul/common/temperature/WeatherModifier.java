package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.TemperatureModifierRegistry;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

public class WeatherModifier extends ModifierBase
{
	public WeatherModifier()
	{
		super();
	}
	
	// TODO: Try and get this also working with serene seasons
	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		// Apply shade effect in hot biomes if the player is either "hidden from sky" or time is raining
		// Shade effect depends on Time, no shade effect at sunrise and sunset, max effect at noon
		if (!world.isRaining() && world.canSeeSky(pos.above())) {
			return 0.0f;
		}

		Biome biome = world.getBiome(pos);
		float weatherTemperature = 0.0f;
		long time = world.getLevelData().getDayTime();

		if(Config.Baked.shadeTimeMultiplier != 0 && time <= 12000)
		{
			if ((TemperatureModifierRegistry.BIOME.get().getWorldInfluence(world, pos) +
					TemperatureModifierRegistry.SERENE_SEASONS.get().getWorldInfluence(world, pos)) > 15) {
				float shadeTemperature = -1.0f * (float) Config.Baked.shadeTimeMultiplier * (float) Math.sin((time * Math.PI) / 12000.0f);
				weatherTemperature += applyUndergroundEffect(shadeTemperature, world, pos);
				// LegendarySurvivalOverhaul.LOGGER.debug("Shade temp influence : " + weatherTemperature + "biome temp : " + TemperatureModifierRegistry.BIOME.get().getWorldInfluence(world, pos) +
				// 		", season temp : " + TemperatureModifierRegistry.SERENE_SEASONS.get().getWorldInfluence(world, pos));
			}
		}

		if(WorldUtil.isRainingOrSnowingAt(world, pos.above())) {
			// LegendarySurvivalOverhaul.LOGGER.debug("Weather Biome temp : " + world.getBiome(pos).getTemperature(pos));

			if (biome.getTemperature(pos) < 0.15f) {
				// LegendarySurvivalOverhaul.LOGGER.debug("Snow temp influence : " + (weatherTemperature + (float) Config.Baked.snowTemperatureModifier));
				weatherTemperature += (float) Config.Baked.snowTemperatureModifier;
			} else if (biome.getTemperature(pos) >= 0.15f) {
				//LegendarySurvivalOverhaul.LOGGER.debug("Rain temp influence : " + (weatherTemperature + (float) Config.Baked.rainTemperatureModifier));
				weatherTemperature += (float) Config.Baked.rainTemperatureModifier;
			}
		}
		return weatherTemperature;
	}
}
