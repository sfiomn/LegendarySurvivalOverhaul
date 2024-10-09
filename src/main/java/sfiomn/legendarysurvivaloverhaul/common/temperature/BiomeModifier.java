package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.integration.terrafirmacraft.TerraFirmaCraftUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class BiomeModifier extends ModifierBase
{
	public BiomeModifier()
	{
		super();
	}
	
	@Override
	public float getWorldInfluence(@Nullable Player player, Level level, BlockPos pos)
	{
		if (TerraFirmaCraftUtil.shouldUseTerraFirmaCraftTemp())
			return 0.0f;

		Vec3i[] posOffsets =
			{
					new Vec3i(0, 0, 0),
					new Vec3i(10, 0, 0),
					new Vec3i(-10, 0, 0),
					new Vec3i(0, 0, 10),
					new Vec3i(0, 0, -10),
					new Vec3i(7, 0, 7),
					new Vec3i(7, 0, -7),
					new Vec3i(-7, 0, 7),
					new Vec3i(-7, 0, -7)
			};
		
		float biomeAverage = 0f;
		float undergroundAverage = 0f;
		
		long worldTime = level.getLevelData().getDayTime() % 24000;
		double drynessTimeMultiplier = 1;
		double drynessAverageMultiplier = 1;
		if (worldTime > 12000 && !level.dimensionType().hasCeiling() && Config.Baked.biomeDrynessMultiplier > 0) {
			drynessTimeMultiplier = 1 + Math.sin(worldTime * Math.PI / 12000) * Config.Baked.biomeDrynessMultiplier;
			drynessAverageMultiplier = (1 + Config.Baked.biomeDrynessMultiplier) / 2.0f;
		}

		for (Vec3i offset : posOffsets)
		{
			Biome biome = level.getBiome(pos.offset(offset)).get();
			float humidity = getHumidityForBiome(level, biome);
			float biomeTemperature = getNormalizedTempForBiome(level, biome);

			if (drynessTimeMultiplier < 1 && humidity < 0.2f && biomeTemperature > 0.80f)
			{
				// calculated average temp between day and night with the dryness effect (makes nights colder)
				undergroundAverage += (float) (drynessAverageMultiplier * biomeTemperature);
				// Deserts are cold at night since heat isn't kept by moisture in the air
				biomeAverage += (float) (drynessTimeMultiplier * biomeTemperature);
			}
			else
			{
				undergroundAverage += biomeTemperature;
				biomeAverage += biomeTemperature;
			}
		}
		
		biomeAverage /= (float)(posOffsets.length);
		undergroundAverage /= (float)(posOffsets.length);

		// LegendarySurvivalOverhaul.LOGGER.debug("Biome temp influence : " + normalizeToPositiveNegative(biomeAverage) * ((float) Config.Baked.biomeTemperatureMultiplier));
		// float tempInfl = applyUndergroundEffect(normalizeToPositiveNegative(biomeAverage) * ((float) Config.Baked.biomeTemperatureMultiplier), world, pos);
		// LegendarySurvivalOverhaul.LOGGER.debug("Biome temp influence after underground : " + tempInfl);
		return applyUndergroundEffect(
				(float) (normalizeToPositiveNegative(biomeAverage) * Config.Baked.biomeTemperatureMultiplier),
				level,
				pos,
                (float) (normalizeToPositiveNegative(undergroundAverage) * Config.Baked.biomeTemperatureMultiplier * Config.Baked.undergroundBiomeTemperatureMultiplier));
	}
}
