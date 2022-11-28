package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;

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
			float humidity = biome.getDownfall();
			float addedTemperature = getTempForBiome(biome);
			
			String biomeName = biome.getRegistryName().toString();
			
			if (JsonConfig.biomeOverrides.containsKey(biomeName))
			{
				humidity = JsonConfig.biomeOverrides.get(biomeName).isDry ? 0.1f : 0.5f;
			}
			
			if (humidity < 0.2f && worldTime > 12000 && addedTemperature > 0.85f && !world.dimensionType().hasCeiling() && Config.Baked.biomeDrynessEffectEnabled)
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
		
		return applyUndergroundEffect(normalizeToPosNeg(biomeAverage) * ((float) Config.Baked.biomeTemperatureMultiplier), world, pos);
	}
}
