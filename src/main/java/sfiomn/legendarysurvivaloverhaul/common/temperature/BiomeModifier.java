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
		
		for (Vector3i offset : posOffsets)
		{
			Biome biome = world.getBiome(pos.offset(offset));
			float humidity = getHumidityForBiome(world, biome);
			float addedTemperature = getNormalizedTempForBiome(world, biome);
			
			if (humidity < 0.2f && worldTime > 12000 && addedTemperature > 0.80f && !world.dimensionType().hasCeiling() && Config.Baked.biomeDrynessEffectEnabled)
			{
				// Deserts are cold at night since heat isn't kept by moisture in the air
				biomeAverage += (addedTemperature / 5f);
			}
			else
			{
				biomeAverage += addedTemperature;
			}
		}
		
		biomeAverage /= (float)(posOffsets.length);

		// LegendarySurvivalOverhaul.LOGGER.debug("Biome temp influence : " + normalizeToPositiveNegative(biomeAverage) * ((float) Config.Baked.biomeTemperatureMultiplier));
		// float tempInfl = applyUndergroundEffect(normalizeToPositiveNegative(biomeAverage) * ((float) Config.Baked.biomeTemperatureMultiplier), world, pos);
		// LegendarySurvivalOverhaul.LOGGER.debug("Biome temp influence after underground : " + tempInfl);
		return applyUndergroundEffect(normalizeToPositiveNegative(biomeAverage) * ((float) Config.Baked.biomeTemperatureMultiplier), world, pos);
	}
}
