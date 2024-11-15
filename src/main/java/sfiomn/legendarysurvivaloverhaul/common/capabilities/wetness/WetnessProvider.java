package sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;

public class WetnessProvider implements ICapabilitySerializable<INBT>
{
	private final LazyOptional<WetnessCapability> instance = LazyOptional.of(LegendarySurvivalOverhaul.WETNESS_CAP::getDefaultInstance);
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		return LegendarySurvivalOverhaul.WETNESS_CAP.orEmpty(cap, instance);
	}
	
	@Override
	public INBT serializeNBT()
	{
		return LegendarySurvivalOverhaul.WETNESS_CAP.getStorage().writeNBT(LegendarySurvivalOverhaul.WETNESS_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}
	
	@Override
	public void deserializeNBT(INBT nbt)
	{
		LegendarySurvivalOverhaul.WETNESS_CAP.getStorage().readNBT(LegendarySurvivalOverhaul.WETNESS_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}
