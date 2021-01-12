package icey.survivaloverhaul.common.capability.temperature;

import icey.survivaloverhaul.Main;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class TemperatureStorage implements IStorage<TemperatureCapability>
{
	@Override
	public INBT writeNBT(Capability<TemperatureCapability> capability, TemperatureCapability instance, Direction side)
	{
		return instance.save();
	}
	
	@Override
	public void readNBT(Capability<TemperatureCapability> capability, TemperatureCapability instance, Direction side, INBT nbt)
	{
		if (nbt instanceof CompoundNBT)
		{
			instance.load((CompoundNBT) nbt);
		}
	}
}