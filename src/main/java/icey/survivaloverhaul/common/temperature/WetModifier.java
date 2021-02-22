package icey.survivaloverhaul.common.temperature;

import java.util.Map;

import icey.survivaloverhaul.api.config.json.temperature.JsonTemperature;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.common.capability.wetness.WetnessCapability;
import icey.survivaloverhaul.common.capability.wetness.WetnessMode;
import icey.survivaloverhaul.config.Config;
import icey.survivaloverhaul.config.json.JsonConfig;
import icey.survivaloverhaul.util.CapabilityUtil;
import icey.survivaloverhaul.util.MathUtil;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WetModifier extends ModifierBase
{
	public WetModifier()
	{
		super();
	}
	
	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		if (Config.Baked.wetnessMode != WetnessMode.SIMPLE)
			return 0.0f;
		
		FluidState state = world.getFluidState(pos);
		Fluid fluid = state.getFluid();
		
		if (!state.isEmpty())
		{
			for (Map.Entry<String, JsonTemperature> entry : JsonConfig.fluidTemperatures.entrySet())
			{
				if (entry.getValue() == null)
					continue;
				
				if (entry.getKey().contentEquals(fluid.getRegistryName().toString()))
				{
					return entry.getValue().temperature;
				}
			}
		}
		
		if (fluid.isEquivalentTo(Fluids.WATER) || fluid.isEquivalentTo(Fluids.FLOWING_WATER))
			return (float) Config.Baked.wetMultiplier;
		else if (world.isRainingAt(pos))
			return (float) Config.Baked.wetMultiplier;
		else
			return 0.0f;
	}
	
	@Override
	public float getPlayerInfluence(PlayerEntity player)
	{
		switch (Config.Baked.wetnessMode)
		{
			case SIMPLE:
				float worldInfluence = this.getWorldInfluence(player.world, player.getPosition());
				
				if (player.getRidingEntity() != null && worldInfluence != 0)
				{
					// If the player is in a boat, cancel out the effect
					
					if (player.getRidingEntity() instanceof BoatEntity)
					{
						return (float) -worldInfluence;
					}
				}
				break;
			case DYNAMIC:
				WetnessCapability wetCap = CapabilityUtil.getWetnessCapability(player);
				if (wetCap.getWetness() == 0)
					return 0.0f;
				else
					return (float) (Config.Baked.wetMultiplier * MathUtil.invLerp(0, WetnessCapability.WETNESS_LIMIT, wetCap.getWetness()));
			default:
				break;
		}
		return 0.0f;
	}
}
