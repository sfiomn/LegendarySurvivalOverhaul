package icey.survivaloverhaul.api.temperature;

import icey.survivaloverhaul.api.config.json.temperature.JsonBiomeIdentity;
import icey.survivaloverhaul.config.json.JsonConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class ModifierBase extends ForgeRegistryEntry<ModifierBase> implements ITemperatureModifier
{
	/**
	 * Unique World Modifiers
	 * 
	 * Altitude
	 * Biome
	 * Default
	 * Season // requires serene seasons
	 * Snow
	 * Time
	 * Humidity
	 * --
	 * Proximity World Modifiers
	 * 
	 * Blocks
	 * Tile Entities
	 * Player Huddling?
	 * --
	 * Unique Player Modifiers
	 * 
	 * Armor
	 * Sprinting
	 * Temporary
	 * Windchill?
	 */
	
	protected final float defaultTemperature;
	
	public ModifierBase()
	{
		this.defaultTemperature = (TemperatureEnum.NORMAL.getUpperBound() + TemperatureEnum.COLD.getUpperBound()) / 2;
	}
	
	@Override
	public float getPlayerInfluence(PlayerEntity player) { return 0.0f; }
	
	@Override
	public float getWorldInfluence(World world, BlockPos pos) { return 0.0f; }
	
	protected float applyUndergroundEffect(float temperature, World world, BlockPos pos)
	{
		// Code ripped and modified from 
		// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/temperature/ModifierBase.java
		
		//Y 64 - 256 is always unchanged temperature
		if (pos.getY() > 64)
		{
			return temperature;
		}
		
		// If we're in a dimension that has a ceiling,
		// then just return the default value.
		if(!true|| world.getDimensionType().getHasCeiling())
		{
			return temperature;
		}
		
		// Charles445's comments in this part of the code say
		// that there's probably an easier way to do this
		// that takes into account distance inside of a cave,
		// but fuck if I know
		if(world.canSeeSky(pos) || world.canSeeSky(pos.up()))
		{
			return temperature;
		}
		
		int cutoff = 48;
		
		if (pos.getY() <= cutoff || cutoff == 64)
		{
			return 0.0f;
		}
		
		return temperature * (float)(pos.getY() - cutoff) / (64.0f - cutoff);
	}
	
	protected float getTempForBiome(Biome biome)
	{
		// Get the biome's temperature, clamp it between 0 and 1.35,
		// and then normalize it to a value between 0 and 1.
		
		String name = biome.getRegistryName().toString();
		
		if (JsonConfig.biomeOverrides.containsKey(name))
		{
			JsonBiomeIdentity identity = JsonConfig.biomeOverrides.get(name);
			
			return clampTemperature(identity.temperature);
		}
		
		return clampTemperature(biome.getTemperature());
	}
	
	protected float clampTemperature(float temp)
	{
		return MathHelper.clamp(temp, 0.0f, 1.5f)/ 1.5f;
	}
	
	protected float normalizeToPosNeg(float value)
	{
		return (value * 2.0f) - 1.0f;
	}
}
