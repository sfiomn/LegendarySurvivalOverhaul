package icey.survivaloverhaul.common.capability.temperature;

import icey.survivaloverhaul.Main;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class TemperatureProvider implements ICapabilitySerializable<INBT>
{
	private LazyOptional<TemperatureCapability> instance = LazyOptional.of(Main.TEMPERATURE_CAP::getDefaultInstance);
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		return Main.TEMPERATURE_CAP.orEmpty(cap, instance);
	}
	
	@Override
	public INBT serializeNBT()
	{
		return Main.TEMPERATURE_CAP.getStorage().writeNBT(Main.TEMPERATURE_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}
	
	@Override
	public void deserializeNBT(INBT nbt)
	{
		Main.TEMPERATURE_CAP.getStorage().readNBT(Main.TEMPERATURE_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}
