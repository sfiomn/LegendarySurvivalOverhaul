package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class BiomeModifier extends ModifierBase
{
	public BiomeModifier()
	{
		super();
	}
	
	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		Vector3i[] posOffsets = 
			{
					new Vector3i(0, 0, 0),
					new Vector3i(10, 0, 0),
					new Vector3i(-10, 0, 0),
					new Vector3i(0, 0, 10),
					new Vector3i(0, 0, -10),
					new Vector3i(7, 0, 7),
					new Vector3i(7, 0, -7),
					new Vector3i(-7, 0, 7),
					new Vector3i(-7, 0, -7)
			};
		
		float biomeAverage = 0f;
		
		long worldTime = world.getLevelData().getDayTime() % 24000;
		double drynessTimeMultiplier = 1;
		if (worldTime > 12000 && !world.dimensionType().hasCeiling() && Config.Baked.biomeDrynessMultiplier > 0)
			drynessTimeMultiplier = 1 + Math.sin(worldTime * Math.PI / 12000) * Config.Baked.biomeDrynessMultiplier;

		for (Vector3i offset : posOffsets)
		{
			Biome biome = world.getBiome(pos.offset(offset));
			float humidity = getHumidityForBiome(world, biome);
			float biomeTemperature = getNormalizedTempForBiome(world, biome);
			
			if (drynessTimeMultiplier < 1 && humidity < 0.2f && biomeTemperature > 0.80f) {
				// Deserts are cold at night since heat isn't kept by moisture in the air
				biomeAverage += (float) (drynessTimeMultiplier * biomeTemperature);

			} else {
				biomeAverage += biomeTemperature;
			}
		}
		
		biomeAverage /= (float)(posOffsets.length);

		// LegendarySurvivalOverhaul.LOGGER.debug("Biome temp influence : " + normalizeToPositiveNegative(biomeAverage) * ((float) Config.Baked.biomeTemperatureMultiplier));
		// float tempInfl = applyUndergroundEffect(normalizeToPositiveNegative(biomeAverage) * ((float) Config.Baked.biomeTemperatureMultiplier), world, pos);
		// LegendarySurvivalOverhaul.LOGGER.debug("Biome temp influence after underground : " + tempInfl);
		return applyUndergroundEffect(normalizeToPositiveNegative(biomeAverage) * ((float) Config.Baked.biomeTemperatureMultiplier), world, pos);
	}
}
