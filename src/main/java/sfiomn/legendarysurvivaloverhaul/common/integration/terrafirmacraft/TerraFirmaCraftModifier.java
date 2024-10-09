package sfiomn.legendarysurvivaloverhaul.common.integration.terrafirmacraft;

import net.dries007.tfc.util.climate.Climate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;


public class TerraFirmaCraftModifier extends ModifierBase
{
	public TerraFirmaCraftModifier()
	{
		super();
	}

	@Override
	public float getWorldInfluence(Player player, Level level, BlockPos pos)
	{
		if (!LegendarySurvivalOverhaul.terraFirmaCraftLoaded)
			return 0.0f;

		if (!Config.Baked.tfcTemperatureOverride)
			return 0.0f;
		
		try
		{
			// Inside a try/catch to make sure something weird
			// hasn't happened with the terrafirma code
			return getUncaughtWorldInfluence(level, pos);
		}
		catch (Exception e)
		{
			// If an error somehow occurs, disable compatibility 
			LegendarySurvivalOverhaul.LOGGER.error("An error has occurred with TerraFirmaCraft compatibility, disabling modifier", e);
			LegendarySurvivalOverhaul.terraFirmaCraftLoaded = false;
			
			return 0.0f;
		}
	}
	
	public float getUncaughtWorldInfluence(Level level, BlockPos pos)
	{
		Vec3i[] posOffsets = new Vec3i[]{
				new Vec3i(0, 0, 0),
				new Vec3i(10, 0, 0),
				new Vec3i(-10, 0, 0),
				new Vec3i(0, 0, 10),
				new Vec3i(0, 0, -10)
		};
		
		float value = 0.0f;

		for (Vec3i offset : posOffsets)
		{
			value += (float) (Climate.getTemperature(level, pos.offset(offset)) * Config.Baked.tfcTemperatureMultiplier);
        }

		value /= posOffsets.length;

		return value;
	}
}
