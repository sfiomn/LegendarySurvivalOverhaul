package sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ThirstStorage implements IStorage<ThirstCapability>
{
	@Override
	public INBT writeNBT(Capability<ThirstCapability> capability, ThirstCapability instance, Direction side)
	{
		return instance.writeNBT();
	}
	
	@Override
	public void readNBT(Capability<ThirstCapability> capability, ThirstCapability instance, Direction side, INBT nbt)
	{
		if (nbt instanceof CompoundNBT)
		{
			instance.readNBT((CompoundNBT) nbt);
		}
	}
}