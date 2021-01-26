package icey.survivaloverhaul.common.temperature;

import java.util.ArrayList;
import java.util.List;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.config.Config;
import icey.survivaloverhaul.config.json.JsonConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class BiomeModifier extends ModifierBase
{

	public BiomeModifier()
	{
		super();
		this.setRegistryName(Main.MOD_ID, "biome");
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
		
		long worldTime = world.getWorldInfo().getDayTime() % 24000;
		
		for (Vector3i offset : posOffsets)
		{
			Biome biome = world.getBiome(pos.add(offset));
			float humidity = biome.getDownfall();
			float addedTemperature = getTempForBiome(biome);
			
			String biomeName = biome.getRegistryName().toString();
			
			if (JsonConfig.biomeOverrides.containsKey(biomeName))
			{
				humidity = JsonConfig.biomeOverrides.get(biomeName).isDry ? 0.1f : 0.5f;
			}
			
			if (humidity < 0.2f && worldTime > 12000 && addedTemperature > 0.85f && !world.getDimensionType().getHasCeiling())
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
