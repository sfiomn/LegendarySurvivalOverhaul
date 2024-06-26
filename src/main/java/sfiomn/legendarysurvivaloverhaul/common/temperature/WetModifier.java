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
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonPropertyTemperature;
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
	public float getWorldInfluence(Level level, BlockPos pos) {
		if (Config.Baked.wetnessMode != WetnessMode.SIMPLE)
			return 0.0f;

		FluidState state = level.getFluidState(pos);
		Fluid fluid = state.getType();

		if (!state.isEmpty()) {
			ResourceLocation fluidRegistryName = ForgeRegistries.FLUID_TYPES.get().getKey(fluid.getFluidType());
			if (fluidRegistryName != null) {
				List<JsonPropertyTemperature> tempPropertyList = JsonConfig.blockTemperatures.get(fluidRegistryName.toString());

				if (tempPropertyList == null) {
					return 0.0f;
				}

				for (JsonPropertyTemperature tempInfo : tempPropertyList) {
					if (tempInfo == null)
						continue;

					if (tempInfo.matchesState(state)) {
						return tempInfo.temperature;
					}
				}
			}
		}

		if (fluid.isSame(Fluids.WATER) || fluid.isSame(Fluids.FLOWING_WATER)) {
			return (float) Config.Baked.wetMultiplier;
		} else if (level.isRainingAt(pos)) {
			return (float) Config.Baked.wetMultiplier;
		} else {
			return 0.0f;
		}
	}
	
	@Override
	public float getPlayerInfluence(Player player)
	{
		switch (Config.Baked.wetnessMode)
		{
			case SIMPLE:
				float worldInfluence = this.getWorldInfluence(player.level(), player.blockPosition());
				
				if (player.getVehicle() != null && worldInfluence != 0)
				{
					// If the player is in a boat, cancel out the effect
					
					if (player.getVehicle() instanceof Boat)
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
