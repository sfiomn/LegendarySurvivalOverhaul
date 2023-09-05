package sfiomn.legendarysurvivaloverhaul.common.capabilities.food;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class FoodProvider implements ICapabilitySerializable<INBT>
{
	private final LazyOptional<FoodCapability> instance = LazyOptional.of(LegendarySurvivalOverhaul.FOOD_CAP::getDefaultInstance);
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		return LegendarySurvivalOverhaul.FOOD_CAP.orEmpty(cap, instance);
	}
	
	@Override
	public INBT serializeNBT()
	{
		return LegendarySurvivalOverhaul.FOOD_CAP.getStorage().writeNBT(LegendarySurvivalOverhaul.FOOD_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}
	
	@Override
	public void deserializeNBT(INBT nbt)
	{
		LegendarySurvivalOverhaul.FOOD_CAP.getStorage().readNBT(LegendarySurvivalOverhaul.FOOD_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}
