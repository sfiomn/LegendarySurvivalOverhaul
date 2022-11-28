package sfiomn.legendarysurvivaloverhaul.common.capability.temperature;

import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class TemperatureProvider implements ICapabilitySerializable<INBT>
{
	private final LazyOptional<TemperatureCapability> instance = LazyOptional.of(LegendarySurvivalOverhaul.TEMPERATURE_CAP::getDefaultInstance);
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		return LegendarySurvivalOverhaul.TEMPERATURE_CAP.orEmpty(cap, instance);
	}
	
	@Override
	public INBT serializeNBT()
	{
		return LegendarySurvivalOverhaul.TEMPERATURE_CAP.getStorage().writeNBT(LegendarySurvivalOverhaul.TEMPERATURE_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}
	
	@Override
	public void deserializeNBT(INBT nbt)
	{
		LegendarySurvivalOverhaul.TEMPERATURE_CAP.getStorage().readNBT(LegendarySurvivalOverhaul.TEMPERATURE_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}
