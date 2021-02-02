package icey.survivaloverhaul.common.temperature;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.config.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class WeatherModifier extends ModifierBase
{
	public WeatherModifier()
	{
		super();
		this.setRegistryName(Main.MOD_ID, "weather");
	}
	
	// TODO: Try and get this also working with serene seasons
	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		if (world.isRainingAt(pos) && world.canSeeSky(pos))
		{
			Biome biome = world.getBiome(pos);
			
			if (biome.getTemperature(pos) < 0.15f)
			{
				return (float) Config.Baked.snowTemperatureModifier;
			}
			else if (biome.getTemperature(pos) >= 0.15f)
			{
				return (float) Config.Baked.rainTemperatureModifier;
			}
		}
		
		return 0.0f;
	}
}
