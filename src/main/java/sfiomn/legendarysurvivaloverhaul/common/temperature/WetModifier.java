package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonBlockFluidTemperature;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessMode;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import java.util.List;

public class WetModifier extends ModifierBase
{
	public WetModifier()
	{
		super();
	}
	
	@Override
	public float getPlayerInfluence(Player player)
	{
		if (Config.Baked.wetnessEnabled)
		{
			WetnessCapability wetCap = CapabilityUtil.getWetnessCapability(player);
			if (wetCap.getWetness() == 0) {
				// LegendarySurvivalOverhaul.LOGGER.debug("Wet player temp influence : " + 0.0f);
				return 0.0f;
			} else {
				// LegendarySurvivalOverhaul.LOGGER.debug("Wet player temp influence : " + (float) (Config.Baked.wetMultiplier * MathUtil.invLerp(0, WetnessCapability.WETNESS_LIMIT, wetCap.getWetness())));
				return (float) (Config.Baked.wetMultiplier * MathUtil.invLerp(0, WetnessCapability.WETNESS_LIMIT, wetCap.getWetness()));
			}
		}
		return 0.0f;
	}
}
