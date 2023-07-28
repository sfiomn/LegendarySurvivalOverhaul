package sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class ThirstProvider implements ICapabilitySerializable<INBT>
{
	private final LazyOptional<ThirstCapability> instance = LazyOptional.of(LegendarySurvivalOverhaul.THIRST_CAP::getDefaultInstance);
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		return LegendarySurvivalOverhaul.THIRST_CAP.orEmpty(cap, instance);
	}
	
	@Override
	public INBT serializeNBT()
	{
		return LegendarySurvivalOverhaul.THIRST_CAP.getStorage().writeNBT(LegendarySurvivalOverhaul.THIRST_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}
	
	@Override
	public void deserializeNBT(INBT nbt)
	{
		LegendarySurvivalOverhaul.THIRST_CAP.getStorage().readNBT(LegendarySurvivalOverhaul.THIRST_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}
