package icey.survivaloverhaul.common.temperature;

import java.util.Map;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.config.json.temperature.JsonTemperature;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.config.Config;
import icey.survivaloverhaul.config.json.JsonConfig;
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
		this.setRegistryName(Main.MOD_ID, "wet");
	}
	
	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
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
}
