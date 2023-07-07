package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperature;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessMode;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import java.util.Map;

public class WetModifier extends ModifierBase
{
	public WetModifier()
	{
		super();
	}
	
	@Override
	public float getWorldInfluence(World world, BlockPos pos) {
		if (Config.Baked.wetnessMode != WetnessMode.SIMPLE)
			return 0.0f;

		FluidState state = world.getFluidState(pos);
		Fluid fluid = state.getType();

		if (!state.isEmpty()) {
			for (Map.Entry<String, JsonTemperature> entry : JsonConfig.fluidTemperatures.entrySet()) {
				if (entry.getValue() == null)
					continue;

				if (entry.getKey().contentEquals(fluid.getRegistryName().toString())) {
					// LegendarySurvivalOverhaul.LOGGER.debug("Wet world temp influence : " + entry.getValue().temperature);
					return entry.getValue().temperature;
				}
			}
		}

		if (fluid.isSame(Fluids.WATER) || fluid.isSame(Fluids.FLOWING_WATER)) {
			// LegendarySurvivalOverhaul.LOGGER.debug("Wet world temp influence : " + String.valueOf(Config.Baked.wetMultiplier));
			return (float) Config.Baked.wetMultiplier;
		} else if (world.isRainingAt(pos)) {
			// LegendarySurvivalOverhaul.LOGGER.debug("Wet world temp influence : " + String.valueOf(Config.Baked.wetMultiplier));
			return (float) Config.Baked.wetMultiplier;
		} else {
			// LegendarySurvivalOverhaul.LOGGER.debug("Wet world temp influence : " + 0.0f);
			return 0.0f;
		}
	}
	
	@Override
	public float getPlayerInfluence(PlayerEntity player)
	{
		switch (Config.Baked.wetnessMode)
		{
			case SIMPLE:
				float worldInfluence = this.getWorldInfluence(player.level, player.blockPosition());
				
				if (player.getVehicle() != null && worldInfluence != 0)
				{
					// If the player is in a boat, cancel out the effect
					
					if (player.getVehicle() instanceof BoatEntity)
					{
						return (float) -worldInfluence;
					}
				}
				break;
			case DYNAMIC:
				WetnessCapability wetCap = CapabilityUtil.getWetnessCapability(player);
				if (wetCap.getWetness() == 0) {
					// LegendarySurvivalOverhaul.LOGGER.debug("Wet player temp influence : " + 0.0f);
					return 0.0f;
				} else {
					// LegendarySurvivalOverhaul.LOGGER.debug("Wet player temp influence : " + (float) (Config.Baked.wetMultiplier * MathUtil.invLerp(0, WetnessCapability.WETNESS_LIMIT, wetCap.getWetness())));
					return (float) (Config.Baked.wetMultiplier * MathUtil.invLerp(0, WetnessCapability.WETNESS_LIMIT, wetCap.getWetness()));
				}
			default:
				break;
		}
		return 0.0f;
	}
}
