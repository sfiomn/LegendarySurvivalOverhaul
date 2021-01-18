package icey.survivaloverhaul.common.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.StateContainer;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class FluidBoilingWater extends ForgeFlowingFluid
{

	protected FluidBoilingWater(Properties properties)
	{
		super(properties);
	}
	
	public static FluidAttributes.Builder addAttributes(FluidAttributes.Builder builder)
	{
		return builder
				.density(100)
				.viscosity(100);
	}
	
	public static class Flowing extends FluidBoilingWater
	{
		public Flowing(Properties properties)
		{
			super(properties);
		}
		
		protected void fillStateContainer(StateContainer.Builder<Fluid, FluidState> builder)
		{
			super.fillStateContainer(builder);
			builder.add(LEVEL_1_8);
		}

		@Override
		public boolean isSource(FluidState state)
		{
			return false;
		}

		@Override
		public int getLevel(FluidState state)
		{
			return state.get(LEVEL_1_8);
		}
	}
	
	public static class Source extends FluidBoilingWater
	{

		public Source(Properties properties)
		{
			super(properties);
		}

		@Override
		public boolean isSource(FluidState state)
		{
			return true;
		}

		@Override
		public int getLevel(FluidState state)
		{
			return 8;
		}
		
	}
}
