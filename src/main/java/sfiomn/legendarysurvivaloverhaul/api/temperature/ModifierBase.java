package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonBiomeIdentity;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

/**
 * Abstract class representing temperature modifiers.
 * @author Icey
 */
public abstract class ModifierBase {
	private static final float COLDEST_BIOME_TEMP = -0.5f;
	private static final float HOTTEST_BIOME_TEMP = 2.0f;
	/**
	 * Global World Modifiers
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
	 * Player Huddling
	 * --
	 * Unique Player Modifiers
	 * 
	 * Armor
	 * Sprinting
	 * Temporary
	 */
	
	/**
	 * Default temperature of the world. 
	 */
	protected final float defaultTemperature;
	
	public ModifierBase()
	{
		this.defaultTemperature = (TemperatureEnum.NORMAL.getUpperBound() + (float) TemperatureEnum.COLD.getUpperBound()) / 2;
	}
	
	/**
	 * Returns temperature from factors based directly on the player, such as what items they
	 * are holding, if they are sprinting, on fire, or what temporary modifiers they have. 
	 * 
	 * Although it is also possible to get data from the world by calling player$getWorld and 
	 * player$getPosition, it's not recommended as this will not affect thermometer temperatures
	 * or other items/blocks that depend on world influences
	 */
	public float getPlayerInfluence(Player player) { return 0.0f; }
	
	/*
	 * Returns temperature based on environmental factors, such as the biome at the given position,
	 * proximity to hot/cold blocks, altitude, time, weather, etc.
	 */
	public float getWorldInfluence(Level world, BlockPos pos) { return 0.0f; }
	
	
	protected float getNormalizedTempForBiome(Level world, Biome biome)
	{
		// Minecraft's temperatures is defined from -0.7 to 2.0, plains are at 0.8
		// Get the biome's temperature, clamp it between -0.5 and 2.0 in case of extreme biomes from other mods,
		// and then normalize it from 0 to 1
		// Plains returned temperature 0.44, savanna 0.7, Ice plain 0.26

		ResourceLocation name = WorldUtil.getBiomeName(world, biome);
		if (name != null && JsonConfig.biomeOverrides.containsKey(name.toString()))
		{
			JsonBiomeIdentity identity = JsonConfig.biomeOverrides.get(name.toString());

			return identity.temperature;
		}

		// LegendarySurvivalOverhaul.LOGGER.debug("Biome base temp for " + name + " is " + biome.getBaseTemperature());
		
		return clampNormalizeTemperature(biome.getBaseTemperature());
	}

	protected float applyUndergroundEffect(float temperature, Level level, BlockPos pos, float undergroundTemperature) {
		// If we're in a dimension that has a ceiling,
		// then just return the default value.
		if(level.dimensionType().hasCeiling()) {
			return temperature;
		}

		int surfaceDistance = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos).getY() - pos.getY();

		if (surfaceDistance < Config.Baked.undergroundEffectStartDistanceToWS) {
			return temperature;
		}

		if (surfaceDistance >= Config.Baked.undergroundEffectEndDistanceToWS) {
			return undergroundTemperature;
		}

		return Mth.lerp((surfaceDistance - Config.Baked.undergroundEffectStartDistanceToWS) / (float) (Config.Baked.undergroundEffectEndDistanceToWS - Config.Baked.undergroundEffectStartDistanceToWS), temperature, undergroundTemperature);
	}

	protected float getHumidityForBiome(Level world, Biome biome)
	{
		// Get the biome's humidity
		// Dry biomes have humidity below 0.2

		ResourceLocation name = WorldUtil.getBiomeName(world, biome);
		if (name != null && JsonConfig.biomeOverrides.containsKey(name.toString()))
		{
			JsonBiomeIdentity identity = JsonConfig.biomeOverrides.get(name.toString());

			return identity.isDry ? 0.1f : 0.5f;
		}

		return biome.getModifiedClimateSettings().downfall();
	}

	// Clamp and normalize the temperature
	protected float clampNormalizeTemperature(float temp)
	{
		return ((Mth.clamp(temp, COLDEST_BIOME_TEMP, HOTTEST_BIOME_TEMP)) - COLDEST_BIOME_TEMP ) / (HOTTEST_BIOME_TEMP - COLDEST_BIOME_TEMP);
	}

	//  Assume input is between 0 and 1
	//  Convert range to -1 and 1 instead
	protected float normalizeToPositiveNegative(float value)
	{
		return (value * 2.0f) - 1.0f;
	}
}
